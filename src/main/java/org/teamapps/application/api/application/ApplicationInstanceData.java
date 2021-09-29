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
import org.teamapps.application.api.desktop.ApplicationDesktop;
import org.teamapps.application.api.localization.ApplicationLocalizationProvider;
import org.teamapps.application.api.privilege.ApplicationPrivilegeProvider;
import org.teamapps.application.api.ui.UiComponentFactory;
import org.teamapps.application.api.user.SessionUser;
import org.teamapps.icons.Icon;
import org.teamapps.model.controlcenter.OrganizationFieldView;
import org.teamapps.reporting.convert.DocumentConverter;
import org.teamapps.ux.application.perspective.Perspective;
import org.teamapps.ux.component.progress.MultiProgressDisplay;
import org.teamapps.ux.session.SessionContext;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ApplicationInstanceData extends ApplicationPrivilegeProvider, ApplicationLocalizationProvider {

	SessionUser getUser();

	OrganizationFieldView getOrganizationField();

	int getManagedApplicationId();

	DocumentConverter getDocumentConverter();

	MultiProgressDisplay getMultiProgressDisplay();

	default <RESULT> void runTaskAsync(Icon icon, String title, Supplier<RESULT> task, Consumer<RESULT> uiResultTask) {
		SessionContext context = SessionContext.current();
		MultiProgressDisplay multiProgressDisplay = getMultiProgressDisplay();
		multiProgressDisplay.addTask(icon, title, progressMonitor -> {
			RESULT result = task.get();
			progressMonitor.markCompleted();
			if (uiResultTask != null) {
				context.runWithContext(() -> uiResultTask.accept(result));
			}
		});
	}

	void showPerspective(Perspective perspective);

	ApplicationDesktop createApplicationDesktop();

	UiComponentFactory getComponentFactory();

	boolean isDarkTheme();

	ApplicationConfig<?> getApplicationConfig();

	void writeActivityLog(String title, String data);

	void writeExceptionLog(String title, Throwable throwable);
}
