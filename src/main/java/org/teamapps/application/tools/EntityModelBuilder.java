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
import org.teamapps.application.api.application.ApplicationInstanceDataMethods;
import org.teamapps.application.api.localization.Dictionary;
import org.teamapps.application.api.theme.ApplicationIcons;
import org.teamapps.application.ux.combo.ComboBoxUtils;
import org.teamapps.data.extract.PropertyProvider;
import org.teamapps.icons.Icon;
import org.teamapps.model.controlcenter.OrganizationUnitView;
import org.teamapps.udb.filter.TimeIntervalFilter;
import org.teamapps.universaldb.index.ColumnIndex;
import org.teamapps.universaldb.index.ColumnType;
import org.teamapps.universaldb.index.IndexType;
import org.teamapps.universaldb.index.TableIndex;
import org.teamapps.universaldb.index.numeric.IntegerIndex;
import org.teamapps.universaldb.index.numeric.LongIndex;
import org.teamapps.universaldb.index.numeric.NumericFilter;
import org.teamapps.universaldb.index.reference.single.SingleReferenceIndex;
import org.teamapps.universaldb.pojo.AbstractUdbEntity;
import org.teamapps.universaldb.pojo.AbstractUdbQuery;
import org.teamapps.universaldb.pojo.Entity;
import org.teamapps.universaldb.pojo.Query;
import org.teamapps.universaldb.record.EntityBuilder;
import org.teamapps.universaldb.schema.Table;
import org.teamapps.ux.component.field.combobox.ComboBox;
import org.teamapps.ux.component.template.BaseTemplate;
import org.teamapps.ux.component.timegraph.TimeGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class EntityModelBuilder<ENTITY extends Entity<ENTITY>> extends RecordModelBuilder<ENTITY> implements ApplicationInstanceDataMethods {

	private final Supplier<Query<ENTITY>> querySupplier;
	private final TableIndex tableIndex;
	private final EntityBuilder<ENTITY> entityBuilder;
	private boolean showDeletedRecords;

	public EntityModelBuilder(Supplier<Query<ENTITY>> querySupplier, ApplicationInstanceData applicationInstanceData) {
		super(applicationInstanceData);
		this.querySupplier = querySupplier;
		AbstractUdbQuery<ENTITY> udbQuery = (AbstractUdbQuery<ENTITY>) querySupplier.get();
		tableIndex = udbQuery.getTableIndex();
		entityBuilder = udbQuery.getEntityBuilder();
	}

	public TableIndex getTableIndex() {
		return tableIndex;
	}

	public EntityBuilder<ENTITY> getEntityBuilder() {
		return entityBuilder;
	}

	@Override
	public List<ENTITY> queryRecords(String fullTextQuery, TimeIntervalFilter timeIntervalFilter) {
		AbstractUdbQuery<ENTITY> query = (AbstractUdbQuery<ENTITY>) querySupplier.get();
		if (timeIntervalFilter != null) {
			NumericFilter numericFilter = tableIndex.getColumnIndex(timeIntervalFilter.getFieldName()).getType() == IndexType.INT ? timeIntervalFilter.getIntFilter() : timeIntervalFilter.getFilter();
			query.addNumericFilter(timeIntervalFilter.getFieldName(), numericFilter);
		}
		if (getCustomFullTextFilter() == null && fullTextQuery != null && !fullTextQuery.isBlank()) {
			query.addFullTextQuery(fullTextQuery);
		}
		List<ENTITY> entities = (getSortField() != null && getCustomFieldSorter() == null) ? query.execute(showDeletedRecords, getSortField(), isSortAscending(), getUser()) : query.execute(showDeletedRecords);
		if (getCustomFullTextFilter() != null && fullTextQuery != null && !fullTextQuery.isBlank()) {
			BiFunction<ENTITY, String, Boolean> customFullTextFilter = getCustomFullTextFilter();
			String fullTextSearchQuery = fullTextQuery.toLowerCase();
			return entities.stream().filter(entity -> customFullTextFilter.apply(entity, fullTextSearchQuery)).collect(Collectors.toList());
		}
		return entities;
	}

	public void setShowDeletedRecords(boolean showDeletedRecords) {
		this.showDeletedRecords = showDeletedRecords;
		onDataChanged.fire();
	}

	public boolean isShowDeletedRecords() {
		return showDeletedRecords;
	}

	public TimeGraph createTimeGraph() {
		return createTimeGraph(createEntityFieldTimeFunction(Table.FIELD_MODIFICATION_DATE), Table.FIELD_MODIFICATION_DATE);
	}

	public ComboBox<String> createTimeGraphFieldSelectionCombobox(TimeGraph timeGraph) {
		Supplier<List<String>> timeFieldsSupplier = () -> tableIndex.getColumnIndices().stream()
				.filter(col -> col.getColumnType() == ColumnType.TIMESTAMP || col.getColumnType() == ColumnType.DATE_TIME)
				.map(ColumnIndex::getName)
				.filter(name -> !Table.FIELD_DELETION_DATE.equals(name) || showDeletedRecords)
				.collect(Collectors.toList());
		PropertyProvider<String> propertyProvider = (s, propertyNames) -> {
			Map<String, Object> map = new HashMap<>();
			map.put(BaseTemplate.PROPERTY_ICON, getFieldIcon(s));
			map.put(BaseTemplate.PROPERTY_CAPTION, getFieldTitle(s));
			return map;
		};
		ComboBox<String> comboBox = ComboBoxUtils.createRecordComboBox(timeFieldsSupplier, propertyProvider, BaseTemplate.LIST_ITEM_SMALL_ICON_SINGLE_LINE);
		comboBox.setDropDownTemplate(BaseTemplate.LIST_ITEM_MEDIUM_ICON_SINGLE_LINE);
		comboBox.onValueChanged.addListener(fieldName -> {
			Function<ENTITY, Long> recordTimeFunction = createEntityFieldTimeFunction(fieldName);
			updateTimeGraphRecordTimeFunction(recordTimeFunction, fieldName, timeGraph);
		});
		comboBox.setValue(Table.FIELD_MODIFICATION_DATE);
		return comboBox;
	}

	private Function<ENTITY, Long> createEntityFieldTimeFunction(String fieldName) {
		ColumnIndex columnIndex = tableIndex.getColumnIndex(fieldName);
		Function<ENTITY, Long> recordTimeFunction;
		if (columnIndex.getColumnType() == ColumnType.DATE_TIME) {
			LongIndex longIndex = (LongIndex) columnIndex;
			recordTimeFunction = entity -> {
				AbstractUdbEntity<ENTITY> udbEntity = (AbstractUdbEntity<ENTITY>) entity;
				long value = udbEntity.getDateTimeAsEpochMilli(longIndex);
				return value == 0 ? null : value;
			};
		} else {
			IntegerIndex integerIndex = (IntegerIndex) columnIndex;
			recordTimeFunction = entity -> {
				AbstractUdbEntity<ENTITY> udbEntity = (AbstractUdbEntity<ENTITY>) entity;
				long value = udbEntity.getTimestampAsEpochMilli(integerIndex);
				return value == 0 ? null : value;
			};
		}
		return recordTimeFunction;
	}

	private String getFieldTitle(String fieldName) {
		return switch (fieldName) {
			case Table.FIELD_CREATION_DATE -> getLocalized(Dictionary.CREATION_DATE);
			case Table.FIELD_MODIFICATION_DATE -> getLocalized(Dictionary.MODIFICATION_DATE);
			case Table.FIELD_DELETION_DATE -> getLocalized(Dictionary.DELETION_DATE);
			case Table.FIELD_RESTORE_DATE -> getLocalized(Dictionary.RESTORE_DATE);
			default -> fieldName;
		};
	}

	private Icon<?, ?> getFieldIcon(String fieldName) {
		return switch (fieldName) {
			case Table.FIELD_CREATION_DATE -> ApplicationIcons.ADD;
			case Table.FIELD_MODIFICATION_DATE -> ApplicationIcons.EDIT;
			case Table.FIELD_DELETION_DATE -> ApplicationIcons.GARBAGE_EMPTY;
			case Table.FIELD_RESTORE_DATE -> ApplicationIcons.GARBAGE_MAKE_EMPTY;
			default -> ApplicationIcons.CALENDAR;
		};
	}

	public Function<ENTITY, OrganizationUnitView> createEntityOrganizationUnitViewFunction() {
		SingleReferenceIndex referenceIndex = null;
		for (ColumnIndex columnIndex : tableIndex.getColumnIndices()) {
			if (columnIndex.getColumnType() == ColumnType.SINGLE_REFERENCE) {
				SingleReferenceIndex singleReferenceIndex = (SingleReferenceIndex) columnIndex;
				String fqn = singleReferenceIndex.getReferencedTable().getFQN();
				if (fqn.equals("controlCenter.organizationUnitView") || fqn.equals("controlCenter.organizationUnit")) {
					referenceIndex = singleReferenceIndex;
					break;
				}
			}
		}
		final SingleReferenceIndex index = referenceIndex;
		if (index == null) {
			return null;
		} else {
			return entity -> {
				if (entity == null) {
					return null;
				} else {
					int id = index.getValue(entity.getId());
					return id == 0 ? null : OrganizationUnitView.getById(id);
				}
			};
		}
	}


}
