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

import org.teamapps.application.api.application.ApplicationBuilder;
import org.teamapps.application.api.localization.ApplicationLocalizationProvider;
import org.teamapps.application.api.localization.Language;
import org.teamapps.application.api.privilege.ApplicationRole;
import org.teamapps.application.api.theme.ApplicationIcons;
import org.teamapps.application.ux.IconUtils;
import org.teamapps.application.ux.combo.ComboBoxUtils;
import org.teamapps.data.extract.PropertyProvider;
import org.teamapps.event.Event;
import org.teamapps.icon.antu.AntuIcon;
import org.teamapps.icon.flags.FlagIcon;
import org.teamapps.icon.fontawesome.FontAwesomeIcon;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.mock.model.MockSchema;
import org.teamapps.model.ApiSchema;
import org.teamapps.model.controlcenter.OrganizationUnitView;
import org.teamapps.reporting.convert.DocumentConverter;
import org.teamapps.server.undertow.embedded.TeamAppsUndertowEmbeddedServer;
import org.teamapps.universaldb.UniversalDB;
import org.teamapps.universaldb.schema.SchemaInfoProvider;
import org.teamapps.ux.component.absolutelayout.Length;
import org.teamapps.ux.component.field.Button;
import org.teamapps.ux.component.field.combobox.ComboBox;
import org.teamapps.ux.component.form.ResponsiveForm;
import org.teamapps.ux.component.form.ResponsiveFormLayout;
import org.teamapps.ux.component.panel.ElegantPanel;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.component.template.BaseTemplate;
import org.teamapps.ux.component.template.BaseTemplateRecord;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.WebController;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class DevServer {

	private final ApplicationBuilder applicationBuilder;
	private int port = 8080;
	private File path = new File("./dev-database");
	private List<OrganizationUnitView> organizationUnitViews = Collections.emptyList();
	private DocumentConverter documentConverter;
	public Event<Void> onDevServerBooted = new Event<>();

	private static Class standardIconClass;
	static {
		try {
			standardIconClass = Class.forName("org.teamapps.icon.standard.StandardIcon");
		} catch (Exception var1) {
		}
	}

	public static DevServer create(ApplicationBuilder applicationBuilder) {
		return new DevServer(applicationBuilder);
	}

	protected DevServer(ApplicationBuilder applicationBuilder) {
		this.applicationBuilder = applicationBuilder;
	}

	public DevServer withDbPath(File path) {
		this.path = path;
		return this;
	}

	public DevServer withPort(int port) {
		this.port = port;
		return this;
	}

	public DevServer withOrganizationUnitViews(List<OrganizationUnitView> organizationUnitViews) {
		this.organizationUnitViews = organizationUnitViews;
		return this;
	}

	public DevServer withDocumentConverter(DocumentConverter documentConverter) {
		this.documentConverter = documentConverter;
		return this;
	}

	public void start() {
		try {
			path.mkdir();
			SchemaInfoProvider databaseModel = applicationBuilder.getDatabaseModel();
			if (databaseModel != null) {
				UniversalDB universalDB = UniversalDB.createStandalone(path, new MockSchema());
				ClassLoader classLoader = DevServer.class.getClassLoader();

				ApiSchema apiSchema = new ApiSchema();
				universalDB.addAuxiliaryModel(apiSchema, classLoader);
				universalDB.installAuxiliaryModelClassed(apiSchema, classLoader);
				universalDB.installTableViews(apiSchema, classLoader);

				universalDB.addAuxiliaryModel(databaseModel, classLoader);
				universalDB.installAuxiliaryModelClassed(databaseModel, classLoader);
				universalDB.installTableViews(databaseModel, classLoader);
			}
			onDevServerBooted.fire();
			applicationBuilder.getOnApplicationInstalled().fire();
			applicationBuilder.getOnApplicationLoaded().fire();
			WebController webController = sessionContext -> {
				SessionContext context = SessionContext.current();

				registerBaseIconProvider(context);
				if (standardIconClass != null) {
					context.getIconProvider().registerIconLibrary(standardIconClass);
				}

				RootPanel rootPanel = context.addRootPanel();

				ResponsiveForm form = new ResponsiveForm(120, 120, 300);
				ResponsiveFormLayout formLayout = form.addResponsiveFormLayout(420);
				ElegantPanel elegantPanel = new ElegantPanel();
				form.setMaxWidth(Length.ofPixels(430));
				elegantPanel.setContent(form);
				elegantPanel.setCssStyle(".content-container", "display", "flex");
				elegantPanel.setCssStyle(".content-container", "justify-content", "center");

				DevLocalizationProvider localizationProvider = new DevLocalizationProvider(applicationBuilder);
				localizationProvider.setLanguage(context.getLocale().getLanguage());

   				List<RoleEntry> roleEntries = new ArrayList<>();
				roleEntries.add(new RoleEntry(null));
				if (applicationBuilder.getApplicationRoles() != null) {
					roleEntries.addAll(applicationBuilder.getApplicationRoles().stream().map(RoleEntry::new).collect(Collectors.toList()));
				}
				ComboBox<RoleEntry> roleEntryComboBox = ComboBoxUtils.createRecordComboBox(roleEntries, (roleEntry, propertyNames) -> roleEntry.getPropertyMap(localizationProvider), BaseTemplate.LIST_ITEM_SMALL_ICON_SINGLE_LINE);

				List<OrganizationUnitView> orgUnits = organizationUnitViews != null && !organizationUnitViews.isEmpty() ? organizationUnitViews : OrganizationUnitView.getAll();
				ComboBox<OrganizationUnitView> rootUnitsComboBox = createOrgUnitComboBox(localizationProvider, orgUnits);
				ComboBox<Language> languageComboBox = Language.createComboBox(localizationProvider);
				languageComboBox.setValue(Language.getLanguageByIsoCode(context.getLocale().getLanguage()));
				if (!orgUnits.isEmpty()) {
					rootUnitsComboBox.setValue(orgUnits.get(0));
				}
				roleEntryComboBox.setValue(roleEntries.get(0));
				Button<BaseTemplateRecord> loginButton = Button.create("Login");

				formLayout.addSection().setCollapsible(false).setDrawHeaderLine(false);
				formLayout.addLabelAndField(null, "Language", languageComboBox);
				formLayout.addLabelAndField(null, "Application role", roleEntryComboBox);
				formLayout.addLabelAndField(null, "Organization root", rootUnitsComboBox);
				formLayout.addLabelAndField(null, null, loginButton);

				rootPanel.setContent(elegantPanel);
				String loginBackground = "/resources/backgrounds/login.jpg";
				context.registerBackgroundImage("login", loginBackground, loginBackground);
				context.setBackgroundImage("login", 0);

				loginButton.onClicked.addListener(() -> {
					ApplicationRole applicationRole = roleEntryComboBox.getValue().getRole();
					String languageIso = languageComboBox.getValue().getIsoCode();
					Locale locale = Locale.forLanguageTag(languageIso);
					List<OrganizationUnitView> units = getAllUnits(rootUnitsComboBox.getValue());
					localizationProvider.setLanguage(languageIso);
					DevApplication devApplication = new DevApplication(applicationRole, context, locale, localizationProvider, applicationBuilder, units, documentConverter);
					rootPanel.setContent(devApplication.getComponent());
					context.showDefaultBackground(0);
				});
			};
			File webAppDirectory = Files.createTempDirectory("teamapps").toRealPath().toFile();
			TeamAppsUndertowEmbeddedServer server = new TeamAppsUndertowEmbeddedServer(webController, webAppDirectory, port);
			server.start();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void registerBaseIconProvider(SessionContext context) {
		context.getIconProvider().registerIconLibrary(FlagIcon.class);
		context.getIconProvider().registerIconLibrary(MaterialIcon.class);
		context.getIconProvider().registerIconLibrary(FontAwesomeIcon.class);
		context.getIconProvider().registerIconLibrary(AntuIcon.class);
	}

	private List<OrganizationUnitView> getAllUnits(OrganizationUnitView rootUnit) {
		if (rootUnit == null) {
			return organizationUnitViews;
		} else {
			Set<OrganizationUnitView> unitSet = new HashSet<>();
			getUnits(rootUnit, unitSet);
			return new ArrayList<>(unitSet);
		}
	}

	private void getUnits(OrganizationUnitView unit, Set<OrganizationUnitView> unitSet) {
		if (!unitSet.contains(unit)) {
			unitSet.add(unit);
			unit.getChildren().forEach(child -> getUnits(child, unitSet));
		}
	}

	private ComboBox<OrganizationUnitView> createOrgUnitComboBox(ApplicationLocalizationProvider localizationProvider, List<OrganizationUnitView> unitRoots) {
		PropertyProvider<OrganizationUnitView> organizationUnitViewPropertyProvider = (unit, propertyNames) -> {
			Map<String, Object> map = new HashMap<>();
			map.put(BaseTemplate.PROPERTY_ICON, IconUtils.decodeIcon(unit.getIcon()));
			map.put(BaseTemplate.PROPERTY_CAPTION, localizationProvider.getLocalized(unit.getName()));
			return map;
		};
		return ComboBoxUtils.createRecordComboBox(unitRoots, organizationUnitViewPropertyProvider, BaseTemplate.LIST_ITEM_SMALL_ICON_SINGLE_LINE);
	}

	static class RoleEntry {
		private final ApplicationRole role;

		public RoleEntry(ApplicationRole role) {
			this.role = role;
		}

		public ApplicationRole getRole() {
			return role;
		}

		public Map<String, Object> getPropertyMap(ApplicationLocalizationProvider localizationProvider){
			Map<String, Object> map = new HashMap<>();
			if (role != null) {
				map.put(BaseTemplate.PROPERTY_ICON, role.getIcon());
				map.put(BaseTemplate.PROPERTY_CAPTION, localizationProvider.getLocalized(role.getTitleKey()));
				map.put(BaseTemplate.PROPERTY_DESCRIPTION, localizationProvider.getLocalized(role.getDescriptionKey()));
			} else {
				map.put(BaseTemplate.PROPERTY_ICON, ApplicationIcons.SHAPE_CIRCLE);
				map.put(BaseTemplate.PROPERTY_CAPTION, "Allow all");
				map.put(BaseTemplate.PROPERTY_DESCRIPTION, "Allow all privilege provider");
			}
			return map;
		}
	}


}
