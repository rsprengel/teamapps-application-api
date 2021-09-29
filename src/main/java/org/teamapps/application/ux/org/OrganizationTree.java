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
package org.teamapps.application.ux.org;

import edu.emory.mathcs.backport.java.util.Collections;
import org.teamapps.application.api.application.AbstractApplicationView;
import org.teamapps.application.api.application.ApplicationInstanceData;
import org.teamapps.model.controlcenter.OrganizationUnitView;
import org.teamapps.ux.component.template.BaseTemplate;
import org.teamapps.ux.component.template.Template;
import org.teamapps.ux.component.tree.Tree;
import org.teamapps.ux.component.tree.TreeNodeInfoImpl;
import org.teamapps.ux.model.ListTreeModel;

import java.util.*;

public class OrganizationTree extends AbstractApplicationView {

	private Tree<OrganizationUnitView> tree;
	private ListTreeModel<OrganizationUnitView> treeModel;
	private Set<OrganizationUnitView> organizationUnits;

	public OrganizationTree(ApplicationInstanceData applicationInstanceData) {
		super(applicationInstanceData);
		treeModel = new ListTreeModel<>(Collections.emptyList());
		tree = new Tree<>(treeModel);
		treeModel.setTreeNodeInfoFunction(unit -> new TreeNodeInfoImpl<>(unit.getParent() != null && organizationUnits.contains(unit.getParent()) ? unit.getParent() : null, false));
		tree.setPropertyProvider(OrganizationViewUtils.creatOrganizationUnitViewPropertyProvider(getApplicationInstanceData()));
		tree.setEntryTemplate(BaseTemplate.LIST_ITEM_SMALL_ICON_SINGLE_LINE);
	}

	public void setTemplate(Template template) {
		tree.setEntryTemplate(template);
	}

	public void setOrganizationUnits(Collection<OrganizationUnitView> units) {
		organizationUnits = new HashSet<>(units);
		treeModel.setRecords(new ArrayList<>(units));
	}

	public Tree<OrganizationUnitView> getTree() {
		return tree;
	}
}
