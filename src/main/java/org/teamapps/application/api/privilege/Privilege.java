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
import org.teamapps.icons.Icon;

public interface Privilege {

	Privilege CREATE = create(PrivilegeType.CREATE, "create", null, Dictionary.CREATE);
	Privilege READ = create(PrivilegeType.READ, "read", null, Dictionary.READ);
	Privilege UPDATE = create(PrivilegeType.UPDATE, "update", null, Dictionary.UPDATE);
	Privilege DELETE = create(PrivilegeType.DELETE, "delete", null, Dictionary.DELETE);
	Privilege EXECUTE = create(PrivilegeType.EXECUTE, "execute", null, Dictionary.EXECUTE);
	Privilege PRINT = create(PrivilegeType.PRINT, "print", null, Dictionary.PRINT);
	Privilege CUSTOM = create(PrivilegeType.CUSTOM, "custom", null, Dictionary.CUSTOM);

	static Privilege create(PrivilegeType privilegeType, String name, Icon icon, String titleKey) {
		return new PrivilegeImpl(privilegeType, name, icon, titleKey);
	}

	PrivilegeType getType();

	String getName();

	Icon getIcon();

	String getTitleKey();



}
