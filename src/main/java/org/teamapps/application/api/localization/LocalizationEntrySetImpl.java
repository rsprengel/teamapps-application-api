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

import java.util.ArrayList;
import java.util.List;

public class LocalizationEntrySetImpl implements LocalizationEntrySet {

	private final String language;
	private final boolean machineTranslation;
	private List<LocalizationEntry> entries = new ArrayList();

	public LocalizationEntrySetImpl(String language, boolean machineTranslation) {
		this.language = language;
		this.machineTranslation = machineTranslation;
	}

	public void addEntry(String key, String value) {
		entries.add(new LocalizationEntryImpl(key, value));
	}

	@Override
	public String getLanguage() {
		return language;
	}

	@Override
	public List<LocalizationEntry> getEntries() {
		return entries;
	}

	@Override
	public boolean isMachineTranslation() {
		return machineTranslation;
	}
}
