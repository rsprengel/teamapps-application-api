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
package org.teamapps.application.api.localization;

import com.ibm.icu.text.Transliterator;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DictionaryBuilder {

	private static final String DICTIONARY_PREFIX = "org.teamapps.dictionary.";
	private static Transliterator TRANSLITERATOR = Transliterator.getInstance("Any-Latin; nfd; [:nonspacing mark:] remove; nfc");

	private static final String HEADER = "/*-\n" +
			" * ========================LICENSE_START=================================\n" +
			" * TeamApps Application API\n" +
			" * ---\n" +
			" * Copyright (C) 2020 - 2021 TeamApps.org\n" +
			" * ---\n" +
			" * Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
			" * you may not use this file except in compliance with the License.\n" +
			" * You may obtain a copy of the License at\n" +
			" * \n" +
			" *      http://www.apache.org/licenses/LICENSE-2.0\n" +
			" * \n" +
			" * Unless required by applicable law or agreed to in writing, software\n" +
			" * distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
			" * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
			" * See the License for the specific language governing permissions and\n" +
			" * limitations under the License.\n" +
			" * =========================LICENSE_END==================================\n" +
			" */";

	private static final String RESOURCE_HEADER = "###\n" +
			"# ========================LICENSE_START=================================\n" +
			"# TeamApps Application API\n" +
			"# ---\n" +
			"# Copyright (C) 2020 - 2021 TeamApps.org\n" +
			"# ---\n" +
			"# Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
			"# you may not use this file except in compliance with the License.\n" +
			"# You may obtain a copy of the License at\n" +
			"# \n" +
			"#      http://www.apache.org/licenses/LICENSE-2.0\n" +
			"# \n" +
			"# Unless required by applicable law or agreed to in writing, software\n" +
			"# distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
			"# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
			"# See the License for the specific language governing permissions and\n" +
			"# limitations under the License.\n" +
			"# =========================LICENSE_END==================================\n" +
			"###";

	private static final String DICTIONARY_HEADER = "package org.teamapps.application.api.localization;\n" +
			"\n" +
			"public class Dictionary {";
	private static final String DICTIONARY_FOOTER = "\n\n}";


	public static void main(String[] args) throws IOException {
		if (args == null || args.length < 2) {
			System.err.println("Error missing paths!");
			return;
		}
		//createCountryAndLanguageLines();
		buildDictionaryData(args[0], args[1]);
	}

	private static void buildDictionaryData(String path, String resourcePath) throws IOException {
		List<String> values = readData().stream()
				.filter(s -> s != null && !s.isBlank())
				.collect(Collectors.toList());;
		List<String> resourceLines = new ArrayList<>();
		List<String> codeLines = new ArrayList<>();
		for (String value : values) {
			if (value.startsWith("::")) {
				codeLines.add("//" + value.substring(2));
				resourceLines.add("# " + value.substring(2));
			} else if (value.contains("=")) {
				int pos = value.indexOf('=');
				String k = value.substring(0, pos);
				String v = value.substring(pos + 1);
				String key = DICTIONARY_PREFIX + k;
				resourceLines.add(key + "=" + v);
				codeLines.add("public static final String " + k.toUpperCase().replaceAll("\\.", "_") + " = \"" + key + "\";");
			} else {
				resourceLines.add(createKey(value) + "=" + value);
				codeLines.add("public static final String " + createConstant(value) + " = \"" + createKey(value) + "\";");
			}
		}

		File dictionary = new File(path, "Dictionary.java");
		File resource = new File(resourcePath, "dictionary_en.properties");
		if (!dictionary.exists() || !resource.exists()) {
			System.err.println("Error paths do not exist!");
			return;
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(dictionary, StandardCharsets.UTF_8));
		writer.write(HEADER);
		writer.newLine();
		writer.write(DICTIONARY_HEADER);
		writer.newLine();
		for (String codeLine : codeLines) {
			writer.write("\t" + codeLine);
			writer.newLine();
		}
		writer.write(DICTIONARY_FOOTER);
		writer.close();

		writer = new BufferedWriter(new FileWriter(resource, StandardCharsets.UTF_8));
		writer.write(RESOURCE_HEADER);
		writer.newLine();
		for (String codeLine : resourceLines) {
			writer.write(codeLine);
			writer.newLine();
		}
		writer.close();
		System.out.println("Done");
	}

	private static void createCountryAndLanguageLines() {
		for (Country value : Country.values()) {
			System.out.println("country." + value.name() + "=" + value.getValue());
		}
		for (Language value : Language.values()) {
			System.out.println("language." + value.name() + "=" + value.getValue());
		}
	}

	private static String cleanValue(String value) {
		value = TRANSLITERATOR.transliterate(value);
		value = WordUtils.capitalize(value);
		value = value.replaceAll("\\.\\.\\.", "___");
		value = removeNonAnsi(value);
		value = value.replaceAll(" ", "");
		value = value.substring(0, 1).toLowerCase() + value.substring(1);
		return value;
	}

	private static String createKey(String value) {
		return DICTIONARY_PREFIX + cleanValue(value);
	}

	private static String createConstant(String value) {
		value = cleanValue(value);
		value = value.replaceAll("(.)(\\p{Upper})", "$1_$2").toUpperCase();
		if (value.length() > 36) {
			value = "SENTENCE_" + value.substring(0, 35) + "__";
		}
		return value;
	}

	public static String removeNonAnsi(String s) {
		if (s == null) return null;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int code = c;
			if (c == '_' || c == ' ') {
				sb.append(c);
			} else if (code > 64 && code < 91) {
				sb.append(c);
			} else if (code > 96 && code < 123) {
				sb.append(c);
			} else if (code > 47 && code < 58) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	private static List<String> readData() throws IOException {
		InputStream resource = DictionaryBuilder.class.getResourceAsStream("dictionary-data.txt");
		return IOUtils.readLines(resource, StandardCharsets.UTF_8);
	}


}



