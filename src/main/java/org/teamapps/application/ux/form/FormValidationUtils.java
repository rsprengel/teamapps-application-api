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
import org.teamapps.ux.component.field.validator.FieldValidator;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class FormValidationUtils {

	public static Pattern PHONE_NUMBER_PATTERN = Pattern.compile(
			"^[+0-9.\\-\\s()]+$"
	);

	public static final Pattern E_MAIL_ADDRESS_PATTERN = Pattern.compile(
			"^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
	);

	public static FieldValidator<?> createNotNullValidator(ApplicationInstanceData applicationInstanceData) {
		return FieldValidator.fromPredicate(Objects::nonNull, applicationInstanceData.getLocalized(Dictionary.PLEASE_ENTER_AVALUE));
	}

	public static FieldValidator<String> createNotBlankValidator(ApplicationInstanceData applicationInstanceData) {
		return FieldValidator.fromPredicate(s -> isNotNullOrBlank(s) && s.length() > 0, applicationInstanceData.getLocalized(Dictionary.PLEASE_ENTER_AVALUE));
	}

	public static FieldValidator<?> createNotEmptyListValidator(ApplicationInstanceData applicationInstanceData) {
		return FieldValidator.fromPredicate(list -> list instanceof List<?> && !((List<?>) list).isEmpty(), applicationInstanceData.getLocalized(Dictionary.PLEASE_ENTER_AVALUE));
	}

	public static FieldValidator<String> createEmailValidator(ApplicationInstanceData applicationInstanceData) {
		return FieldValidator.fromPredicate(s -> isNotNullOrBlank(s) && E_MAIL_ADDRESS_PATTERN.matcher(s).matches(), applicationInstanceData.getLocalized(Dictionary.NOT_AVALID_EMAIL_ADDRESS));
	}

	public static FieldValidator<String> createEmailOrEmptyValidator(ApplicationInstanceData applicationInstanceData) {
		return FieldValidator.fromPredicate(s -> isEmpty(s) || E_MAIL_ADDRESS_PATTERN.matcher(s).matches(), applicationInstanceData.getLocalized(Dictionary.NOT_AVALID_EMAIL_ADDRESS));
	}

	public static FieldValidator<String> createPhoneNumberValidator(ApplicationInstanceData applicationInstanceData) {
		return FieldValidator.fromPredicate(s -> isNotNullOrBlank(s) &&  PHONE_NUMBER_PATTERN.matcher(s).matches(), applicationInstanceData.getLocalized(Dictionary.NOT_AVALID_PHONE_NUMBER));
	}

	public static FieldValidator<String> createPhoneNumberOrEmptyValidator(ApplicationInstanceData applicationInstanceData) {
		return FieldValidator.fromPredicate(s -> isEmpty(s) || PHONE_NUMBER_PATTERN.matcher(s).matches(), applicationInstanceData.getLocalized(Dictionary.NOT_AVALID_PHONE_NUMBER));
	}

	public static FieldValidator<String> createMinCharactersValidator(int minCharacters, ApplicationInstanceData applicationInstanceData) {
		return FieldValidator.fromPredicate(s -> isNotNullOrBlank(s) && s.length() >= minCharacters, applicationInstanceData.getLocalized(Dictionary.SENTENCE_THE_FIELD_MUST_CONTAIN_AT_LEAST0_CH__, minCharacters));
	}

	public static FieldValidator<String> createMinCharactersOrEmptyValidator(int minCharacters, ApplicationInstanceData applicationInstanceData) {
		return FieldValidator.fromPredicate(s -> isEmpty(s) || s.length() >= minCharacters, applicationInstanceData.getLocalized(Dictionary.SENTENCE_THE_FIELD_MUST_CONTAIN_AT_LEAST0_CH__, minCharacters));
	}

	public static FieldValidator<String> createMinCharactersValidator(int minCharacters, String fieldCaption, ApplicationInstanceData applicationInstanceData) {
		return FieldValidator.fromPredicate(s -> isNotNullOrBlank(s) && s.length() >= minCharacters, applicationInstanceData.getLocalized(Dictionary.SENTENCE_THE_FIELD0_MUST_CONTAIN_AT_LEAST1_C__, fieldCaption, minCharacters));
	}

	public static FieldValidator<String> createMaxCharactersValidator(int maxCharacters, ApplicationInstanceData applicationInstanceData) {
		return FieldValidator.fromPredicate(s -> isNotNullOrBlank(s) && s.length() < maxCharacters, applicationInstanceData.getLocalized(Dictionary.SENTENCE_THE_FIELD_MUST_CONTAIN_LESS_THAN0_C__, maxCharacters));
	}


	private static boolean isNotNullOrBlank(String s) {
		return s != null && !s.isBlank();
	}

	private static boolean isEmpty(String s) {
		return s == null || s.isEmpty();
	}

}
