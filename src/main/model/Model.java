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
import org.teamapps.universaldb.schema.*;

import static org.teamapps.universaldb.schema.TableOption.*;

public class Model implements SchemaInfoProvider {

	@Override
	public String getSchema() {
		Schema schema = Schema.create("org.teamapps.model");
		Database db = schema.addDatabase("system");
		Table systemStarts = db.addTable("systemStarts");
		systemStarts
				.addTimestamp("timestamp")
				.addEnum("type", "start", "stop")
		;
		return schema.getSchema();
	}
}