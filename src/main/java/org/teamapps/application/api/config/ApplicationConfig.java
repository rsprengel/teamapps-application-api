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
package org.teamapps.application.api.config;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;
import org.teamapps.event.Event;

public class ApplicationConfig<CONFIG> {

	private CONFIG config;
	public final Event<CONFIG> onConfigUpdate = new Event<>();


	public ApplicationConfig() {
	}

	public CONFIG getConfig() {
		return config;
	}

	public void setConfig(CONFIG config) {
		this.config = config;
	}

	public void updateConfig(String xml, ClassLoader classLoader) throws Exception {
		try {
			XStream xStream = createXStream(classLoader);
			CONFIG config = (CONFIG) xStream.fromXML(xml);
			setConfig(config);
			onConfigUpdate.fire(config);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}

	public String getConfigXml(ClassLoader classLoader) {
		if (getConfig() == null) {
			return null;
		}
		XStream xStream = createXStream(classLoader);
		return xStream.toXML(getConfig());
	}


	private XStream createXStream(ClassLoader classLoader) {
		XStream xStream = new XStream(new DomDriver());
		xStream.setClassLoader(classLoader);
		xStream.addPermission(AnyTypePermission.ANY);
		return xStream;
	}
}
