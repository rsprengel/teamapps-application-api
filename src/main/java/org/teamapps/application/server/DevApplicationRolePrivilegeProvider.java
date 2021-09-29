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

import org.teamapps.application.api.privilege.*;
import org.teamapps.model.controlcenter.OrganizationUnitView;

import java.util.*;

public class DevApplicationRolePrivilegeProvider implements ApplicationPrivilegeProvider {

	private final ApplicationRole applicationRole;
	private final Set<OrganizationUnitView> allowedUnits;

	private Set<SimplePrivilege> simplePrivileges = new HashSet<>();
	private Map<SimpleOrganizationalPrivilege, Set<OrganizationUnitView>> simpleOrganizationalPrivilegeSetMap = new HashMap<>();
	private Map<SimpleCustomObjectPrivilege, Set<PrivilegeObject>> simpleCustomObjectPrivilegeSetMap = new HashMap<>();
	private Map<StandardPrivilegeGroup, Set<Privilege>> standardPrivilegeGroupSetMap = new HashMap<>();
	private Map<OrganizationalPrivilegeGroup, Map<Privilege, Set<OrganizationUnitView>>> organizationalPrivilegeGroupMap = new HashMap<>();
	private Map<CustomObjectPrivilegeGroup, Map<Privilege, Set<PrivilegeObject>>> customObjectPrivilegeGroupMap = new HashMap<>();

	public DevApplicationRolePrivilegeProvider(ApplicationRole applicationRole, Set<OrganizationUnitView> allowedUnits) {
		this.applicationRole = applicationRole;
		this.allowedUnits = allowedUnits;
		init();
	}

	private void init() {
		for (PrivilegeGroup privilegeGroup : applicationRole.getPrivilegeGroups()) {
			if (privilegeGroup instanceof SimplePrivilege) {
				SimplePrivilege simplePrivilege = (SimplePrivilege) privilegeGroup;
				simplePrivileges.add(simplePrivilege);
			} else if (privilegeGroup instanceof SimpleOrganizationalPrivilege) {
				SimpleOrganizationalPrivilege simpleOrganizationalPrivilege = (SimpleOrganizationalPrivilege) privilegeGroup;
				simpleOrganizationalPrivilegeSetMap.put(simpleOrganizationalPrivilege, allowedUnits);
			} else if (privilegeGroup instanceof SimpleCustomObjectPrivilege) {
				SimpleCustomObjectPrivilege customObjectPrivilege = (SimpleCustomObjectPrivilege) privilegeGroup;
				simpleCustomObjectPrivilegeSetMap.put(customObjectPrivilege, new HashSet<>(customObjectPrivilege.getPrivilegeObjectsSupplier().get()));
			} else if (privilegeGroup instanceof StandardPrivilegeGroup) {
				StandardPrivilegeGroup standardPrivilegeGroup = (StandardPrivilegeGroup) privilegeGroup;
				standardPrivilegeGroupSetMap.put(standardPrivilegeGroup, new HashSet<>(standardPrivilegeGroup.getPrivileges()));
			} else if (privilegeGroup instanceof OrganizationalPrivilegeGroup) {
				OrganizationalPrivilegeGroup organizationalPrivilegeGroup = (OrganizationalPrivilegeGroup) privilegeGroup;
				for (Privilege privilege : organizationalPrivilegeGroup.getPrivileges()) {
					organizationalPrivilegeGroupMap.computeIfAbsent(organizationalPrivilegeGroup, pg -> new HashMap<>()).put(privilege, allowedUnits);
				}
			} else if (privilegeGroup instanceof CustomObjectPrivilegeGroup) {
				CustomObjectPrivilegeGroup objectPrivilegeGroup = (CustomObjectPrivilegeGroup) privilegeGroup;
				for (Privilege privilege : objectPrivilegeGroup.getPrivileges()) {
					customObjectPrivilegeGroupMap.computeIfAbsent(objectPrivilegeGroup, pg -> new HashMap<>()).put(privilege, new HashSet<>(objectPrivilegeGroup.getPrivilegeObjectsSupplier().get()));
				}
			}
		}
	}

	@Override
	public boolean isAllowed(SimplePrivilege simplePrivilege) {
		if (simplePrivileges == null) {
			return false;
		} else {
			return simplePrivileges.contains(simplePrivilege);
		}
	}

	@Override
	public boolean isAllowed(SimpleOrganizationalPrivilege simpleOrganizationalPrivilege, OrganizationUnitView organizationUnitView) {
		if (simpleOrganizationalPrivilegeSetMap == null || !simpleOrganizationalPrivilegeSetMap.containsKey(simpleOrganizationalPrivilege)) {
			return false;
		} else {
			return simpleOrganizationalPrivilegeSetMap.get(simpleOrganizationalPrivilege).contains(organizationUnitView);
		}
	}

	@Override
	public boolean isAllowed(SimpleCustomObjectPrivilege simpleCustomObjectPrivilege, PrivilegeObject privilegeObject) {
		if (simpleCustomObjectPrivilegeSetMap == null || !simpleCustomObjectPrivilegeSetMap.containsKey(simpleCustomObjectPrivilege)) {
			return false;
		} else {
			return simpleCustomObjectPrivilegeSetMap.get(simpleCustomObjectPrivilege).contains(privilegeObject);
		}
	}

	@Override
	public boolean isAllowed(StandardPrivilegeGroup standardPrivilegeGroup, Privilege privilege) {
		if (standardPrivilegeGroupSetMap == null || !standardPrivilegeGroupSetMap.containsKey(standardPrivilegeGroup)) {
			return false;
		} else {
			return standardPrivilegeGroupSetMap.get(standardPrivilegeGroup).contains(privilege);
		}
	}

	@Override
	public boolean isAllowed(OrganizationalPrivilegeGroup organizationalPrivilegeGroup, Privilege privilege, OrganizationUnitView organizationUnitView) {
		if (organizationalPrivilegeGroupMap == null || !organizationalPrivilegeGroupMap.containsKey(organizationalPrivilegeGroup)) {
			return false;
		} else {
			Set<OrganizationUnitView> organizationUnitViews = organizationalPrivilegeGroupMap.get(organizationalPrivilegeGroup).get(privilege);
			if (organizationUnitViews != null && organizationUnitViews.contains(organizationUnitView)) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public boolean isAllowed(CustomObjectPrivilegeGroup customObjectPrivilegeGroup, Privilege privilege, PrivilegeObject privilegeObject) {
		if (customObjectPrivilegeGroupMap == null || !customObjectPrivilegeGroupMap.containsKey(customObjectPrivilegeGroup)) {
			return false;
		} else {
			Set<PrivilegeObject> privilegeObjects = customObjectPrivilegeGroupMap.get(customObjectPrivilegeGroup).get(privilege);
			if (privilegeObjects != null && privilegeObjects.contains(privilegeObject)) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public List<OrganizationUnitView> getAllowedUnits(SimpleOrganizationalPrivilege simpleOrganizationalPrivilege) {
		if (simpleOrganizationalPrivilegeSetMap == null || !simpleOrganizationalPrivilegeSetMap.containsKey(simpleOrganizationalPrivilege)) {
			return Collections.emptyList();
		} else {
			return new ArrayList<>(simpleOrganizationalPrivilegeSetMap.get(simpleOrganizationalPrivilege));
		}
	}

	@Override
	public List<OrganizationUnitView> getAllowedUnits(OrganizationalPrivilegeGroup organizationalPrivilegeGroup, Privilege privilege) {
		if (organizationalPrivilegeGroupMap == null || !organizationalPrivilegeGroupMap.containsKey(organizationalPrivilegeGroup)) {
			return Collections.emptyList();
		} else {
			Set<OrganizationUnitView> organizationUnitViews = organizationalPrivilegeGroupMap.get(organizationalPrivilegeGroup).get(privilege);
			return new ArrayList<>(organizationUnitViews);
		}
	}

	@Override
	public List<PrivilegeObject> getAllowedPrivilegeObjects(SimpleCustomObjectPrivilege simpleCustomObjectPrivilege) {
		if (simpleCustomObjectPrivilegeSetMap == null || !simpleCustomObjectPrivilegeSetMap.containsKey(simpleCustomObjectPrivilege)) {
			return Collections.emptyList();
		} else {
			return new ArrayList<>(simpleCustomObjectPrivilegeSetMap.get(simpleCustomObjectPrivilege));
		}
	}

	@Override
	public List<PrivilegeObject> getAllowedPrivilegeObjects(CustomObjectPrivilegeGroup customObjectPrivilegeGroup, Privilege privilege) {
		if (customObjectPrivilegeGroupMap == null || !customObjectPrivilegeGroupMap.containsKey(customObjectPrivilegeGroup)) {
			return Collections.emptyList();
		} else {
			Set<PrivilegeObject> privilegeObjects = customObjectPrivilegeGroupMap.get(customObjectPrivilegeGroup).get(privilege);
			return new ArrayList<>(privilegeObjects);
		}
	}
}
