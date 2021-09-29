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

import org.teamapps.application.api.application.BaseApplicationBuilder;
import org.teamapps.application.api.localization.ApplicationLocalizationProvider;
import org.teamapps.application.api.localization.LocalizationData;
import org.teamapps.universaldb.index.translation.TranslatableText;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevLocalizationProvider implements ApplicationLocalizationProvider {

	private final BaseApplicationBuilder applicationBuilder;
	private final Map<String, Map<String, String>> localizationMap;
	private final Map<String, Map<String, String>> dictionaryMap;
	private String language = "en";

	public DevLocalizationProvider(BaseApplicationBuilder applicationBuilder) {
		this.applicationBuilder = applicationBuilder;
		this.localizationMap = applicationBuilder.getLocalizationData() != null ? applicationBuilder.getLocalizationData().createLocalizationMapByLanguage() : new HashMap<>();
		this.dictionaryMap = LocalizationData.createDictionaryData(getClass().getClassLoader()).createLocalizationMapByLanguage();
	}

	public void setLanguage(String language) {
		if (language != null && !language.isEmpty()) {
			this.language = language;
		}
	}

	@Override
	public String getLocalized(String key, Object... parameters) {
		String localizationValue = getLocalized(key, language);
		if (parameters != null && parameters.length > 0) {
			try {
				return MessageFormat.format(localizationValue, parameters);
			} catch (Exception e) {
				e.printStackTrace();
				return localizationValue;
			}
		} else {
			return localizationValue;
		}
	}

	@Override
	public String getLocalized(String key, List<String> languagePriorityOrder, Object... parameters) {
		String localizationValue = getLocalized(key, languagePriorityOrder.get(0));
		if (parameters != null && parameters.length > 0) {
			try {
				return MessageFormat.format(localizationValue, parameters);
			} catch (Exception e) {
				e.printStackTrace();
				return localizationValue;
			}
		} else {
			return localizationValue;
		}
	}

	private String getLocalized(String key, String language) {
		Map<String, String> translationMap = localizationMap.get(language);
		if (translationMap != null && translationMap.containsKey(key)) {
			return translationMap.get(key);
		}
		translationMap = dictionaryMap.get(language);
		if (translationMap != null && translationMap.containsKey(key)) {
			return translationMap.get(key);
		}
		for (Map<String, String> map : localizationMap.values()) {
			if (map.containsKey(key)) {
				return map.get(key);
			}
		}
		for (Map<String, String> map : dictionaryMap.values()) {
			if (map.containsKey(key)) {
				return map.get(key);
			}
		}
		return key;
	}

	@Override
	public String getLocalized(TranslatableText translatableText) {
		return translatableText != null ? translatableText.getText() : null;
	}
}
