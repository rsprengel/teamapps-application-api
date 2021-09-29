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
import org.teamapps.ux.component.field.AbstractField;
import org.teamapps.ux.component.field.Fields;
import org.teamapps.ux.component.field.validator.FieldValidator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class FormValidator {

	private final ApplicationInstanceData applicationInstanceData;
	private Set<AbstractField<?>> fields = new HashSet<>();

	public FormValidator(ApplicationInstanceData applicationInstanceData) {
		this.applicationInstanceData = applicationInstanceData;
	}

	public <VALUE> void addValidator(AbstractField<VALUE> field, Function<VALUE, String> errorMessageOrNullFunction) {
		field.addValidator(FieldValidator.fromErrorMessageFunction(errorMessageOrNullFunction));
		fields.add(field);
	}

	public void addNotNull(AbstractField field) {
		field.addValidator(FormValidationUtils.createNotNullValidator(applicationInstanceData));
		fields.add(field);
	}

	public void addNotEmptyList(AbstractField field) {
		field.addValidator(FormValidationUtils.createNotEmptyListValidator(applicationInstanceData));
		fields.add(field);
	}

	public void addNotBlank(AbstractField<String> field) {
		field.addValidator(FormValidationUtils.createNotBlankValidator(applicationInstanceData));
		fields.add(field);
	}

	public void addMinCharacters(AbstractField<String> field, int chars) {
		field.addValidator(FormValidationUtils.createMinCharactersValidator(chars, applicationInstanceData));
		fields.add(field);
	}

	public void addMinCharactersOrEmpty(AbstractField<String> field, int chars) {
		field.addValidator(FormValidationUtils.createMinCharactersOrEmptyValidator(chars, applicationInstanceData));
		fields.add(field);
	}

	public void addMaxCharacters(AbstractField<String> field, int chars) {
		field.addValidator(FormValidationUtils.createMaxCharactersValidator(chars, applicationInstanceData));
		fields.add(field);
	}

	public void addEmail(AbstractField<String> field) {
		field.addValidator(FormValidationUtils.createEmailValidator(applicationInstanceData));
		fields.add(field);
	}

	public void addEmailOrEmpty(AbstractField<String> field) {
		field.addValidator(FormValidationUtils.createEmailOrEmptyValidator(applicationInstanceData));
		fields.add(field);
	}

	public void addPhoneNumber(AbstractField<String> field) {
		field.addValidator(FormValidationUtils.createPhoneNumberValidator(applicationInstanceData));
		fields.add(field);
	}

	public void addPhoneOrEmptyNumber(AbstractField<String> field) {
		field.addValidator(FormValidationUtils.createPhoneNumberOrEmptyValidator(applicationInstanceData));
		fields.add(field);
	}

	public void addFieldWithValidator(AbstractField<?> field) {
		fields.add(field);
	}

	public boolean validate() {
		return Fields.validateAll(new ArrayList<>(fields));
	}

	public void clearMessages() {
		fields.forEach(AbstractField::clearValidatorMessages);
	}

	public Set<AbstractField<?>> getFields() {
		return fields;
	}
}
