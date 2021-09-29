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

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChangeCounter {

	private long time = System.currentTimeMillis();
	private Map<String, Integer> createCountMap = new HashMap<>();
	private Map<String, Integer> updateCountMap = new HashMap<>();
	private Map<String, Integer> deleteCountMap = new HashMap<>();
	private Map<String, Integer> errorCountMap = new HashMap<>();

	private String defaultRecord = "records";

	public ChangeCounter() {
	}

	public ChangeCounter(String defaultRecord) {
		this.defaultRecord = defaultRecord;
	}

	public void updateOrCreate(boolean update) {
		updateOrCreate(defaultRecord, update);
	}

	public void updateOrCreate(String name, boolean update) {
		if (update) {
			update(name);
		} else {
			create(name);
		}
	}

	public void update() {
		update(defaultRecord);
	}

	public void update(String name) {
		updateCountMap.compute(name, (k, v) -> (v == null) ? 1 : v + 1);
	}

	public void create() {
		create(defaultRecord);
	}

	public void create(String name) {
		createCountMap.compute(name, (k, v) -> (v == null) ? 1 : v + 1);
	}

	public void delete() {
		delete(defaultRecord);
	}

	public void delete(String name) {
		deleteCountMap.compute(name, (k, v) -> (v == null) ? 1 : v + 1);
	}

	public void error() {
		error(defaultRecord);
	}

	public void error(String name) {
		errorCountMap.compute(name, (k, v) -> (v == null) ? 1 : v + 1);
	}

	public int getUpdated() {
		return updateCountMap.getOrDefault(defaultRecord, 0);
	}
	
	public int getCreated() {
		return createCountMap.getOrDefault(defaultRecord, 0);
	}

	public int getDeleted() {
		return createCountMap.getOrDefault(defaultRecord, 0);
	}

	public int getError() {
		return createCountMap.getOrDefault(defaultRecord, 0);
	}

	public int getUpdated(String key) {
		return updateCountMap.getOrDefault(key, 0);
	}

	public int getCreated(String key) {
		return createCountMap.getOrDefault(key, 0);
	}

	public int getDeleted(String key) {
		return createCountMap.getOrDefault(key, 0);
	}

	public int getError(String key) {
		return createCountMap.getOrDefault(key, 0);
	}

	public String getDurationAsString() {
		return Duration.ofMillis(System.currentTimeMillis() - time).toString().substring(2);
	}

	public int getDuration() {
		return (int) (System.currentTimeMillis() - time);
	}

	public String getResults() {
		return getTime() + ", " + getKeys().stream()
				.map(this::getResult)
				.collect(Collectors.joining(", "));
	}

	public String getResults(String delimiter) {
		return getTime() + delimiter + getKeys().stream()
				.map(this::getResult)
				.collect(Collectors.joining(delimiter));
	}

	public String getCompactResults() {
		return getTime() + ", " + getKeys().stream()
				.map(this::getCompactResult)
				.collect(Collectors.joining(", "));
	}

	private String getCompactResult(String key) {
		return key + "(" +
				"+" + createCountMap.getOrDefault(key,0) + ", " +
				"-" + deleteCountMap.getOrDefault(key,0) + ", " +
				"*" + updateCountMap.getOrDefault(key,0) +
				(errorCountMap.get(key) != null ? ", ⚠" + errorCountMap.get(key) : "") +
				")";
	}

	private String getResult(String key) {
		return key + "(" +
				"added: " + createCountMap.getOrDefault(key,0) + ", " +
				"removed: " + deleteCountMap.getOrDefault(key,0) + ", " +
				"changed: " + updateCountMap.getOrDefault(key,0) +
				(errorCountMap.get(key) != null ? ", errors: " + errorCountMap.get(key) : "") +
				")";
	}

	private String getTime() {
		return "time: " + (System.currentTimeMillis() - time);
	}

	public List<String> getKeys() {
		HashSet<String> set = new HashSet<>();
		set.addAll(createCountMap.keySet());
		set.addAll(updateCountMap.keySet());
		set.addAll(deleteCountMap.keySet());
		set.addAll(errorCountMap.keySet());
		return set.stream().sorted().collect(Collectors.toList());
	}
}
