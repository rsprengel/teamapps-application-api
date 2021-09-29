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
package org.teamapps.application.server;

import org.teamapps.application.api.application.ApplicationInstanceData;
import org.teamapps.application.api.theme.ApplicationIcons;
import org.teamapps.application.api.ui.FormMetaFields;
import org.teamapps.application.api.ui.TranslationKeyField;
import org.teamapps.application.api.ui.UiComponentFactory;
import org.teamapps.application.ux.UiUtils;
import org.teamapps.application.ux.form.FormMetaFieldsImpl;
import org.teamapps.application.ux.localize.TranslatableField;
import org.teamapps.application.ux.org.OrganizationViewUtils;
import org.teamapps.data.extract.PropertyProvider;
import org.teamapps.event.Event;
import org.teamapps.model.controlcenter.OrganizationUnitTypeView;
import org.teamapps.model.controlcenter.OrganizationUnitView;
import org.teamapps.ux.component.field.AbstractField;
import org.teamapps.ux.component.field.FieldEditingMode;
import org.teamapps.ux.component.field.TemplateField;
import org.teamapps.ux.component.field.TextField;
import org.teamapps.ux.component.field.combobox.ComboBox;
import org.teamapps.ux.component.field.combobox.TagComboBox;
import org.teamapps.ux.component.linkbutton.LinkButton;
import org.teamapps.ux.component.template.BaseTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class DevUiComponentFactory implements UiComponentFactory {

	private final ApplicationInstanceData applicationInstanceData;

	public DevUiComponentFactory(ApplicationInstanceData applicationInstanceData) {
		this.applicationInstanceData = applicationInstanceData;
	}

	@Override
	public ComboBox<OrganizationUnitView> createOrganizationUnitComboBox(Supplier<Collection<OrganizationUnitView>> allowedUnitsSupplier) {
		return OrganizationViewUtils.createOrganizationComboBox(BaseTemplate.LIST_ITEM_SMALL_ICON_SINGLE_LINE, allowedUnitsSupplier, applicationInstanceData);
	}

	@Override
	public ComboBox<OrganizationUnitView> createOrganizationUnitComboBox(Set<OrganizationUnitView> allowedUnits) {
		return OrganizationViewUtils.createOrganizationComboBox(BaseTemplate.LIST_ITEM_SMALL_ICON_SINGLE_LINE, allowedUnits, applicationInstanceData);
	}

	@Override
	public TagComboBox<OrganizationUnitTypeView> createOrganizationUnitTypeTagComboBox() {
		return OrganizationViewUtils.createOrganizationUnitTypeTagComboBox(150, applicationInstanceData);
	}

	@Override
	public TemplateField<OrganizationUnitView> createOrganizationUnitTemplateField() {
		return UiUtils.createTemplateField(BaseTemplate.LIST_ITEM_MEDIUM_ICON_SINGLE_LINE, OrganizationViewUtils.creatOrganizationUnitViewPropertyProvider(applicationInstanceData));
	}

	@Override
	public TemplateField<Integer> createUserTemplateField() {
		return UiUtils.createTemplateField(BaseTemplate.LIST_ITEM_MEDIUM_ICON_SINGLE_LINE, createUserIdPropertyProvider());
	}

	@Override
	public TranslatableField createTranslatableField() {
		return new TranslatableField(applicationInstanceData);
	}

	@Override
	public TranslationKeyField createTranslationKeyField(String linkButtonCaption, boolean allowMultiLine, boolean selectionFieldWithKey) {
		final TextField selectionField = new TextField();
		final TextField keyField = new TextField();
		final LinkButton linkButton = new LinkButton(linkButtonCaption);
		final Event<String> onValueChanged = new Event<>();
		keyField.setEditingMode(FieldEditingMode.READONLY);
		return new TranslationKeyField() {
			@Override
			public Event<String> getOnValueChanged() {
				return onValueChanged;
			}

			@Override
			public AbstractField<String> getSelectionField() {
				return selectionField;
			}

			@Override
			public AbstractField<String> getKeyDisplayField() {
				return keyField;
			}

			@Override
			public LinkButton getKeyLinkButton() {
				return linkButton;
			}

			@Override
			public void setKey(String key) {
				keyField.setValue(key);
				selectionField.setValue(key);
			}

			@Override
			public String getKey() {
				return selectionField.getValue();
			}
		};
	}

	@Override
	public FormMetaFields createFormMetaFields() {
		return new FormMetaFieldsImpl(applicationInstanceData);
	}

	@Override
	public String createUserAvatarLink(int userId, boolean large) {
		return null;
	}

	public static PropertyProvider<Integer> createUserIdPropertyProvider() {
		return (userId, propertyNames) -> {
			Map<String, Object> map = new HashMap<>();
			map.put(BaseTemplate.PROPERTY_ICON, ApplicationIcons.USER);
			map.put(BaseTemplate.PROPERTY_CAPTION, "User with id:" + userId);
			return map;
		};
	}
}
