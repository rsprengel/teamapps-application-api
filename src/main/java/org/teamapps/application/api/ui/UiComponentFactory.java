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
package org.teamapps.application.api.ui;

import org.teamapps.application.ux.localize.TranslatableField;
import org.teamapps.model.controlcenter.OrganizationUnitTypeView;
import org.teamapps.model.controlcenter.OrganizationUnitView;
import org.teamapps.ux.component.field.TemplateField;
import org.teamapps.ux.component.field.combobox.ComboBox;
import org.teamapps.ux.component.field.combobox.TagComboBox;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;

public interface UiComponentFactory {

	ComboBox<OrganizationUnitView> createOrganizationUnitComboBox(Supplier<Collection<OrganizationUnitView>> allowedUnitsSupplier);
	ComboBox<OrganizationUnitView> createOrganizationUnitComboBox(Set<OrganizationUnitView> allowedUnits);

	TagComboBox<OrganizationUnitTypeView> createOrganizationUnitTypeTagComboBox();

	TemplateField<OrganizationUnitView> createOrganizationUnitTemplateField();

	TemplateField<Integer> createUserTemplateField();

	TranslatableField createTranslatableField();

	default TranslationKeyField createTranslationKeyField(String linkButtonCaption) {
		return createTranslationKeyField(linkButtonCaption, false, false);
	}

	TranslationKeyField createTranslationKeyField(String linkButtonCaption, boolean allowMultiLine, boolean selectionFieldWithKey);

	FormMetaFields createFormMetaFields();

	String createUserAvatarLink(int userId, boolean large);

}
