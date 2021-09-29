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

import org.teamapps.application.api.application.ApplicationInstanceData;
import org.teamapps.application.api.application.BaseApplicationBuilder;
import org.teamapps.application.api.config.ApplicationConfig;
import org.teamapps.application.api.desktop.ApplicationDesktop;
import org.teamapps.application.api.localization.ApplicationLocalizationProvider;
import org.teamapps.application.api.privilege.*;
import org.teamapps.application.api.ui.UiComponentFactory;
import org.teamapps.application.api.user.SessionUser;
import org.teamapps.model.controlcenter.OrganizationFieldView;
import org.teamapps.model.controlcenter.OrganizationUnitView;
import org.teamapps.reporting.convert.DocumentConverter;
import org.teamapps.universaldb.index.translation.TranslatableText;
import org.teamapps.ux.application.ResponsiveApplication;
import org.teamapps.ux.application.perspective.Perspective;
import org.teamapps.ux.component.progress.MultiProgressDisplay;
import org.teamapps.ux.session.SessionContext;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class DevApplicationData implements ApplicationInstanceData {

	private final ApplicationRole applicationRole;
	private final SessionContext context;
	private final Locale locale;
	private final ApplicationLocalizationProvider localizationProvider;
	private final BaseApplicationBuilder applicationBuilder;
	private final List<OrganizationUnitView> organizationUnitViews;
	private final DocumentConverter documentConverter;
	private final ResponsiveApplication responsiveApplication;
	private final SessionUser sessionUser;
	private ApplicationPrivilegeProvider applicationPrivilegeProvider;
	private UiComponentFactory uiComponentFactory;

	public DevApplicationData(ApplicationRole applicationRole, SessionContext context, Locale locale, ApplicationLocalizationProvider localizationProvider, BaseApplicationBuilder applicationBuilder, List<OrganizationUnitView> organizationUnitViews, DocumentConverter documentConverter, ResponsiveApplication responsiveApplication) {
		this.applicationRole = applicationRole;
		this.context = context;
		this.locale = locale;
		this.localizationProvider = localizationProvider;
		this.applicationBuilder = applicationBuilder;
		this.organizationUnitViews = organizationUnitViews;
		this.documentConverter = documentConverter;
		this.responsiveApplication = responsiveApplication;
		sessionUser = new DevSessionUser(context, locale);
		applicationPrivilegeProvider = applicationRole != null ? new DevApplicationRolePrivilegeProvider(applicationRole, new HashSet<>(organizationUnitViews)) : null;
		this.uiComponentFactory = new DevUiComponentFactory(this);
	}

	@Override
	public SessionUser getUser() {
		return sessionUser;
	}

	@Override
	public OrganizationFieldView getOrganizationField() {
		return null;
	}

	@Override
	public int getManagedApplicationId() {
		return 0;
	}

	@Override
	public DocumentConverter getDocumentConverter() {
		return documentConverter;
	}

	@Override
	public MultiProgressDisplay getMultiProgressDisplay() {
		return responsiveApplication.getMultiProgressDisplay();
	}

	@Override
	public void showPerspective(Perspective perspective) {
		responsiveApplication.showPerspective(perspective);
	}

	@Override
	public ApplicationDesktop createApplicationDesktop() {
		return null;
	}

	@Override
	public UiComponentFactory getComponentFactory() {
		return uiComponentFactory;
	}

	@Override
	public boolean isDarkTheme() {
		return false;
	}

	@Override
	public ApplicationConfig<?> getApplicationConfig() {
		return applicationBuilder.getApplicationConfig();
	}

	@Override
	public void writeActivityLog(String eventTitle, String eventData) {
		System.out.println("User activity: " + eventTitle + ", " + eventData);
	}

	@Override
	public void writeExceptionLog(String title, Throwable throwable) {
		System.out.println("Exception: " + title + ", " + throwable.getMessage());
	}

	@Override
	public boolean isAllowed(SimplePrivilege simplePrivilege) {
		return applicationPrivilegeProvider == null || applicationPrivilegeProvider.isAllowed(simplePrivilege);
	}

	@Override
	public boolean isAllowed(SimpleOrganizationalPrivilege group, OrganizationUnitView organizationUnitView) {
		return applicationPrivilegeProvider == null || applicationPrivilegeProvider.isAllowed(group, organizationUnitView);
	}

	@Override
	public boolean isAllowed(SimpleCustomObjectPrivilege group, PrivilegeObject privilegeObject) {
		return applicationPrivilegeProvider == null || applicationPrivilegeProvider.isAllowed(group, privilegeObject);
	}

	@Override
	public boolean isAllowed(StandardPrivilegeGroup group, Privilege privilege) {
		return applicationPrivilegeProvider == null || applicationPrivilegeProvider.isAllowed(group, privilege);
	}

	@Override
	public boolean isAllowed(OrganizationalPrivilegeGroup group, Privilege privilege, OrganizationUnitView organizationUnitView) {
		return applicationPrivilegeProvider == null || applicationPrivilegeProvider.isAllowed(group, privilege, organizationUnitView);
	}

	@Override
	public boolean isAllowed(CustomObjectPrivilegeGroup group, Privilege privilege, PrivilegeObject privilegeObject) {
		return applicationPrivilegeProvider == null || applicationPrivilegeProvider.isAllowed(group, privilege, privilegeObject);
	}

	@Override
	public List<OrganizationUnitView> getAllowedUnits(SimpleOrganizationalPrivilege simplePrivilege) {
		return applicationPrivilegeProvider != null ? applicationPrivilegeProvider.getAllowedUnits(simplePrivilege) : organizationUnitViews;
	}

	@Override
	public List<OrganizationUnitView> getAllowedUnits(OrganizationalPrivilegeGroup group, Privilege privilege) {
		return applicationPrivilegeProvider != null ? applicationPrivilegeProvider.getAllowedUnits(group, privilege) : organizationUnitViews;
	}

	@Override
	public List<PrivilegeObject> getAllowedPrivilegeObjects(SimpleCustomObjectPrivilege simplePrivilege) {
		return applicationPrivilegeProvider != null ? applicationPrivilegeProvider.getAllowedPrivilegeObjects(simplePrivilege) : simplePrivilege.getPrivilegeObjectsSupplier().get();
	}

	@Override
	public List<PrivilegeObject> getAllowedPrivilegeObjects(CustomObjectPrivilegeGroup group, Privilege privilege) {
		return applicationPrivilegeProvider != null ? applicationPrivilegeProvider.getAllowedPrivilegeObjects(group, privilege) : group.getPrivilegeObjectsSupplier().get();
	}

	@Override
	public String getLocalized(String key, Object... parameters) {
		return localizationProvider.getLocalized(key, parameters);
	}

	@Override
	public String getLocalized(String key, List<String> languagePriorityOrder, Object... parameters) {
		return localizationProvider.getLocalized(key, languagePriorityOrder, parameters);
	}

	@Override
	public String getLocalized(TranslatableText translatableText) {
		return localizationProvider.getLocalized(translatableText);
	}
}
