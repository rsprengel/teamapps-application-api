/*-
 * ========================LICENSE_START=================================
 * TeamApps Application API
 * ---
 * Copyright (C) 2020 TeamApps.org
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
import org.teamapps.application.api.application.ApplicationPerspective;
import org.teamapps.application.api.application.ApplicationPerspectiveBuilder;
import org.teamapps.application.api.application.PerspectiveBuilder;
import org.teamapps.application.api.localization.Dictionary;
import org.teamapps.application.api.organization.OrgUnit;
import org.teamapps.common.format.Color;
import org.teamapps.application.api.theme.ApplicationIcons;
import org.teamapps.reporting.convert.DocumentConverter;
import org.teamapps.ux.application.ResponsiveApplication;
import org.teamapps.ux.application.layout.StandardLayout;
import org.teamapps.ux.application.view.View;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.animation.PageTransition;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.mobile.MobileLayout;
import org.teamapps.ux.component.template.BaseTemplate;
import org.teamapps.ux.component.toolbar.Toolbar;
import org.teamapps.ux.component.toolbar.ToolbarButton;
import org.teamapps.ux.component.toolbar.ToolbarButtonGroup;
import org.teamapps.ux.component.tree.Tree;
import org.teamapps.ux.model.ListTreeModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevApplication {

	private final ApplicationPerspectiveBuilder applicationBuilder;
	private final DevApplicationData applicationData;
	private ResponsiveApplication application;

	public DevApplication(ApplicationPerspectiveBuilder applicationBuilder, List<OrgUnit> orgUnits, DocumentConverter documentConverter) {
		this.applicationBuilder = applicationBuilder;
		applicationData = new DevApplicationData(applicationBuilder, orgUnits, documentConverter);
		createUi();
	}

	private void createUi() {
		application = ResponsiveApplication.createApplication();

		View applicationMenu = View.createView(StandardLayout.LEFT, ApplicationIcons.WINDOW, applicationData.getLocalized(Dictionary.APPLICATIONS), null);
		application.addApplicationView(applicationMenu);

		applicationMenu.getPanel().setBodyBackgroundColor(Color.WHITE.withAlpha(0.94f));

		VerticalLayout verticalLayout = new VerticalLayout();
		applicationMenu.setComponent(verticalLayout);

		Toolbar toolbar = new Toolbar();
		ToolbarButtonGroup buttonGroup = toolbar.addButtonGroup(new ToolbarButtonGroup());
		buttonGroup.setShowGroupSeparator(false);
		ToolbarButton backButton = ToolbarButton.createSmall(ApplicationIcons.NAV_LEFT, applicationData.getLocalized(Dictionary.BACK));
		backButton.setVisible(false);
		buttonGroup.addButton(backButton);
		verticalLayout.addComponent(toolbar);
		MobileLayout mobileLayout = new MobileLayout();
		verticalLayout.addComponentFillRemaining(mobileLayout);


		ListTreeModel<PerspectiveBuilder> treeModel = new ListTreeModel<>(applicationBuilder.getPerspectiveBuilders());
		Tree<PerspectiveBuilder> tree = new Tree<>(treeModel);
		tree.setShowExpanders(false);
		tree.setEntryTemplate(BaseTemplate.LIST_ITEM_VERY_LARGE_ICON_TWO_LINES);
		tree.setPropertyExtractor((perspectiveBuilder, propertyName) -> {
			switch (propertyName) {
				case BaseTemplate.PROPERTY_BADGE:
					return null; //todo
				case BaseTemplate.PROPERTY_ICON:
					return perspectiveBuilder.getIcon();
				case BaseTemplate.PROPERTY_CAPTION:
					return applicationData.getLocalized(perspectiveBuilder.getTitleKey());
				case BaseTemplate.PROPERTY_DESCRIPTION:
					return applicationData.getLocalized(perspectiveBuilder.getDescriptionKey());
				default:
					return null;
			}
		});
		mobileLayout.setContent(tree);

		Map<PerspectiveBuilder, ApplicationPerspective> applicationPerspectiveByPerspectiveBuilder = new HashMap<>();

		tree.onNodeSelected.addListener(perspectiveBuilder -> {
			showPerspective(perspectiveBuilder, applicationData, application, backButton, mobileLayout, applicationPerspectiveByPerspectiveBuilder);
		});

		showPerspective(applicationBuilder.getPerspectiveBuilders().get(0), applicationData, application, backButton, mobileLayout, applicationPerspectiveByPerspectiveBuilder);

		backButton.onClick.addListener(() -> {
			backButton.setVisible(false);
			mobileLayout.setContent(tree, PageTransition.MOVE_TO_RIGHT_VS_MOVE_FROM_LEFT, 500);
		});

	}

	private void showPerspective(PerspectiveBuilder perspectiveBuilder, ApplicationInstanceData applicationData, ResponsiveApplication application, ToolbarButton backButton, MobileLayout mobileLayout, Map<PerspectiveBuilder, ApplicationPerspective> applicationPerspectiveByPerspectiveBuilder) {
		ApplicationPerspective applicationPerspective = applicationPerspectiveByPerspectiveBuilder.get(perspectiveBuilder);
		if (applicationPerspective == null) {
			applicationPerspective = perspectiveBuilder.build(applicationData, null);
			applicationPerspectiveByPerspectiveBuilder.put(perspectiveBuilder, applicationPerspective);
			application.addPerspective(applicationPerspective.getPerspective());
		}
		application.showPerspective(applicationPerspective.getPerspective());
		if (applicationPerspective.getPerspectiveMenuPanel() != null) {
			backButton.setVisible(true);
			mobileLayout.setContent(applicationPerspective.getPerspectiveMenuPanel(), PageTransition.MOVE_TO_LEFT_VS_MOVE_FROM_RIGHT, 500);
		}
	}

	public Component getComponent() {
		return application.getUi();
	}

}
