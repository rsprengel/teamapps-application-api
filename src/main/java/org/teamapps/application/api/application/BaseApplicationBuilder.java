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

import org.teamapps.application.api.config.ApplicationConfig;
import org.teamapps.application.api.localization.LocalizationData;
import org.teamapps.application.api.privilege.ApplicationPrivilegeProvider;
import org.teamapps.application.api.privilege.ApplicationRole;
import org.teamapps.application.api.privilege.PrivilegeGroup;
import org.teamapps.application.api.versioning.ApplicationVersion;
import org.teamapps.event.Event;
import org.teamapps.icons.Icon;
import org.teamapps.universaldb.schema.SchemaInfoProvider;
import org.teamapps.ux.application.ResponsiveApplication;

import java.util.List;

public interface BaseApplicationBuilder {

	ApplicationVersion getApplicationVersion();

	default String getReleaseNotes() {
		return null;
	}

	Icon getApplicationIcon();

	String getApplicationName();

	String getApplicationTitleKey();

	String getApplicationDescriptionKey();

	default boolean isDarkTheme() {
		return false;
	}

	List<ApplicationRole> getApplicationRoles();

	List<PrivilegeGroup> getPrivilegeGroups();

	LocalizationData getLocalizationData();

	SchemaInfoProvider getDatabaseModel();

	default boolean useToolbarApplicationMenu() {
		return false;
	}

	ApplicationConfig getApplicationConfig();

	default String getApplicationConfigXml(ClassLoader classLoader) {
		return getApplicationConfig() != null ? getApplicationConfig().getConfigXml(classLoader) : null;
	}

	default void updateConfig(String xml, ClassLoader classLoader) throws Exception {
		if (getApplicationConfig() != null) {
			getApplicationConfig().updateConfig(xml, classLoader);
		}
	}

	Event<Void> getOnApplicationInstalled();

	Event<Void> getOnApplicationLoaded();

	Event<Void> getOnApplicationUnloaded();

	Event<Void> getOnApplicationUninstalled();

	boolean isApplicationAccessible(ApplicationPrivilegeProvider privilegeProvider);

	void build(ResponsiveApplication application, ApplicationInstanceData applicationInstanceData);

}
