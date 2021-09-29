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
package org.teamapps.application.tools;

import org.teamapps.application.api.application.ApplicationInstanceData;
import org.teamapps.application.api.privilege.OrganizationalPrivilegeGroup;
import org.teamapps.application.api.privilege.Privilege;
import org.teamapps.model.controlcenter.OrganizationUnitView;
import org.teamapps.universaldb.index.numeric.NumericFilter;
import org.teamapps.universaldb.index.reference.single.SingleReferenceIndex;
import org.teamapps.universaldb.index.reference.value.RecordReference;
import org.teamapps.universaldb.pojo.AbstractUdbQuery;
import org.teamapps.universaldb.pojo.Entity;
import org.teamapps.universaldb.pojo.Query;
import org.teamapps.universaldb.query.IndexFilter;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PrivilegeUtils {

	public static <ENTITY extends Entity<ENTITY>> void createOrgUnitFilter(Query<ENTITY> query, String orgUnitFieldName, OrganizationalPrivilegeGroup privilegeGroup, Privilege privilege, ApplicationInstanceData applicationInstanceData) {
		AbstractUdbQuery<ENTITY> abstractUdbQuery = (AbstractUdbQuery<ENTITY>) query;
		SingleReferenceIndex singleReferenceIndex = (SingleReferenceIndex) abstractUdbQuery.getTableIndex().getColumnIndex(orgUnitFieldName);

		List<OrganizationUnitView> allowedUnits = applicationInstanceData.getAllowedUnits(privilegeGroup, privilege);
		List<Number> allowedUnitIds = allowedUnits.stream().map(unit -> (Number) unit.getId()).collect(Collectors.toList());
		IndexFilter<RecordReference, NumericFilter> filter = singleReferenceIndex.createFilter(NumericFilter.containsFilter(allowedUnitIds));
		abstractUdbQuery.and(filter);
	}

	public static <ENTITY extends Entity<ENTITY>> Supplier<Query<ENTITY>> createQueryOrgUnitFilter(Supplier<Query<ENTITY>> querySupplier, String orgUnitFieldName, OrganizationalPrivilegeGroup privilegeGroup, Privilege privilege, ApplicationInstanceData applicationInstanceData) {
		return () -> {
			Query<ENTITY> entityQuery = querySupplier.get();
			createOrgUnitFilter(entityQuery, orgUnitFieldName, privilegeGroup, privilege, applicationInstanceData);
			return entityQuery;
		};
	}
}
