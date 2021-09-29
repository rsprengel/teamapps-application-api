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
package org.teamapps.application.ux.org;

import org.teamapps.application.api.application.ApplicationInstanceData;
import org.teamapps.application.ux.IconUtils;
import org.teamapps.application.ux.localize.TranslatableTextUtils;
import org.teamapps.data.extract.PropertyProvider;
import org.teamapps.model.controlcenter.GeoLocationType;
import org.teamapps.model.controlcenter.OrganizationFieldView;
import org.teamapps.model.controlcenter.OrganizationUnitTypeView;
import org.teamapps.model.controlcenter.OrganizationUnitView;
import org.teamapps.universaldb.index.translation.TranslatableText;
import org.teamapps.ux.component.field.combobox.ComboBox;
import org.teamapps.ux.component.field.combobox.TagBoxWrappingMode;
import org.teamapps.ux.component.field.combobox.TagComboBox;
import org.teamapps.ux.component.template.BaseTemplate;
import org.teamapps.ux.component.template.Template;
import org.teamapps.ux.component.tree.TreeNodeInfo;
import org.teamapps.ux.component.tree.TreeNodeInfoImpl;
import org.teamapps.ux.model.ComboBoxModel;
import org.teamapps.ux.model.ListTreeModel;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class OrganizationViewUtils {

	public static Set<OrganizationUnitView> getAllUnits(OrganizationUnitView unit, Collection<OrganizationUnitTypeView> unitTypesFilter) {
		Set<OrganizationUnitView> result = new HashSet<>();
		Set<OrganizationUnitView> traversedNodes = new HashSet<>();
		Set<OrganizationUnitTypeView> filter = unitTypesFilter != null && !unitTypesFilter.isEmpty() ? new HashSet<>(unitTypesFilter) : null;
		calculateAllUnits(unit, filter, traversedNodes, result);
		return result;
	}

	private static void calculateAllUnits(OrganizationUnitView unit, Set<OrganizationUnitTypeView> unitTypesFilter, Set<OrganizationUnitView> traversedNodes, Set<OrganizationUnitView> result) {
		if (unitTypesFilter == null || unitTypesFilter.contains(unit.getType())) {
			result.add(unit);
		}
		for (OrganizationUnitView child : unit.getChildren()) {
			if (!traversedNodes.contains(child)) {
				traversedNodes.add(child);
				calculateAllUnits(child, unitTypesFilter, traversedNodes, result);
			}
		}
	}

	public static ComboBox<OrganizationFieldView> createOrganizationFieldCombo(ApplicationInstanceData applicationInstanceData) {
		ComboBox<OrganizationFieldView> comboBox = new ComboBox<>(BaseTemplate.LIST_ITEM_SMALL_ICON_SINGLE_LINE);
		comboBox.setModel(new ListTreeModel<>(OrganizationFieldView.getAll()));
		comboBox.setPropertyProvider(createOrganizationFieldViewPropertyProvider(applicationInstanceData));
		return comboBox;
	}

	public static int getOrgLevel(OrganizationUnitView unit) {
		int level = 0;
		OrganizationUnitView parent = unit.getParent();
		while (parent != null) {
			level++;
			parent = parent.getParent();
		}
		return level;
	}

	public static OrganizationUnitView getParentWithGeoType(OrganizationUnitView unit, GeoLocationType type) {
		if (unit == null) {
			return null;
		}
		OrganizationUnitView parent = unit.getParent();
		while (parent != null) {
			if (parent.getType().getGeoLocationType() == type) {
				return parent;
			}
			parent = parent.getParent();
		}
		return null;
	}

	public static ComboBox<OrganizationUnitView> createOrganizationComboBox(Template template, Collection<OrganizationUnitView> allowedUnits, ApplicationInstanceData applicationInstanceData) {
		return createOrganizationComboBox(template, () -> allowedUnits, applicationInstanceData);
	}

	public static ComboBox<OrganizationUnitView> createOrganizationComboBox(Template template, Supplier<Collection<OrganizationUnitView>> allowedUnitsSupplier, ApplicationInstanceData applicationInstanceData) {
		return createOrganizationComboBox(template, allowedUnitsSupplier, null, applicationInstanceData);
	}

	public static ComboBox<OrganizationUnitView> createOrganizationComboBox(Template template, Supplier<Collection<OrganizationUnitView>> allowedUnitsSupplier, Set<OrganizationUnitTypeView> selectableTypes, ApplicationInstanceData applicationInstanceData) {
		ComboBox<OrganizationUnitView> comboBox = new ComboBox<>(template);
		//comboBox.setDropDownTemplate(BaseTemplate.LIST_ITEM_MEDIUM_ICON_TWO_LINES);
		ComboBoxModel<OrganizationUnitView> model = createLazyOrgUnitModel(allowedUnitsSupplier, selectableTypes);
		comboBox.setModel(model);
		comboBox.setShowExpanders(true);
		PropertyProvider<OrganizationUnitView> propertyProvider = creatOrganizationUnitViewPropertyProvider(applicationInstanceData);
		comboBox.setPropertyProvider(propertyProvider);
		Function<OrganizationUnitView, String> recordToStringFunction = unit -> {
			Map<String, Object> values = propertyProvider.getValues(unit, Collections.singleton(BaseTemplate.PROPERTY_CAPTION));
			Object result = values.get(BaseTemplate.PROPERTY_CAPTION);
			return (String) result;
		};
		comboBox.setRecordToStringFunction(recordToStringFunction);
		return comboBox;
	}

	public static ComboBoxModel<OrganizationUnitView> createLazyOrgUnitModel(Supplier<Collection<OrganizationUnitView>> allowedUnitsSupplier, Set<OrganizationUnitTypeView> selectableTypes) {
		ComboBoxModel<OrganizationUnitView> model = new ComboBoxModel<>() {
			@Override
			public List<OrganizationUnitView> getRecords(String query) {
				Collection<OrganizationUnitView> nodes = allowedUnitsSupplier.get();
				return query == null || query.isBlank() ?
						getRootNodes(nodes) :
						getRootNodes(
								OrganizationUnitView.filter()
										.parseFullTextFilter(query)
										.execute()
										.stream()
										.filter(nodes::contains)
										.limit(250)
										.collect(Collectors.toList())
						);
			}

			@Override
			public List<OrganizationUnitView> getChildRecords(OrganizationUnitView unit) {
				Collection<OrganizationUnitView> organizationUnitViews = allowedUnitsSupplier.get();
				return unit.getChildren().stream().filter(organizationUnitViews::contains).collect(Collectors.toList());
			}

			@Override
			public TreeNodeInfo getTreeNodeInfo(OrganizationUnitView unit) {
				return new TreeNodeInfoImpl<>(unit.getParent(), false, selectableTypes == null || selectableTypes.contains(unit.getType()), unit.getChildrenCount() > 0);
			}
		};
		return model;
	}

	private static List<OrganizationUnitView> getRootNodes(Collection<OrganizationUnitView> nodes) {
		Set<OrganizationUnitView> set = nodes instanceof Set ? (Set<OrganizationUnitView>) nodes : new HashSet<>(nodes);
		return nodes.stream()
				.filter(node -> node.getParent() == null || !set.contains(node.getParent()))
				.collect(Collectors.toList());
	}

	public static TagComboBox<OrganizationUnitTypeView> createOrganizationUnitTypeTagComboBox(int limit, ApplicationInstanceData applicationInstanceData) {
		TagComboBox<OrganizationUnitTypeView> tagComboBox = new TagComboBox<>(BaseTemplate.LIST_ITEM_SMALL_ICON_SINGLE_LINE);
		tagComboBox.setModel(query -> query == null || query.isBlank() ?
				OrganizationUnitTypeView.getAll().stream().limit(limit).collect(Collectors.toList()) :
				OrganizationUnitTypeView.filter().parseFullTextFilter(query).execute().stream().limit(limit).collect(Collectors.toList())
		);
		PropertyProvider<OrganizationUnitTypeView> propertyProvider = creatOrganizationUnitTypeViewPropertyProvider(applicationInstanceData);
		tagComboBox.setPropertyProvider(propertyProvider);
		tagComboBox.setRecordToStringFunction(unitType -> (String) propertyProvider.getValues(unitType, Collections.emptyList()).get(BaseTemplate.PROPERTY_CAPTION));
		tagComboBox.setWrappingMode(TagBoxWrappingMode.SINGLE_TAG_PER_LINE);
		tagComboBox.setDistinct(true);
		return tagComboBox;
	}

	public static PropertyProvider<OrganizationUnitView> creatOrganizationUnitViewPropertyProvider(ApplicationInstanceData applicationInstanceData) {
		Function<TranslatableText, String> translatableTextExtractor = TranslatableTextUtils.createTranslatableTextExtractor(applicationInstanceData);
		return (unit, propertyNames) -> {
			String prefix = "";
			String abbreviation = translatableTextExtractor.apply(unit.getType().getAbbreviation());
			if (abbreviation != null) {
				prefix = abbreviation + "-";
			}
			Map<String, Object> map = new HashMap<>();
			map.put(BaseTemplate.PROPERTY_ICON, unit.getIcon() != null ? IconUtils.decodeIcon(unit.getIcon()) : IconUtils.decodeIcon(unit.getType().getIcon()));
			map.put(BaseTemplate.PROPERTY_CAPTION, prefix + translatableTextExtractor.apply(unit.getName()));
			map.put(BaseTemplate.PROPERTY_DESCRIPTION, translatableTextExtractor.apply(unit.getType().getName()));
			return map;
		};
	}


	public static PropertyProvider<OrganizationUnitTypeView> creatOrganizationUnitTypeViewPropertyProvider(ApplicationInstanceData applicationInstanceData) {
		Function<TranslatableText, String> translatableTextExtractor = TranslatableTextUtils.createTranslatableTextExtractor(applicationInstanceData);
		return (unitType, propertyNames) -> {
			Map<String, Object> map = new HashMap<>();
			map.put(BaseTemplate.PROPERTY_ICON, IconUtils.decodeIcon(unitType.getIcon()));
			map.put(BaseTemplate.PROPERTY_CAPTION, translatableTextExtractor.apply(unitType.getName()));
			map.put(BaseTemplate.PROPERTY_DESCRIPTION, translatableTextExtractor.apply(unitType.getAbbreviation()));
			return map;
		};
	}

	public static PropertyProvider<OrganizationFieldView> createOrganizationFieldViewPropertyProvider(ApplicationInstanceData applicationInstanceData) {
		return (organizationFieldView, propertyNames) -> {
			Map<String, Object> map = new HashMap<>();
			map.put(BaseTemplate.PROPERTY_ICON, IconUtils.decodeIcon(organizationFieldView.getIcon()));
			map.put(BaseTemplate.PROPERTY_CAPTION, applicationInstanceData.getLocalized(organizationFieldView.getTitle()));
			return map;
		};
	}

}
