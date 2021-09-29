/*-
 * ========================LICENSE_START=================================
 * TeamApps Application API
 * ---
 * Copyright (C) 2020 - 2021 TeamApps.org
 * ---
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
package org.teamapps.application.ux.form;

import org.teamapps.application.api.application.ApplicationInstanceData;
import org.teamapps.application.api.localization.Dictionary;
import org.teamapps.application.api.privilege.OrganizationalPrivilegeGroup;
import org.teamapps.application.api.privilege.Privilege;
import org.teamapps.application.api.privilege.StandardPrivilegeGroup;
import org.teamapps.application.api.theme.ApplicationIcons;
import org.teamapps.application.api.ui.FormMetaFields;
import org.teamapps.application.tools.RecordModelBuilder;
import org.teamapps.application.ux.UiUtils;
import org.teamapps.databinding.TwoWayBindableValue;
import org.teamapps.event.Event;
import org.teamapps.model.controlcenter.OrganizationUnitView;
import org.teamapps.universaldb.pojo.Entity;
import org.teamapps.ux.application.view.View;
import org.teamapps.ux.component.dialogue.Dialogue;
import org.teamapps.ux.component.field.AbstractField;
import org.teamapps.ux.component.field.FieldEditingMode;
import org.teamapps.ux.component.field.FieldMessage;
import org.teamapps.ux.component.field.Fields;
import org.teamapps.ux.component.form.AbstractForm;
import org.teamapps.ux.component.form.ResponsiveFormLayout;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.toolbar.Toolbar;
import org.teamapps.ux.component.toolbar.ToolbarButton;
import org.teamapps.ux.component.toolbar.ToolbarButtonGroup;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class FormController<ENTITY extends Entity<?>> extends FormValidator {

	public final Event<ENTITY> onEntityCreated = new Event<>();
	public final Event<ENTITY> onEntityUpdated = new Event<>();
	public final Event<ENTITY> onEntityDeleted = new Event<>();
	public final Event<ENTITY> onEntityRestored = new Event<>();
	public final Event<ENTITY> onEntityAnyChanged = new Event<>();

	private final AbstractForm<?, ENTITY> form;
	private final Set<AbstractField<?>> otherFields = new HashSet<>();
	private final TwoWayBindableValue<ENTITY> selectedEntity;
	private final ApplicationInstanceData applicationInstanceData;

	private Supplier<ENTITY> createNewEntitySupplier;
	private Predicate<ENTITY> saveEntityHandler;
	private Predicate<ENTITY> revertChangesHandler;
	private Predicate<ENTITY> deleteEntityHandler;
	private Predicate<ENTITY> restoreEntityHandler;

	private StandardPrivilegeGroup standardPrivilegeGroup;

	private OrganizationalPrivilegeGroup organizationalPrivilegeGroup;
	private Function<ENTITY, OrganizationUnitView> entityOrganizationUnitSelector;
	private AbstractField<OrganizationUnitView> organizationUnitViewField;

	private boolean updateFieldEditMode;
	private final Set<AbstractField<?>> nonEditableFields = new HashSet<>();

	private ToolbarButton newButton;
	private ToolbarButton saveButton;
	private ToolbarButton revertButton;
	private ToolbarButton deleteButton;
	private ToolbarButton restoreButton;
	private List<ToolbarButtonGroup> toolbarButtonGroups;

	private boolean isModified;

	private boolean autoApplyFieldValuesToRecord;
	private boolean autoApplyRecordValuesToFields;

	public FormController(ApplicationInstanceData applicationInstanceData, AbstractForm<?, ENTITY> form, TwoWayBindableValue<ENTITY> selectedEntity, Supplier<ENTITY> createNewEntitySupplier, StandardPrivilegeGroup standardPrivilegeGroup) {
		this(applicationInstanceData, form, selectedEntity, createNewEntitySupplier);
		this.standardPrivilegeGroup = standardPrivilegeGroup;
	}

	public FormController(ApplicationInstanceData applicationInstanceData, AbstractForm<?, ENTITY> form, TwoWayBindableValue<ENTITY> selectedEntity, Supplier<ENTITY> createNewEntitySupplier, OrganizationalPrivilegeGroup organizationalPrivilegeGroup, Function<ENTITY, OrganizationUnitView> entityOrganizationUnitSelector) {
		this(applicationInstanceData, form, selectedEntity, createNewEntitySupplier);
		this.organizationalPrivilegeGroup = organizationalPrivilegeGroup;
		this.entityOrganizationUnitSelector = entityOrganizationUnitSelector;
		if (entityOrganizationUnitSelector == null) {
			throw new RuntimeException("Missing entity org unit selector!");
		}
		createOrganizationUnitField();
	}

	private FormController(ApplicationInstanceData applicationInstanceData, AbstractForm<?, ENTITY> form, TwoWayBindableValue<ENTITY> selectedEntity, Supplier<ENTITY> createNewEntitySupplier) {
		super(applicationInstanceData);
		this.form = form;
		this.selectedEntity = selectedEntity;
		this.applicationInstanceData = applicationInstanceData;
		this.createNewEntitySupplier = createNewEntitySupplier;
		init(applicationInstanceData);
		form.onFieldValueChanged.addListener(event -> handleFieldUpdateByClient(event.getField()));
		selectedEntity.onChanged().addListener(this::handleEntitySelection);
		init(applicationInstanceData);
		onEntityCreated.addListener((Runnable) onEntityAnyChanged::fire);
		onEntityUpdated.addListener((Runnable) onEntityAnyChanged::fire);
		onEntityDeleted.addListener((Runnable) onEntityAnyChanged::fire);
		onEntityRestored.addListener((Runnable) onEntityAnyChanged::fire);

	}

	private void init(ApplicationInstanceData applicationInstanceData) {
		newButton = FormButtonUtils.createNewButton(applicationInstanceData);
		saveButton = FormButtonUtils.createSaveButton(applicationInstanceData);
		revertButton = FormButtonUtils.createRevertButton(applicationInstanceData);
		deleteButton = FormButtonUtils.createDeleteButton(applicationInstanceData);
		restoreButton = FormButtonUtils.createRestoreButton(applicationInstanceData);

		newButton.setVisible(isEntityCreationAllowed());
		saveButton.setVisible(false);
		revertButton.setVisible(false);
		deleteButton.setVisible(false);
		restoreButton.setVisible(false);

		toolbarButtonGroups = new ArrayList<>();
		ToolbarButtonGroup buttonGroup = new ToolbarButtonGroup();
		buttonGroup.addButton(newButton);
		buttonGroup.addButton(saveButton);
		toolbarButtonGroups.add(buttonGroup);
		buttonGroup = new ToolbarButtonGroup();
		buttonGroup.addButton(revertButton);
		toolbarButtonGroups.add(buttonGroup);
		buttonGroup = new ToolbarButtonGroup();
		buttonGroup.addButton(deleteButton);
		buttonGroup.addButton(restoreButton);
		toolbarButtonGroups.add(buttonGroup);

		newButton.onClick.addListener(() -> {
			if (isEntityCreationAllowed()) {
				ENTITY entity = createNewEntitySupplier.get();
				selectedEntity.set(entity);
				newButton.setVisible(false);
				revertButton.setVisible(false);
			}
		});

		saveButton.onClick.addListener(() -> {
			ENTITY entity = selectedEntity.get();
			if (validate() && (saveEntityHandler == null || saveEntityHandler.test(entity))) {
				boolean stored = entity.isStored();
				if (autoApplyFieldValuesToRecord) {
					form.applyFieldValuesToRecord(entity);
				}
				entity.save();
				saveButton.setVisible(false);
				restoreButton.setVisible(false);
				newButton.setVisible(isEntityCreationAllowed());
				if (stored) {
					onEntityUpdated.fire(entity);
				} else {
					onEntityCreated.fire(entity);
				}
				UiUtils.showSaveNotification(true, applicationInstanceData);
			} else {
				UiUtils.showSaveNotification(false, applicationInstanceData);
			}
		});

		revertButton.onClick.addListener(() -> {
			ENTITY entity = selectedEntity.get();
			if (revertChangesHandler == null || revertChangesHandler.test(entity)) {
				selectedEntity.onChanged().fire(entity);
				saveButton.setVisible(false);
				revertButton.setVisible(false);
				newButton.setVisible(isEntityCreationAllowed());
				deleteButton.setVisible(isEntityDeletable(selectedEntity.get()));
				markAllFieldsUnchanged();
			}
		});

		deleteButton.onClick.addListener(() -> {
			ENTITY entity = selectedEntity.get();
			if (isEntityDeletable(entity)) {
				Dialogue.showOkCancel(ApplicationIcons.DELETE, applicationInstanceData.getLocalized(Dictionary.DELETE_RECORD), applicationInstanceData.getLocalized(Dictionary.SENTENCE_DO_YOU_REALLY_WANT_TO_DELETE_THE_RE__)).addListener(result -> {
					if (result) {
						if (deleteEntityHandler == null || deleteEntityHandler.test(entity)) {
							entity.delete();
							deleteButton.setVisible(false);
							restoreButton.setVisible(isEntityRestorable(entity));
							onEntityDeleted.fire(entity);
						}
					}
				});
			}
		});

		restoreButton.onClick.addListener(() -> {
			ENTITY entity = selectedEntity.get();
			if (isEntityRestorable(entity)) {
				if (restoreEntityHandler == null || revertChangesHandler.test(entity)) {
					entity.restoreDeleted();
					restoreButton.setVisible(false);
					deleteButton.setVisible(isEntityDeletable(selectedEntity.get()));
					onEntityRestored.fire(entity);
				}
			}
		});

		selectedEntity.onChanged().addListener(entity -> {
			newButton.setVisible(isEntityCreationAllowed());
			deleteButton.setVisible(isEntityDeletable(entity));
			restoreButton.setVisible(isEntityRestorable(entity));
			revertButton.setVisible(false);
			clearMessages();
			markAllFieldsUnchanged();
		});
	}

	private void createOrganizationUnitField() {
		List<OrganizationUnitView> allowedUnitsForCreation = applicationInstanceData.getAllowedUnits(organizationalPrivilegeGroup, Privilege.CREATE);
		List<OrganizationUnitView> allowedUnitsForModification = applicationInstanceData.getAllowedUnits(organizationalPrivilegeGroup, Privilege.UPDATE);
		if (allowedUnitsForCreation.size() > 1 || allowedUnitsForModification.size() > 1) {
			organizationUnitViewField = applicationInstanceData.getComponentFactory().createOrganizationUnitComboBox(() -> {
				if (selectedEntity.get().isStored()) {
					return allowedUnitsForModification;
				} else {
					return allowedUnitsForCreation;
				}
			});
		} else {
			organizationUnitViewField = applicationInstanceData.getComponentFactory().createOrganizationUnitTemplateField();
		}
		organizationUnitViewField.addValidator(organizationUnitView -> {
			ENTITY entity = selectedEntity.get();
			Privilege privilege = entity.isStored() ? Privilege.UPDATE : Privilege.CREATE;
			if (applicationInstanceData.isAllowed(organizationalPrivilegeGroup, privilege, organizationUnitViewField.getValue())) {
				return Collections.emptyList();
			} else {
				return Collections.singletonList(new FieldMessage(FieldMessage.Severity.ERROR, applicationInstanceData.getLocalized(Dictionary.ORGANIZATION))); //todo change error message
			}
		});
		addFieldWithValidator(organizationUnitViewField);
		selectedEntity.onChanged().addListener(entity -> organizationUnitViewField.setValue(entityOrganizationUnitSelector.apply(entity)));
	}

	public void registerModelBuilder(RecordModelBuilder<ENTITY> modelBuilder) {
		onEntityCreated.addListener(entity -> modelBuilder.onDataChanged.fire());
		onEntityUpdated.addListener(entity -> modelBuilder.onDataChanged.fire());
		onEntityDeleted.addListener(entity -> modelBuilder.onDataChanged.fire());
		onEntityRestored.addListener(entity -> modelBuilder.onDataChanged.fire());
	}

	public void registerView(View view) {
		toolbarButtonGroups.forEach(view::addLocalButtonGroup);
	}

	public void setCreateNewEntitySupplier(Supplier<ENTITY> createNewEntitySupplier) {
		this.createNewEntitySupplier = createNewEntitySupplier;
	}

	public void setUpdateFieldEditMode(boolean updateFieldEditMode) {
		this.updateFieldEditMode = updateFieldEditMode;
		if (updateFieldEditMode) {
			nonEditableFields.clear();
			Stream.concat(form.getFields().stream(), otherFields.stream())
					.filter(f -> f.getEditingMode() != FieldEditingMode.EDITABLE)
					.forEach(nonEditableFields::add);
		}
	}

	public void addFormFields(List<AbstractField<?>> fields) {
		fields.stream().filter(f -> !otherFields.contains(f)).forEach(f -> {
			otherFields.add(f);
			handleFieldUpdateByClient(f);
		});
	}

	private void handleEntitySelection(ENTITY entity) {
		if (isModified) {
			isModified = false;
			markAllFieldsUnchanged();
			clearMessages();
		}
		if (autoApplyRecordValuesToFields) {
			form.applyRecordValuesToFields(entity);
		}
		if (this.updateFieldEditMode) {
			boolean editable = !entity.isDeleted() && isEntityEditable(entity);
			Stream.concat(form.getFields().stream(), otherFields.stream())
					.filter(f -> !nonEditableFields.contains(f))
					.forEach(f -> f.setEditingMode(editable ? FieldEditingMode.EDITABLE : FieldEditingMode.READONLY));
		}
	}

	public void handleFieldUpdateByClient(AbstractField<?> field) {
		markFieldChanged(field);
		if (!saveButton.isVisible()) {
			ENTITY entity = selectedEntity.get();
			if (isEntityEditable(entity)) {
				saveButton.setVisible(true);
				revertButton.setVisible(true);
				newButton.setVisible(false);
				deleteButton.setVisible(false);
				isModified = true;
			}
		}
	}

	private boolean isEntityEditable(ENTITY entity) {
		if (entity == null) {
			return false;
		}
		Privilege privilege = entity.isStored() ? Privilege.UPDATE : Privilege.CREATE;
		if (standardPrivilegeGroup != null) {
			return applicationInstanceData.isAllowed(standardPrivilegeGroup, privilege);
		} else {
			OrganizationUnitView selectedOrganizationUnit = entityOrganizationUnitSelector.apply(entity);
			if (entity.isStored() || selectedOrganizationUnit != null) {
				return applicationInstanceData.isAllowed(organizationalPrivilegeGroup, privilege, selectedOrganizationUnit);
			} else {
				return !applicationInstanceData.getAllowedUnits(organizationalPrivilegeGroup, Privilege.CREATE).isEmpty();
			}
		}
	}

	private boolean isEntityDeletable(ENTITY entity) {
		if (entity == null || !entity.isStored() || entity.isDeleted()) {
			return false;
		}
		Privilege privilege = Privilege.DELETE;
		if (standardPrivilegeGroup != null) {
			return applicationInstanceData.isAllowed(standardPrivilegeGroup, privilege);
		} else {
			return applicationInstanceData.isAllowed(organizationalPrivilegeGroup, privilege, entityOrganizationUnitSelector.apply(entity));
		}
	}

	private boolean isEntityRestorable(ENTITY entity) {
		if (entity == null || !entity.isDeleted() || !entity.isRestorable()) {
			return false;
		}
		Privilege privilege = Privilege.RESTORE;
		if (standardPrivilegeGroup != null) {
			return applicationInstanceData.isAllowed(standardPrivilegeGroup, privilege);
		} else {
			return applicationInstanceData.isAllowed(organizationalPrivilegeGroup, privilege, entityOrganizationUnitSelector.apply(entity));
		}
	}

	private boolean isEntityCreationAllowed() {
		return standardPrivilegeGroup != null ? applicationInstanceData.isAllowed(standardPrivilegeGroup, Privilege.CREATE) : !applicationInstanceData.getAllowedUnits(organizationalPrivilegeGroup, Privilege.CREATE).isEmpty();
	}

	public AbstractField<OrganizationUnitView> getOrganizationUnitViewField() {
		return organizationUnitViewField;
	}

	public List<ToolbarButtonGroup> getToolbarButtonGroups() {
		return toolbarButtonGroups;
	}

	public void addToolbarButtonGroup(ToolbarButtonGroup buttonGroup) {
		toolbarButtonGroups.add(buttonGroup);
	}

	public void addMetaDataSection(ResponsiveFormLayout formLayout) {
		FormMetaFields formMetaFields = applicationInstanceData.getComponentFactory().createFormMetaFields();
		formMetaFields.addMetaFields(formLayout, false);
		selectedEntity.onChanged().addListener(formMetaFields::updateEntity);
	}

	protected void markFieldChanged(AbstractField<?> field) {
		field.setCssStyle(".field-border", "border-color", "#ec9a1a");
		field.setCssStyle(".field-border-glow", "box-shadow", "0 0 3px 0 #ec9a1a");
	}

	protected void markAllFieldsUnchanged() {
		form.getFields().forEach(field -> {
			field.setCssStyle(".field-border", "border-color", null);
			field.setCssStyle(".field-border-glow", "box-shadow", null);
		});
	}

	@Override
	public boolean validate() {
		boolean validationResult = super.validate();
		if (validationResult) {
			validationResult = Fields.validateAll(new ArrayList<>(otherFields));
			if (validationResult) {
				ENTITY entity = selectedEntity.get();
				return isEntityEditable(entity);
			}
		}
		return false;
	}

	public ToolbarButton getNewButton() {
		return newButton;
	}

	public ToolbarButton getSaveButton() {
		return saveButton;
	}

	public ToolbarButton getRevertButton() {
		return revertButton;
	}

	public ToolbarButton getDeleteButton() {
		return deleteButton;
	}

	public ToolbarButton getRestoreButton() {
		return restoreButton;
	}

	public void setSaveEntityHandler(Predicate<ENTITY> saveEntityHandler) {
		this.saveEntityHandler = saveEntityHandler;
	}

	public void setRevertChangesHandler(Predicate<ENTITY> revertChangesHandler) {
		this.revertChangesHandler = revertChangesHandler;
	}

	public void setDeleteEntityHandler(Predicate<ENTITY> deleteEntityHandler) {
		this.deleteEntityHandler = deleteEntityHandler;
	}

	public void setRestoreEntityHandler(Predicate<ENTITY> restoreEntityHandler) {
		this.restoreEntityHandler = restoreEntityHandler;
	}

	public boolean isAutoApplyFieldValuesToRecord() {
		return autoApplyFieldValuesToRecord;
	}

	public void setAutoApplyFieldValuesToRecord(boolean autoApplyFieldValuesToRecord) {
		this.autoApplyFieldValuesToRecord = autoApplyFieldValuesToRecord;
	}

	public boolean isAutoApplyRecordValuesToFields() {
		return autoApplyRecordValuesToFields;
	}

	public void setAutoApplyRecordValuesToFields(boolean autoApplyRecordValuesToFields) {
		this.autoApplyRecordValuesToFields = autoApplyRecordValuesToFields;
	}
}
