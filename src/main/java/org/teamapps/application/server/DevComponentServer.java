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

import org.teamapps.icon.antu.AntuIcon;
import org.teamapps.icon.flags.FlagIcon;
import org.teamapps.icon.fontawesome.FontAwesomeIcon;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.undertow.embedded.TeamAppsUndertowEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.WebController;

import java.util.function.Supplier;

public class DevComponentServer {

	private static Class standardIconClass;
	static {
		try {
			standardIconClass = Class.forName("org.teamapps.icon.standard.StandardIcon");
		} catch (Exception var1) {
		}
	}

	public static void showComponent(Supplier<Component> componentSupplier) {
		showComponent(componentSupplier, 8080);
	}

	public static void showComponent(Supplier<Component> componentSupplier, int port) {
		try {
			WebController webController = sessionContext -> {
				SessionContext context = SessionContext.current();

				registerBaseIconProvider(context);
				if (standardIconClass != null) {
					context.getIconProvider().registerIconLibrary(standardIconClass);
				}

				RootPanel rootPanel = context.addRootPanel();
				context.showDefaultBackground(0);
				rootPanel.setContent(componentSupplier.get());
			};
			TeamAppsUndertowEmbeddedServer server = new TeamAppsUndertowEmbeddedServer(webController, port);
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void registerBaseIconProvider(SessionContext context) {
		context.getIconProvider().registerIconLibrary(FlagIcon.class);
		context.getIconProvider().registerIconLibrary(MaterialIcon.class);
		context.getIconProvider().registerIconLibrary(FontAwesomeIcon.class);
		context.getIconProvider().registerIconLibrary(AntuIcon.class);
	}
}
