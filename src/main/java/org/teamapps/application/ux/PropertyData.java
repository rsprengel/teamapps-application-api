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
package org.teamapps.application.ux;

import org.teamapps.icons.Icon;
import org.teamapps.ux.component.template.BaseTemplate;

import java.util.HashMap;
import java.util.Map;

public class PropertyData {

	public static Map<String, Object> createEmpty() {
		return new HashMap<>();
	}

	public static Map<String, Object> create(Icon icon, String title) {
		return create(title, null, icon, null);
	}

	public static Map<String, Object> create(Icon icon, String title, String description) {
		return create(title, description, icon, null);
	}

	public static Map<String, Object> create(String title, String description, Icon icon, String image) {
		Map<String, Object> map = new HashMap<>();
		if (icon != null) {
			map.put(BaseTemplate.PROPERTY_ICON, icon);
		}
		if (image != null) {
			map.put(BaseTemplate.PROPERTY_IMAGE, image);
		}
		if (title != null) {
			map.put(BaseTemplate.PROPERTY_CAPTION, title);
		}
		if (description != null) {
			map.put(BaseTemplate.PROPERTY_DESCRIPTION, description);
		}
		return map;
	}
}
