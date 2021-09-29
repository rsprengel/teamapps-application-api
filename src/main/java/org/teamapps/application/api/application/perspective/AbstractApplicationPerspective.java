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
package org.teamapps.application.api.application.perspective;

import org.teamapps.application.api.application.ApplicationInstanceData;
import org.teamapps.application.api.application.ApplicationInstanceDataMethods;
import org.teamapps.application.api.config.ApplicationConfig;
import org.teamapps.application.api.desktop.ApplicationDesktop;
import org.teamapps.application.api.privilege.*;
import org.teamapps.application.api.user.SessionUser;
import org.teamapps.databinding.MutableValue;
import org.teamapps.event.Event;
import org.teamapps.icons.Icon;
import org.teamapps.model.controlcenter.OrganizationUnitView;
import org.teamapps.universaldb.index.translation.TranslatableText;
import org.teamapps.ux.application.perspective.Perspective;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.progress.MultiProgressDisplay;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractApplicationPerspective implements ApplicationPerspective, ApplicationInstanceDataMethods {

	public final Event<Void> onPerspectiveInitialized = new Event<>();
	public final Event<Void> onPerspectiveRefreshRequested = new Event<>();
	private final ApplicationInstanceData applicationInstanceData;
	private final MutableValue<String> perspectiveInfoBadgeValue;

	private Component perspectiveMenuPanel;
	private Component perspectiveToolbarMenuPanel;
	private Perspective perspective;

	public AbstractApplicationPerspective(ApplicationInstanceData applicationInstanceData, MutableValue<String> perspectiveInfoBadgeValue) {
		this.applicationInstanceData = applicationInstanceData;
		this.perspectiveInfoBadgeValue = perspectiveInfoBadgeValue;
		perspective = Perspective.createPerspective();
	}

	@Override
	public Event<Void> getOnPerspectiveInitialized() {
		return onPerspectiveInitialized;
	}

	@Override
	public Event<Void> getOnPerspectiveRefreshRequested() {
		return onPerspectiveRefreshRequested;
	}

	public void setPerspectiveMenuPanel(Component perspectiveMenuPanel, Component perspectiveToolbarMenuPanel) {
		this.perspectiveMenuPanel = perspectiveMenuPanel;
		this.perspectiveToolbarMenuPanel = perspectiveToolbarMenuPanel;
	}

	public void setPerspective(Perspective perspective) {
		this.perspective = perspective;
	}

	@Override
	public Component getPerspectiveMenuPanel() {
		return perspectiveMenuPanel;
	}

	@Override
	public Component getPerspectiveToolbarMenuPanel() {
		return perspectiveToolbarMenuPanel;
	}

	@Override
	public Perspective getPerspective() {
		return perspective;
	}

	public ApplicationInstanceData getApplicationInstanceData() {
		return applicationInstanceData;
	}

	public MutableValue<String> getPerspectiveInfoBadgeValue() {
		return perspectiveInfoBadgeValue;
	}

	public String getLocalized(String key) {
		return getLocalized(key, (Object) null);
	}



}
