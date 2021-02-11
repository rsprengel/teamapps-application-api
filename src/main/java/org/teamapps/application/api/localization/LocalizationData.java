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

import org.teamapps.ux.i18n.TeamAppsResourceBundleControl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface LocalizationData {

	String DICTIONARY_PREFIX = "org.teamapps.dictionary.";


	List<LocalizationEntrySet> getLocalizationEntrySets();

	default Set<String> getAllKeys() {
		return getLocalizationEntrySets().stream()
				.flatMap(entrySet -> entrySet.getEntries().stream())
				.map(LocalizationEntry::getKey)
				.collect(Collectors.toSet());
	}

	default Set<String> getLanguageSet() {
		return getLocalizationEntrySets().stream()
				.map(LocalizationEntrySet::getLanguage)
				.collect(Collectors.toSet());
	}

	default boolean containsAnyLanguage(List<String> languages) {
		Set<String> languageSet = getLanguageSet();
		for (String language : languages) {
			if (languageSet.contains(language)) {
				return true;
			}
		}
		return false;
	}

	default Map<String, Map<String, String>> createLocalizationMapByKey() {
		Map<String, Map<String, String>> localizationMap = new HashMap<>();
		for (LocalizationEntrySet entrySet : getLocalizationEntrySets()) {
			String language = entrySet.getLanguage();
			for (LocalizationEntry entry : entrySet.getEntries()) {
				String key = entry.getKey();
				String value = entry.getValue();
				localizationMap.computeIfAbsent(key, k -> new HashMap<>()).put(language, value);
			}
		}
		return localizationMap;
	}

	default Map<String, Map<String, String>> createLocalizationMapByLanguage() {
		Map<String, Map<String, String>> localizationMap = new HashMap<>();
		for (LocalizationEntrySet entrySet : getLocalizationEntrySets()) {
			Map<String, String> map = localizationMap.computeIfAbsent(entrySet.getLanguage(), s -> new HashMap<>());
			entrySet.getEntries().forEach(entry -> map.put(entry.getKey(), entry.getValue()));
		}
		return localizationMap;
	}

	static LocalizationData createFromResourceBundle(ResourceBundle resourceBundle, Locale language) {
		return create(locale -> resourceBundle, language);
	}

	static LocalizationData createFromPropertyFiles(String baseName, ClassLoader classLoader, Locale... translations) {
		return createFromPropertyFiles(baseName, "properties", classLoader, translations);
	}

	static LocalizationData createFromPropertyFiles(String baseName, String resourceFileSuffix, ClassLoader classLoader, Locale... translations) {
		Function<Locale, ResourceBundle> resourceBundleByLocaleFunction = locale -> ResourceBundle.getBundle(baseName, locale, classLoader, new TeamAppsResourceBundleControl(resourceFileSuffix, Locale.ENGLISH));
		return create(resourceBundleByLocaleFunction, translations);
	}

	static LocalizationData create(Function<Locale, ResourceBundle> resourceBundleByLocaleFunction, Locale... translations) {
		List<LocalizationEntrySet> entrySets = new ArrayList<>();
		for (Locale translation : translations) {
			ResourceBundle resourceBundle = resourceBundleByLocaleFunction.apply(translation);
			entrySets.add(createEntrySet(resourceBundle, translation));
		}
		return () -> entrySets;
	}

	static LocalizationData createDictionaryData(ClassLoader classLoader) {
		return createFromPropertyFiles("org.teamapps.application.api.localization.dictionary", classLoader, Locale.ENGLISH);
	}

	static LocalizationEntrySet createEntrySet(ResourceBundle bundle, Locale translation) {
		LocationEntrySetImpl entrySet = new LocationEntrySetImpl(translation.getLanguage());
		if (bundle instanceof PropertyResourceBundle) {
			PropertyResourceBundle propertyResourceBundle = (PropertyResourceBundle) bundle;
			for (String key : bundle.keySet()) {
				Object result = propertyResourceBundle.handleGetObject(key);
				if (result != null) {
					String value = bundle.getString(key);
					if (checkNotEmpty(translation.getLanguage(), key, value)) {
						entrySet.addEntry(key, value);
					}
				}
			}
		} else {
			for (String key : bundle.keySet()) {
				String value = bundle.getString(key);
				if (checkNotEmpty(translation.getLanguage(), key, value)) {
					entrySet.addEntry(key, value);
				}
			}
		}
		return entrySet;
	}

	static boolean checkNotEmpty(String... values) {
		for (String value : values) {
			boolean result = checkNotEmpty(value);
			if (!result) {
				return false;
			}
		}
		return true;
	}

	static boolean checkNotEmpty(String value) {
		if (value == null || value.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
}
