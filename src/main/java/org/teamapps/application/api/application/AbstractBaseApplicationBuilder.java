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
package org.teamapps.application.api.application;

import org.teamapps.event.Event;
import org.teamapps.icons.Icon;

public abstract class AbstractBaseApplicationBuilder implements BaseApplicationBuilder {

	public final Event<Void> onApplicationInstalled = new Event<>();
	public final Event<Void> onApplicationLoaded = new Event<>();
	public final Event<Void> onApplicationUnloaded = new Event<>();
	public final Event<Void> onApplicationUninstalled = new Event<>();

	private final String name;
	private final Icon icon;
	private final String titleKey;
	private final String descriptionKey;

	public AbstractBaseApplicationBuilder(String name, Icon icon, String titleKey, String descriptionKey) {
		this.name = name;
		this.icon = icon;
		this.titleKey = titleKey;
		this.descriptionKey = descriptionKey;
	}

	@Override
	public Icon getApplicationIcon() {
		return icon;
	}

	@Override
	public String getApplicationName() {
		return name;
	}

	@Override
	public String getApplicationTitleKey() {
		return titleKey;
	}

	@Override
	public String getApplicationDescriptionKey() {
		return descriptionKey;
	}

	@Override
	public Event<Void> getOnApplicationInstalled() {
		return onApplicationInstalled;
	}

	@Override
	public Event<Void> getOnApplicationLoaded() {
		return onApplicationLoaded;
	}

	@Override
	public Event<Void> getOnApplicationUnloaded() {
		return onApplicationUnloaded;
	}

	@Override
	public Event<Void> getOnApplicationUninstalled() {
		return onApplicationUninstalled;
	}

}
