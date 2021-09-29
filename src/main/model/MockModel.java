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
import org.teamapps.universaldb.schema.Database;
import org.teamapps.universaldb.schema.Schema;
import org.teamapps.universaldb.schema.SchemaInfoProvider;
import org.teamapps.universaldb.schema.Table;

import static org.teamapps.universaldb.schema.TableOption.*;

public class MockModel implements SchemaInfoProvider {

	@Override
	public Schema getSchema() {
		Schema schema = Schema.create("org.teamapps.mock.model");
		schema.setSchemaName("MockSchema");
		Database db = schema.addDatabase("controlCenter");

		Table organizationUnitMock = db.addTable("organizationUnit", KEEP_DELETED, TRACK_CREATION, TRACK_MODIFICATION);
		Table organizationUnitTypeMock = db.addTable("organizationUnitType", KEEP_DELETED, TRACK_CREATION, TRACK_MODIFICATION);
		Table organizationFieldMock = db.addTable("organizationField", KEEP_DELETED, TRACK_CREATION, TRACK_MODIFICATION);
		Table addressMock = db.addTable("address", KEEP_DELETED, TRACK_CREATION, TRACK_MODIFICATION);


		addressMock
				.addText("name")
				.addText("organisation")
				.addText("street")
				.addText("city")
				.addText("dependentLocality")
				.addText("state")
				.addText("postalCode")
				.addText("country")
				.addFloat("latitude")
				.addFloat("longitude")
		;

		organizationUnitMock
				.addTranslatableText("name")
				.addReference("parent", organizationUnitMock, false, "children")
				.addReference("children", organizationUnitMock, true, "parent")
				.addReference("type", organizationUnitTypeMock, false)
				.addText("icon")
				.addReference("address", addressMock, false)
		;

		organizationUnitTypeMock
				.addTranslatableText("name")
				.addTranslatableText("abbreviation")
				.addText("icon")
				.addBoolean("translateOrganizationUnits")
				.addBoolean("allowUserContainer")
				.addReference("defaultChildType", organizationUnitTypeMock, false)
				.addReference("possibleChildrenTypes", organizationUnitTypeMock, true)
				.addEnum("geoLocationType", "country", "state", "city", "place", "none")
		;

		organizationFieldMock
				.addTranslatableText("title")
				.addText("icon")
		;

		return schema;
	}
}
