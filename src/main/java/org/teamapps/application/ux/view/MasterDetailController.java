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
package org.teamapps.application.ux.view;

import org.teamapps.application.api.application.ApplicationInstanceData;
import org.teamapps.application.api.application.ApplicationInstanceDataMethods;
import org.teamapps.application.api.localization.Dictionary;
import org.teamapps.application.api.privilege.OrganizationalPrivilegeGroup;
import org.teamapps.application.api.privilege.Privilege;
import org.teamapps.application.api.privilege.PrivilegeGroup;
import org.teamapps.application.api.privilege.StandardPrivilegeGroup;
import org.teamapps.application.api.theme.ApplicationIcons;
import org.teamapps.application.api.ui.FormMetaFields;
import org.teamapps.application.tools.EntityModelBuilder;
import org.teamapps.application.tools.PrivilegeUtils;
import org.teamapps.application.ux.form.FormController;
import org.teamapps.common.format.Color;
import org.teamapps.icons.Icon;
import org.teamapps.icons.composite.CompositeIcon;
import org.teamapps.model.controlcenter.OrganizationUnitView;
import org.teamapps.universaldb.index.numeric.NumericFilter;
import org.teamapps.universaldb.index.reference.single.SingleReferenceIndex;
import org.teamapps.universaldb.index.reference.value.RecordReference;
import org.teamapps.universaldb.pojo.AbstractUdbQuery;
import org.teamapps.universaldb.pojo.Entity;
import org.teamapps.universaldb.pojo.Query;
import org.teamapps.universaldb.query.IndexFilter;
import org.teamapps.ux.application.layout.ExtendedLayout;
import org.teamapps.ux.application.perspective.Perspective;
import org.teamapps.ux.application.view.View;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.field.combobox.ComboBox;
import org.teamapps.ux.component.form.ResponsiveForm;
import org.teamapps.ux.component.form.ResponsiveFormLayout;
import org.teamapps.ux.component.itemview.SimpleItemGroup;
import org.teamapps.ux.component.itemview.SimpleItemView;
import org.teamapps.ux.component.timegraph.TimeGraph;
import org.teamapps.ux.component.toolbar.Toolbar;
import org.teamapps.ux.component.toolbar.ToolbarButton;
import org.teamapps.ux.component.toolbar.ToolbarButtonGroup;
import org.teamapps.ux.component.window.Window;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MasterDetailController<ENTITY extends Entity<ENTITY>> implements ApplicationInstanceDataMethods {

	private final Icon entityIcon;
	private final String entityTitle;
	private final ApplicationInstanceData applicationInstanceData;
	private final EntityModelBuilder<ENTITY> entityModelBuilder;
	private final FormController<ENTITY> formController;
	private final ResponsiveForm<ENTITY> responsiveForm;

	private DetailPosition detailPosition = DetailPosition.RIGHT;
	private Component masterComponent;
	private Component detailComponent;

	private PrivilegeGroup privilegeGroup;

	private boolean singleViewMenu;
	private View timeGraphView;
	private View masterView;
	private View detailViewRight;
	private View detailViewBottom;
	private Window window;
	private TimeGraph timeGraph;
	private ComboBox<String> timeGraphFieldSelectionCombobox;
	private Toolbar formToolbar;
	private ToolbarButton windowCloseButton;
	private ToolbarButton editorNextButton;
	private ToolbarButton editorPreviousButton;

	public static enum DetailPosition {
		RIGHT, BOTTOM, CENTER, WINDOW
	}

	public MasterDetailController(Icon entityIcon, String entityTitle, ApplicationInstanceData applicationInstanceData, EntityModelBuilder<ENTITY> entityModelBuilder, FormController<ENTITY> formController, ResponsiveForm<ENTITY> responsiveForm) {
		this.entityIcon = entityIcon;
		this.entityTitle = entityTitle;
		this.applicationInstanceData = applicationInstanceData;
		this.entityModelBuilder = entityModelBuilder;
		this.formController = formController;
		this.responsiveForm = responsiveForm;
		init();
	}

	public MasterDetailController(Icon entityIcon, String entityTitle, ApplicationInstanceData applicationInstanceData, Supplier<Query<ENTITY>> querySupplier, StandardPrivilegeGroup standardPrivilegeGroup) {
		this.entityIcon = entityIcon;
		this.entityTitle = entityTitle;
		this.applicationInstanceData = applicationInstanceData;
		this.entityModelBuilder = new EntityModelBuilder<>(querySupplier, applicationInstanceData);
		this.responsiveForm = new ResponsiveForm<>(120, 120, 0);
		this.formController = new FormController<>(applicationInstanceData, responsiveForm, entityModelBuilder.getSelectedRecordBindableValue(), () -> entityModelBuilder.getEntityBuilder().build(), standardPrivilegeGroup);
		init();
	}

	public MasterDetailController(Icon entityIcon, String entityTitle, ApplicationInstanceData applicationInstanceData, Supplier<Query<ENTITY>> querySupplier, OrganizationalPrivilegeGroup organizationalPrivilegeGroup) {
		this.entityIcon = entityIcon;
		this.entityTitle = entityTitle;
		this.applicationInstanceData = applicationInstanceData;
		this.entityModelBuilder = new EntityModelBuilder<>(querySupplier, applicationInstanceData);
		this.responsiveForm = new ResponsiveForm<>(120, 120, 0);
		this.formController = new FormController<>(applicationInstanceData, responsiveForm, entityModelBuilder.getSelectedRecordBindableValue(), () -> entityModelBuilder.getEntityBuilder().build(), organizationalPrivilegeGroup, entityModelBuilder.createEntityOrganizationUnitViewFunction());
		init();
	}

	public MasterDetailController(Icon entityIcon, String entityTitle, ApplicationInstanceData applicationInstanceData, Supplier<Query<ENTITY>> querySupplier, OrganizationalPrivilegeGroup organizationalPrivilegeGroup, String orgUnitField) {
		this.entityIcon = entityIcon;
		this.entityTitle = entityTitle;
		this.applicationInstanceData = applicationInstanceData;
		this.entityModelBuilder = new EntityModelBuilder<>(PrivilegeUtils.createQueryOrgUnitFilter(querySupplier,orgUnitField,organizationalPrivilegeGroup,Privilege.READ,applicationInstanceData), applicationInstanceData);
		this.responsiveForm = new ResponsiveForm<>(120, 120, 0);
		this.formController = new FormController<>(applicationInstanceData, responsiveForm, entityModelBuilder.getSelectedRecordBindableValue(), () -> entityModelBuilder.getEntityBuilder().build(), organizationalPrivilegeGroup, entityModelBuilder.createEntityOrganizationUnitViewFunction());
		init();
	}


	private void init() {
		formController.registerModelBuilder(entityModelBuilder);
		detailComponent = responsiveForm;
		timeGraph = entityModelBuilder.createTimeGraph();
		timeGraphFieldSelectionCombobox = entityModelBuilder.createTimeGraphFieldSelectionCombobox(timeGraph);

		entityModelBuilder.getOnSelectionEvent().addListener(entity -> {
			switch (detailPosition) {
				case RIGHT -> detailViewRight.focus();
				case BOTTOM -> detailViewBottom.focus();
				case CENTER -> {
				}
				case WINDOW -> window.show();
			}
		});
	}

	public void createViews(Perspective perspective, Component masterComponent, ResponsiveFormLayout formLayout) {
		createViews(perspective, masterComponent, formLayout, true);
	}

	public void createViews(Perspective perspective, Component masterComponent, ResponsiveFormLayout formLayout, boolean withMetaFields) {
		this.masterComponent = masterComponent;
		timeGraphView = perspective.addView(View.createView(ExtendedLayout.TOP, ApplicationIcons.CHART_LINE, getLocalized(Dictionary.TIMELINE), null));
		masterView = perspective.addView(View.createView(ExtendedLayout.CENTER, entityIcon, entityTitle, masterComponent));
		detailViewRight = perspective.addView(View.createView(ExtendedLayout.RIGHT, entityIcon, entityTitle, null));
		detailViewBottom = perspective.addView(View.createView(ExtendedLayout.CENTER_BOTTOM, entityIcon, entityTitle, null));

		detailViewRight.getPanel().setBodyBackgroundColor(Color.WHITE.withAlpha(0.9f));
		detailViewBottom.getPanel().setBodyBackgroundColor(Color.WHITE.withAlpha(0.9f));

		timeGraphView.setVisible(false);
		detailViewBottom.setVisible(false);
		timeGraphView.getPanel().setRightHeaderField(timeGraphFieldSelectionCombobox);
		timeGraphView.setComponent(timeGraph);

		entityModelBuilder.attachViewCountHandler(masterView, () -> entityTitle);
		entityModelBuilder.attachSearchField(masterView);
		formController.registerView(detailViewRight);

		detailViewRight.setComponent(responsiveForm);

		window = new Window(1200, 800, null);
		window.setTitle(entityTitle);
		window.setIcon(entityIcon);
		window.setCloseable(true);
		window.setMaximizable(true);
		window.setCloseOnEscape(true);

		formToolbar = detailViewRight.getPanel().getToolbar();
		ToolbarButtonGroup buttonGroup = formToolbar.addButtonGroup(new ToolbarButtonGroup());

		editorPreviousButton = buttonGroup.addButton(ToolbarButton.createSmall(ApplicationIcons.NAVIGATE_LEFT, getLocalized(Dictionary.PREVIOUS)));
		editorNextButton = buttonGroup.addButton(ToolbarButton.createSmall(ApplicationIcons.NAVIGATE_RIGHT, getLocalized(Dictionary.NEXT)));
		editorPreviousButton.setVisible(false);
		editorNextButton.setVisible(false);

		buttonGroup = formToolbar.addButtonGroup(new ToolbarButtonGroup());
		windowCloseButton = buttonGroup.addButton(ToolbarButton.createSmall(ApplicationIcons.WINDOW_CLOSE, getLocalized(Dictionary.CLOSE)));
		windowCloseButton.setVisible(false);

		editorPreviousButton.onClick.addListener(entityModelBuilder::selectPreviousRecord);
		editorNextButton.onClick.addListener(entityModelBuilder::selectNextRecord);
		windowCloseButton.onClick.addListener(() -> window.close());

		if (withMetaFields) {
			FormMetaFields formMetaFields = getApplicationInstanceData().getComponentFactory().createFormMetaFields();
			formMetaFields.addMetaFields(formLayout, false);
			entityModelBuilder.getOnSelectionEvent().addListener(formMetaFields::updateEntity);
		}

		createToolbarButtons(perspective);
	}

	public void setDetailComponent(Component detailComponent) {
		this.detailComponent = detailComponent;
		setDetailPosition(detailPosition);
	}

	private void createToolbarButtons(Perspective perspective) {
		ToolbarButtonGroup buttonGroup = perspective.addWorkspaceButtonGroup(new ToolbarButtonGroup());
		ToolbarButton showTimeGraphButton = buttonGroup.addButton(ToolbarButton.create(ApplicationIcons.CHART_LINE, getLocalized(Dictionary.TIMELINE), getLocalized(Dictionary.TIMELINE)));
		ToolbarButton hideTimeGraphButton = buttonGroup.addButton(ToolbarButton.create(CompositeIcon.of(ApplicationIcons.CHART_LINE, ApplicationIcons.ERROR), getLocalized(Dictionary.TIMELINE), getLocalized(Dictionary.TIMELINE)));
		hideTimeGraphButton.setVisible(false);

		buttonGroup = perspective.addWorkspaceButtonGroup(new ToolbarButtonGroup());
		ToolbarButton showDeletedButton = buttonGroup.addButton(ToolbarButton.create(ApplicationIcons.GARBAGE_EMPTY, getLocalized(Dictionary.RECYCLE_BIN), getLocalized(Dictionary.SHOW_RECYCLE_BIN)));
		ToolbarButton hideDeletedButton = buttonGroup.addButton(ToolbarButton.create(CompositeIcon.of(ApplicationIcons.GARBAGE_EMPTY, ApplicationIcons.ERROR), getLocalized(Dictionary.RECYCLE_BIN), getLocalized(Dictionary.RECYCLE_BIN)));
		hideDeletedButton.setVisible(false);

		buttonGroup = perspective.addWorkspaceButtonGroup(new ToolbarButtonGroup());
		ToolbarButton viewButton = buttonGroup.addButton(ToolbarButton.create(ApplicationIcons.WINDOWS, getLocalized(Dictionary.VIEW), getLocalized(Dictionary.VIEW)));
		viewButton.setDroDownPanelWidth(400);
		SimpleItemView<?> simpleItemView = new SimpleItemView<>();
		SimpleItemGroup<?> itemGroup = simpleItemView.addSingleColumnGroup(ApplicationIcons.WINDOWS, getLocalized(Dictionary.VIEW));
		itemGroup.addItem(ApplicationIcons.WINDOW_SPLIT_HOR, getLocalized(Dictionary.EDITOR_PANE_ON_THE_RIGHT), getLocalized(Dictionary.SENTENCE_DISPLAY_THE_EDITOR_PANE_ON_THE_RIGH__)).onClick.addListener(() -> setDetailPosition(DetailPosition.RIGHT));
		itemGroup.addItem(ApplicationIcons.WINDOW_SPLIT_VER, getLocalized(Dictionary.EDITOR_PANE_BELOW), getLocalized(Dictionary.SENTENCE_DISPLAY_THE_EDITOR_PANE_BELOW_THE_M__)).onClick.addListener(() -> setDetailPosition(DetailPosition.BOTTOM));
		itemGroup.addItem(ApplicationIcons.WINDOW_DIALOG, getLocalized(Dictionary.CENTRAL_EDITOR_PANE), getLocalized(Dictionary.SENTENCE_DISPLAY_THE_EDITOR_PANE_IN_THE_CENT__)).onClick.addListener(() -> setDetailPosition(DetailPosition.CENTER));
		itemGroup.addItem(ApplicationIcons.WINDOWS, getLocalized(Dictionary.EDITOR_PANE_AS_WINDOW), getLocalized(Dictionary.SENTENCE_DISPLAY_THE_EDITOR_PANE_AS_POPUP_WI__)).onClick.addListener(() -> setDetailPosition(DetailPosition.WINDOW));
		viewButton.setDropDownComponent(simpleItemView);

		showTimeGraphButton.onClick.addListener(() -> {
			showTimeGraphButton.setVisible(false);
			hideTimeGraphButton.setVisible(true);
			showTimeGraph(true);
			timeGraphView.focus();
		});

		hideTimeGraphButton.onClick.addListener(() -> {
			hideTimeGraphButton.setVisible(false);
			showTimeGraphButton.setVisible(true);
			showTimeGraph(false);
			masterView.focus();
		});
		showDeletedButton.onClick.addListener(() -> {
			showDeletedButton.setVisible(false);
			hideDeletedButton.setVisible(true);
			entityModelBuilder.setShowDeletedRecords(true);
			masterView.focus();
		});

		hideDeletedButton.onClick.addListener(() -> {
			showDeletedButton.setVisible(true);
			hideDeletedButton.setVisible(false);
			entityModelBuilder.setShowDeletedRecords(false);
			masterView.focus();
		});
	}


	public void setDetailPosition(DetailPosition position) {
		detailPosition = position;
		masterView.setVisible(true);
		window.setToolbar(null);
		window.setContent(null);
		windowCloseButton.setVisible(false);
		editorPreviousButton.setVisible(false);
		editorNextButton.setVisible(false);
		switch (position) {
			case RIGHT -> {
				detailViewBottom.setVisible(false);
				detailViewBottom.setComponent(null);
				detailViewBottom.getPanel().setToolbar(null);
				detailViewRight.getPanel().setToolbar(formToolbar);
				detailViewRight.setComponent(detailComponent);
				detailViewRight.setVisible(true);
			}
			case BOTTOM -> {
				detailViewRight.setVisible(false);
				detailViewRight.setComponent(null);
				detailViewRight.getPanel().setToolbar(null);
				detailViewBottom.getPanel().setToolbar(formToolbar);
				detailViewBottom.setComponent(detailComponent);
				detailViewBottom.setVisible(true);
			}
			case CENTER -> {
				masterView.setVisible(false);
				detailViewBottom.setVisible(false);
				detailViewBottom.setComponent(null);
				detailViewBottom.getPanel().setToolbar(null);
				detailViewRight.getPanel().setToolbar(formToolbar);
				detailViewRight.setComponent(detailComponent);
				detailViewRight.setVisible(true);
				editorPreviousButton.setVisible(true);
				editorNextButton.setVisible(true);
			}
			case WINDOW -> {
				detailViewBottom.setVisible(false);
				detailViewBottom.setComponent(null);
				detailViewRight.setVisible(false);
				detailViewRight.setComponent(null);
				detailViewRight.getPanel().setToolbar(null);
				detailViewBottom.getPanel().setToolbar(null);
				window.setToolbar(formToolbar);
				window.setContent(detailComponent);
				windowCloseButton.setVisible(true);
				editorPreviousButton.setVisible(true);
				editorNextButton.setVisible(true);
			}
		}
	}

	public void showTimeGraph(boolean show) {
		timeGraphView.setVisible(show);
	}

	@Override
	public ApplicationInstanceData getApplicationInstanceData() {
		return applicationInstanceData;
	}

	public EntityModelBuilder<ENTITY> getEntityModelBuilder() {
		return entityModelBuilder;
	}

	public FormController<ENTITY> getFormController() {
		return formController;
	}

	public ResponsiveForm<ENTITY> getResponsiveForm() {
		return responsiveForm;
	}

	public View getTimeGraphView() {
		return timeGraphView;
	}

	public View getMasterView() {
		return masterView;
	}

	public View getDetailViewRight() {
		return detailViewRight;
	}

	public View getDetailViewBottom() {
		return detailViewBottom;
	}
}
