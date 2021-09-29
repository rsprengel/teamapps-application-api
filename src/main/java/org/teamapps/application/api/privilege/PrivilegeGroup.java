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

import org.teamapps.application.api.localization.Dictionary;
import org.teamapps.application.api.theme.ApplicationIcons;
import org.teamapps.icons.Icon;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public interface PrivilegeGroup {


	static SimplePrivilege createSimplePrivilege(String name, Icon icon, String titleKey, String descriptionKey) {
		return new SimplePrivilegeImpl(name, icon, titleKey, descriptionKey);
	}

	static SimpleOrganizationalPrivilege createSimpleOrganizationalPrivilege(String name, Icon icon, String titleKey, String descriptionKey) {
		return new SimpleOrganizationalPrivilegeImpl(name, icon, titleKey, descriptionKey);
	}

	static SimpleCustomObjectPrivilege createSimpleCustomObjectPrivilege(String name, Icon icon, String titleKey, String descriptionKey, Supplier<List<PrivilegeObject>> privilegeObjectsSupplier) {
		return new SimpleCustomObjectPrivilegeImpl(name, icon, titleKey, descriptionKey, privilegeObjectsSupplier);
	}

	static StandardPrivilegeGroup createStandardPrivilegeGroup(String name, Icon icon, String titleKey, String descriptionKey, Privilege... privileges) {
		return new StandardPrivilegeGroupImpl(name, icon, titleKey, descriptionKey, privileges);
	}

	static OrganizationalPrivilegeGroup createOrganizationalPrivilegeGroup(String name, Icon icon, String titleKey, String descriptionKey, Privilege... privileges) {
		return new OrganizationalPrivilegeGroupImpl(name, icon, titleKey, descriptionKey, privileges);
	}

	static CustomObjectPrivilegeGroup createCustomObjectPrivilegeGroup(String name, Icon icon, String titleKey, String descriptionKey, List<Privilege> privileges, Supplier<List<PrivilegeObject>> privilegeObjectsSupplier) {
		return new CustomObjectPrivilegeGroupImpl(name, icon, titleKey, descriptionKey, privileges, privilegeObjectsSupplier);
	}

	default PrivilegeGroup createCopyWithPrivileges(Privilege... privileges) {
		PrivilegeGroup privilegeGroup = this;
		return new PrivilegeGroup() {
			@Override
			public PrivilegeGroupType getType() {
				return privilegeGroup.getType();
			}

			@Override
			public String getName() {
				return privilegeGroup.getName();
			}

			@Override
			public Icon getIcon() {
				return privilegeGroup.getIcon();
			}

			@Override
			public String getTitleKey() {
				return privilegeGroup.getTitleKey();
			}

			@Override
			public String getDescriptionKey() {
				return privilegeGroup.getDescriptionKey();
			}

			@Override
			public List<Privilege> getPrivileges() {
				return Arrays.asList(privileges);
			}

			@Override
			public Supplier<List<PrivilegeObject>> getPrivilegeObjectsSupplier() {
				return privilegeGroup.getPrivilegeObjectsSupplier();
			}
		};
	}

	PrivilegeGroupType getType();

	String getName();

	Icon getIcon();

	String getTitleKey();

	String getDescriptionKey();

	List<Privilege> getPrivileges();

	Supplier<List<PrivilegeObject>> getPrivilegeObjectsSupplier();
}
