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
package org.teamapps.application.api.localization;

import org.teamapps.universaldb.index.translation.TranslatableText;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public interface ApplicationLocalizationProvider {

	String getLocalized(String key, Object... parameters);

	String getLocalized(String key, List<String> languagePriorityOrder, Object... parameters);

	default String getLocalized(String key, String language, Object... parameters) {
		return getLocalized(key, Collections.singletonList(language), parameters);
	}

	default String getLocalized(String key, Locale locale, Object... parameters) {
		return getLocalized(key, Collections.singletonList(locale.getLanguage()), parameters);
	}

	String getLocalized(TranslatableText translatableText);
}
