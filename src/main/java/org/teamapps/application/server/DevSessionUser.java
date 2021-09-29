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

import com.ibm.icu.util.ULocale;
import org.teamapps.application.api.user.SessionUser;
import org.teamapps.ux.session.SessionContext;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DevSessionUser implements SessionUser {

	private final SessionContext context;
	private final Locale locale;

	public DevSessionUser(SessionContext context, Locale locale) {
		this.context = context;
		this.locale = locale;
	}

	@Override
	public int getId() {
		return 1;
	}

	@Override
	public String getName(boolean lastNameFirst) {
		return lastNameFirst ? "Doe, John" : "John Doe";
	}

	@Override
	public String getProfilePictureLink() {
		return null;
	}

	@Override
	public String getLargeProfilePictureLink() {
		return null;
	}

	@Override
	public SessionContext getSessionContext() {
		return context;
	}

	@Override
	public ULocale getULocale() {
		return ULocale.forLocale(locale);
	}

	@Override
	public Locale getLocale() {
		return locale;
	}

	@Override
	public List<String> getRankedLanguages() {
		return Collections.singletonList(locale.getLanguage());
	}
}
