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
package org.teamapps.application.ux.form;

import org.teamapps.application.api.application.ApplicationInstanceData;
import org.teamapps.application.api.localization.Dictionary;
import org.teamapps.application.api.theme.ApplicationIcons;
import org.teamapps.icons.composite.CompositeIcon;
import org.teamapps.ux.component.toolbar.ToolbarButton;

public class FormButtonUtils {

	public static ToolbarButton createNewButton(ApplicationInstanceData applicationInstanceData) {
		return ToolbarButton.createSmall(ApplicationIcons.ADD, applicationInstanceData.getLocalized(Dictionary.NEW));
	}

	public static ToolbarButton createAddButton(ApplicationInstanceData applicationInstanceData) {
		return ToolbarButton.createSmall(ApplicationIcons.ADD, applicationInstanceData.getLocalized(Dictionary.ADD));
	}

	public static ToolbarButton createSaveButton(ApplicationInstanceData applicationInstanceData) {
		return ToolbarButton.createSmall(ApplicationIcons.FLOPPY_DISK, applicationInstanceData.getLocalized(Dictionary.SAVE));
	}

	public static ToolbarButton createRevertButton(ApplicationInstanceData applicationInstanceData) {
		return ToolbarButton.createSmall(ApplicationIcons.UNDO, applicationInstanceData.getLocalized(Dictionary.REVERT_CHANGES));
	}

	public static ToolbarButton createDeleteButton(ApplicationInstanceData applicationInstanceData) {
		return ToolbarButton.createSmall(ApplicationIcons.GARBAGE_EMPTY, applicationInstanceData.getLocalized(Dictionary.DELETE));
	}

	public static ToolbarButton createRestoreButton(ApplicationInstanceData applicationInstanceData) {
		return ToolbarButton.createSmall(CompositeIcon.of(ApplicationIcons.GARBAGE_MAKE_EMPTY, ApplicationIcons.ADD), applicationInstanceData.getLocalized(Dictionary.RESTORE));
	}

}
