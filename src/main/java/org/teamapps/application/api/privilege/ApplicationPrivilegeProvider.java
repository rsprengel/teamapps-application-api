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
package org.teamapps.application.api.privilege;


import org.teamapps.model.controlcenter.OrganizationUnitView;

import java.util.List;

public interface ApplicationPrivilegeProvider {

	boolean isAllowed(SimplePrivilege simplePrivilege);

	boolean isAllowed(SimpleOrganizationalPrivilege group, OrganizationUnitView OrganizationUnitView);

	boolean isAllowed(SimpleCustomObjectPrivilege group, PrivilegeObject privilegeObject);

	boolean isAllowed(StandardPrivilegeGroup group, Privilege privilege);

	boolean isAllowed(OrganizationalPrivilegeGroup group, Privilege privilege, OrganizationUnitView OrganizationUnitView);

	boolean isAllowed(CustomObjectPrivilegeGroup group, Privilege privilege, PrivilegeObject privilegeObject);

	List<OrganizationUnitView> getAllowedUnits(SimpleOrganizationalPrivilege simplePrivilege);

	List<OrganizationUnitView> getAllowedUnits(OrganizationalPrivilegeGroup group, Privilege privilege);

	List<PrivilegeObject> getAllowedPrivilegeObjects(SimpleCustomObjectPrivilege simplePrivilege);

	List<PrivilegeObject> getAllowedPrivilegeObjects(CustomObjectPrivilegeGroup group, Privilege privilege);

}
