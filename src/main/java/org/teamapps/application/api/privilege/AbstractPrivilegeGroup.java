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

import org.teamapps.icons.Icon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class AbstractPrivilegeGroup implements PrivilegeGroup {

	private final String name;
	private final Icon icon;
	private final String titleKey;
	private final String descriptionKey;
	private final List<Privilege> privileges;
	private Supplier<List<PrivilegeObject>> privilegeObjectsSupplier;

	public AbstractPrivilegeGroup(String name, Icon icon, String titleKey, String descriptionKey) {
		this.name = name;
		this.icon = icon;
		this.titleKey = titleKey;
		this.descriptionKey = descriptionKey;
		this.privileges = Collections.emptyList();
	}

	public AbstractPrivilegeGroup(String name, Icon icon, String titleKey, String descriptionKey, List<Privilege> privileges) {
		this.name = name;
		this.icon = icon;
		this.titleKey = titleKey;
		this.descriptionKey = descriptionKey;
		this.privileges = privileges;
	}

	public AbstractPrivilegeGroup(String name, Icon icon, String titleKey, String descriptionKey, Privilege... privileges) {
		this.name = name;
		this.icon = icon;
		this.titleKey = titleKey;
		this.descriptionKey = descriptionKey;
		this.privileges = Arrays.asList(privileges);
	}

	public AbstractPrivilegeGroup(String name, Icon icon, String titleKey, String descriptionKey, List<Privilege> privileges, Supplier<List<PrivilegeObject>> privilegeObjectsSupplier) {
		this.name = name;
		this.icon = icon;
		this.titleKey = titleKey;
		this.descriptionKey = descriptionKey;
		this.privileges = privileges;
		this.privilegeObjectsSupplier = privilegeObjectsSupplier;
	}

	public void setPrivilegeObjectsSupplier(Supplier<List<PrivilegeObject>> privilegeObjectsSupplier) {
		this.privilegeObjectsSupplier = privilegeObjectsSupplier;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Icon getIcon() {
		return icon;
	}

	@Override
	public String getTitleKey() {
		return titleKey;
	}

	@Override
	public String getDescriptionKey() {
		return descriptionKey;
	}

	@Override
	public List<Privilege> getPrivileges() {
		return privileges;
	}

	@Override
	public Supplier<List<PrivilegeObject>> getPrivilegeObjectsSupplier() {
		return privilegeObjectsSupplier;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AbstractPrivilegeGroup that = (AbstractPrivilegeGroup) o;
		return getName().equals(that.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName());
	}
}
