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
import org.teamapps.application.api.theme.ApplicationIcons;
import org.teamapps.application.api.ui.FormMetaFields;
import org.teamapps.event.Event;
import org.teamapps.icons.Icon;
import org.teamapps.universaldb.index.ColumnIndex;
import org.teamapps.universaldb.index.TableIndex;
import org.teamapps.universaldb.index.numeric.IntegerIndex;
import org.teamapps.universaldb.pojo.AbstractUdbEntity;
import org.teamapps.universaldb.pojo.Entity;
import org.teamapps.universaldb.schema.Table;
import org.teamapps.ux.component.field.FieldEditingMode;
import org.teamapps.ux.component.field.TemplateField;
import org.teamapps.ux.component.field.datetime.InstantDateTimeField;
import org.teamapps.ux.component.form.ResponsiveFormLayout;
import org.teamapps.ux.component.form.ResponsiveFormSection;
import org.teamapps.ux.component.format.SizingPolicy;

public class FormMetaFieldsImpl implements FormMetaFields {

	private final ApplicationInstanceData applicationInstanceData;
	private final TemplateField<Integer> createdByField;
	private final TemplateField<Integer> modifiedByField;
	private final TemplateField<Integer> deletedByField;
	private final TemplateField<Integer> restoredByField;
	private final InstantDateTimeField creationDateField;
	private final InstantDateTimeField modificationDateField;
	private final InstantDateTimeField deletionDateField;
	private final InstantDateTimeField restoreDateField;

	public FormMetaFieldsImpl(ApplicationInstanceData applicationInstanceData) {
		this.applicationInstanceData = applicationInstanceData;

		createdByField = applicationInstanceData.getComponentFactory().createUserTemplateField();
		modifiedByField = applicationInstanceData.getComponentFactory().createUserTemplateField();
		deletedByField = applicationInstanceData.getComponentFactory().createUserTemplateField();
		restoredByField = applicationInstanceData.getComponentFactory().createUserTemplateField();

		creationDateField = new InstantDateTimeField();
		modificationDateField = new InstantDateTimeField();
		deletionDateField = new InstantDateTimeField();
		restoreDateField = new InstantDateTimeField();

		creationDateField.setEditingMode(FieldEditingMode.READONLY);
		modificationDateField.setEditingMode(FieldEditingMode.READONLY);
		deletionDateField.setEditingMode(FieldEditingMode.READONLY);
		restoreDateField.setEditingMode(FieldEditingMode.READONLY);
	}

	@Override
	public ResponsiveFormSection addMetaFields(ResponsiveFormLayout formLayout, boolean withIcons, Event<? extends Entity<?>> onEntityChange) {
		onEntityChange.addListener(this::updateEntity);
		return addMetaFields(formLayout, withIcons);
	}

	@Override
	public ResponsiveFormSection addMetaFields(ResponsiveFormLayout formLayout, boolean withIcons) {
		Icon dateIcon = withIcons ? ApplicationIcons.CALENDAR_CLOCK : null;
		Icon userIcon = withIcons ? ApplicationIcons.USER : null;
		ResponsiveFormSection formSection = formLayout.addSection(ApplicationIcons.WINDOW_SIDEBAR, applicationInstanceData.getLocalized(Dictionary.META_DATA));
		formLayout.addLabelAndField(userIcon, applicationInstanceData.getLocalized(Dictionary.CREATION), creationDateField).field.getColumnDefinition().setWidthPolicy(SizingPolicy.AUTO);
		formLayout.addLabelAndField(null, null, createdByField, false).field.getColumnDefinition().setWidthPolicy(SizingPolicy.FRACTION);
		formLayout.addLabelAndField(dateIcon, applicationInstanceData.getLocalized(Dictionary.MODIFICATION), modificationDateField);
		formLayout.addLabelAndField(null, null, modifiedByField, false);
		formLayout.addLabelAndField(dateIcon, applicationInstanceData.getLocalized(Dictionary.DELETED), deletionDateField);
		formLayout.addLabelAndField(null, null, deletedByField, false);
		formLayout.addLabelAndField(dateIcon, applicationInstanceData.getLocalized(Dictionary.RESTORED), restoreDateField);
		formLayout.addLabelAndField(null, null, restoredByField, false);
		return formSection;
	}

	@Override
	public void updateEntity(Entity<?> entity) {
		if (entity instanceof AbstractUdbEntity) {
			AbstractUdbEntity<?> udbEntity = (AbstractUdbEntity<?>) entity;
			TableIndex tableIndex = udbEntity.getTableIndex();
			ColumnIndex<?, ?> createdByIndex = tableIndex.getColumnIndex(Table.FIELD_CREATED_BY);
			ColumnIndex<?, ?> modifiedByIndex = tableIndex.getColumnIndex(Table.FIELD_MODIFIED_BY);
			ColumnIndex<?, ?> deletedByIndex = tableIndex.getColumnIndex(Table.FIELD_DELETED_BY);
			ColumnIndex<?, ?> restoredByIndex = tableIndex.getColumnIndex(Table.FIELD_RESTORED_BY);
			ColumnIndex<?, ?> creationDateIndex = tableIndex.getColumnIndex(Table.FIELD_CREATION_DATE);
			ColumnIndex<?, ?> modificationDateIndex = tableIndex.getColumnIndex(Table.FIELD_MODIFICATION_DATE);
			ColumnIndex<?, ?> deletionDateIndex = tableIndex.getColumnIndex(Table.FIELD_DELETION_DATE);
			ColumnIndex<?, ?> restoreDateIndex = tableIndex.getColumnIndex(Table.FIELD_RESTORE_DATE);

			createdByField.setValue(createdByIndex != null ? udbEntity.getIntValue((IntegerIndex) createdByIndex) : 0);
			modifiedByField.setValue(modifiedByIndex != null ? udbEntity.getIntValue((IntegerIndex) modifiedByIndex) : 0);
			deletedByField.setValue(deletedByIndex != null ? udbEntity.getIntValue((IntegerIndex) deletedByIndex) : 0);
			restoredByField.setValue(restoredByIndex != null ? udbEntity.getIntValue((IntegerIndex) restoredByIndex) : 0);

			creationDateField.setValue(creationDateIndex != null ? udbEntity.getTimestampValue((IntegerIndex) creationDateIndex) : null);
			modificationDateField.setValue(modificationDateIndex != null ? udbEntity.getTimestampValue((IntegerIndex) modificationDateIndex) : null);
			deletionDateField.setValue(deletionDateIndex != null ? udbEntity.getTimestampValue((IntegerIndex) deletionDateIndex) : null);
			restoreDateField.setValue(restoreDateIndex != null ? udbEntity.getTimestampValue((IntegerIndex) restoreDateIndex) : null);

			boolean showModification = modificationDateField.getValue() != null && !modificationDateField.getValue().equals(creationDateField.getValue());
			createdByField.setVisible(createdByField.getValue() != 0);
			modifiedByField.setVisible(showModification);
			deletedByField.setVisible(deletedByField.getValue() != 0);
			restoredByField.setVisible(restoredByField.getValue() != 0);
			creationDateField.setVisible(creationDateField.getValue() != null);
			modificationDateField.setVisible(showModification);
			deletionDateField.setVisible(deletedByField.getValue() != 0);
			restoreDateField.setVisible(restoredByField.getValue() != 0);
		}
	}
}
