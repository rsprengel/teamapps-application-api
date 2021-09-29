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
import org.teamapps.udb.filter.TimeIntervalFilter;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RecordListModelBuilder<RECORD> extends RecordModelBuilder<RECORD> {

	private List<RECORD> records = Collections.emptyList();
	private Function<RECORD, String> recordStringFunction;
	private Function<RECORD, Long> recordDateInMillisFunction;

	public RecordListModelBuilder(ApplicationInstanceData applicationInstanceData) {
		super(applicationInstanceData);
	}

	public RecordListModelBuilder(ApplicationInstanceData applicationInstanceData, Function<RECORD, String> recordStringFunction) {
		super(applicationInstanceData);
		this.recordStringFunction = recordStringFunction;
	}

	public RecordListModelBuilder(ApplicationInstanceData applicationInstanceData, Function<RECORD, String> recordStringFunction, Function<RECORD, Long> recordDateInMillisFunction) {
		super(applicationInstanceData);
		this.recordStringFunction = recordStringFunction;
		this.recordDateInMillisFunction = recordDateInMillisFunction;
	}

	public void setRecordStringFunction(Function<RECORD, String> recordStringFunction) {
		this.recordStringFunction = recordStringFunction;
	}

	public void setRecordDateInMillisFunction(Function<RECORD, Long> recordDateInMillisFunction) {
		this.recordDateInMillisFunction = recordDateInMillisFunction;
	}

	public List<RECORD> getRecords() {
		return records;
	}

	public void setRecords(List<RECORD> records) {
		this.records = records;
		onDataChanged.fire();
	}

	public void addRecord(RECORD record) {
		this.records.add(record);
		onDataChanged.fire();
	}

	public void addRecords(List<RECORD> records) {
		this.records.addAll(records);
		onDataChanged.fire();
	}

	public void removeRecord(RECORD record) {
		this.records.remove(record);
		onDataChanged.fire();
	}

	public void removeRecords(List<RECORD> records) {
		this.records.removeAll(records);
		onDataChanged.fire();
	}


	@Override
	public List<RECORD> queryRecords(String fullTextQuery, TimeIntervalFilter timeIntervalFilter) {
		List<RECORD> filteredEntities = null;
		if (recordStringFunction != null && getCustomFullTextFilter() == null && fullTextQuery != null && !fullTextQuery.isBlank()) {
			String query = fullTextQuery.toLowerCase();
			filteredEntities = records.stream().filter(RECORD -> match(recordStringFunction.apply(RECORD), query)).collect(Collectors.toList());
		}
		if (recordDateInMillisFunction != null && timeIntervalFilter != null) {
			if (filteredEntities == null) {
				filteredEntities = records;
			}
			filteredEntities = filteredEntities.stream().filter(RECORD -> match(timeIntervalFilter, recordDateInMillisFunction.apply(RECORD))).collect(Collectors.toList());
		}

		if (filteredEntities == null) {
			filteredEntities = records;
		}

		if (getCustomFullTextFilter() != null && fullTextQuery != null && !fullTextQuery.isBlank()) {
			String query = fullTextQuery.toLowerCase();
			BiFunction<RECORD, String, Boolean> customFullTextFilter = getCustomFullTextFilter();
			filteredEntities = records.stream().filter(RECORD -> customFullTextFilter.apply(RECORD, query)).collect(Collectors.toList());
		}
		return filteredEntities;
	}

	private boolean match(String text, String query) {
		if (text == null) {
			return false;
		}
		return text.toLowerCase().contains(query);
	}

	private boolean match(TimeIntervalFilter intervalFilter, Long date) {
		if (date == null) {
			return false;
		}
		return date >= intervalFilter.getStart() && date <= intervalFilter.getEnd();
	}
}
