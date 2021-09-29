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

public interface Privilege {

	Privilege CREATE = create(PrivilegeType.CREATE, "create", ApplicationIcons.DOCUMENT_EMPTY, Dictionary.CREATE);
	Privilege READ = create(PrivilegeType.READ, "read", ApplicationIcons.DOCUMENT_TEXT, Dictionary.READ);
	Privilege UPDATE = create(PrivilegeType.UPDATE, "update", ApplicationIcons.EDIT, Dictionary.UPDATE);
	Privilege DELETE = create(PrivilegeType.DELETE, "delete", ApplicationIcons.GARBAGE_EMPTY, Dictionary.DELETE);
	Privilege RESTORE = create(PrivilegeType.RESTORE, "restore", ApplicationIcons.UNDO, Dictionary.RESTORE);
	Privilege SHOW_RECYCLE_BIN = create(PrivilegeType.SHOW_RECYCLE_BIN, "readRecycleBin", ApplicationIcons.GARBAGE_OVERFLOW, Dictionary.SHOW_RECYCLE_BIN);
	Privilege EXECUTE = create(PrivilegeType.EXECUTE, "execute", ApplicationIcons.GEARWHEEL, Dictionary.EXECUTE);
	Privilege PRINT = create(PrivilegeType.PRINT, "print", ApplicationIcons.PRINTER, Dictionary.PRINT);
	Privilege IMPORT = create(PrivilegeType.IMPORT, "import", ApplicationIcons.ARROW_INTO, Dictionary.IMPORT);
	Privilege EXPORT = create(PrivilegeType.EXPORT, "export", ApplicationIcons.ARROW_OUT, Dictionary.EXPORT);
	Privilege CUSTOM = create(PrivilegeType.CUSTOM, "custom", ApplicationIcons.LOCK_OPEN, Dictionary.CUSTOM);

	static Privilege create(PrivilegeType privilegeType, String name, Icon icon, String titleKey) {
		return new PrivilegeImpl(privilegeType, name, icon, titleKey);
	}

	static Privilege[] getDefault() {
		return new Privilege[]{CREATE, READ, UPDATE, DELETE, RESTORE, SHOW_RECYCLE_BIN};
	}

	static Privilege[] getAll() {
		return new Privilege[]{CREATE, READ, UPDATE, DELETE, RESTORE, SHOW_RECYCLE_BIN, EXECUTE, PRINT, IMPORT, EXPORT};
	}

	PrivilegeType getType();

	String getName();

	Icon getIcon();

	String getTitleKey();


}
