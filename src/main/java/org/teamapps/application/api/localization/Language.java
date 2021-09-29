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

import org.teamapps.application.api.application.ApplicationInstanceData;
import org.teamapps.data.extract.PropertyExtractor;
import org.teamapps.icon.flags.FlagIcon;
import org.teamapps.icons.Icon;
import org.teamapps.ux.component.field.combobox.ComboBox;
import org.teamapps.ux.component.field.combobox.TagComboBox;
import org.teamapps.ux.component.template.BaseTemplate;
import org.teamapps.ux.model.ComboBoxModel;

import java.util.*;
import java.util.stream.Collectors;

public enum Language {

	AA_AFAR("aa", "Afar", FlagIcon.ER_ERITREA),
	AF_AFRIKAANS("af", "Afrikaans", FlagIcon.ZA_SOUTH_AFRICA),
	AK_AKAN("ak", "Akan", FlagIcon.GH_GHANA),
	AM_AMHARIC("am", "Amharic", FlagIcon.ET_ETHIOPIA),
	AN_ARAGONESE("an", "Aragonese", FlagIcon.ES_SPAIN),
	AR_ARABIC("ar", "Arabic", FlagIcon.EG_EGYPT),
	AS_ASSAMESE("as", "Assamese", FlagIcon.IN_INDIA),
	AV_AVARIC("av", "Avaric", null),
	AY_AYMARA("ay", "Aymara", null),
	AZ_AZERBAIJANI("az", "Azerbaijani", FlagIcon.AZ_AZERBAIJAN),
	BA_BASHKIR("ba", "Bashkir", null),
	BE_BELARUSIAN("be", "Belarusian", null),
	BG_BULGARIAN("bg", "Bulgarian", FlagIcon.BG_BULGARIA),
	BH_BIHARI("bh", "Bihari", null),
	BI_BISLAMA("bi", "Bislama", FlagIcon.VU_VANUATU),
	BM_BAMBARA("bm", "Bambara", FlagIcon.ML_MALI),
	BN_BENGALI("bn", "Bengali", FlagIcon.BD_BANGLADESH),
	BO_TIBETAN("bo", "Tibetan", null),
	BR_BRETON("br", "Breton", null),
	BS_BOSNIAN("bs", "Bosnian", FlagIcon.BA_BOSNIA),
	CA_CATALAN("ca", "Catalan", FlagIcon.AD_ANDORRA),
	CE_CHECHEN("ce", "Chechen", null),
	CH_CHAMORRO("ch", "Chamorro", null),
	CO_CORSICAN("co", "Corsican", null),
	CR_CREE("cr", "Cree", null),
	CS_CZECH("cs", "Czech", FlagIcon.CZ_CZECHIA),
	CV_CHUVASH("cv", "Chuvash", null),
	CY_WELSH("cy", "Welsh", null),
	DA_DANISH("da", "Danish", FlagIcon.DK_DENMARK),
	DE_GERMAN("de", "German", FlagIcon.DE_GERMANY),
	DV_DHIVEHI_MALDIVIAN("dv", "Dhivehi; Maldivian", FlagIcon.MV_MALDIVES),
	DZ_DZONGKHA("dz", "Dzongkha", FlagIcon.BT_BHUTAN),
	EE_EWE("ee", "Ewe", null),
	EL_GREEK("el", "Greek", FlagIcon.GR_GREECE),
	EN_ENGLISH("en", "English", FlagIcon.GB_GREAT_BRITAIN),
	EO_ESPERANTO("eo", "Esperanto", FlagIcon.EU_EUROPEAN_UNION),
	ES_SPANISH("es", "Spanish", FlagIcon.ES_SPAIN),
	ET_ESTONIAN("et", "Estonian", FlagIcon.EE_ESTONIA),
	EU_BASQUE("eu", "Basque", FlagIcon.ES_SPAIN),
	FA_PERSIAN("fa", "Persian", FlagIcon.IR_IRAN),
	FF_FULAH("ff", "Fulah", null),
	FI_FINNISH("fi", "Finnish", FlagIcon.FI_FINLAND),
	FJ_FIJIAN("fj", "Fijian", FlagIcon.FJ_FIJI),
	FO_FAROESE("fo", "Faroese", FlagIcon.FO_FAROE_ISLANDS),
	FR_FRENCH("fr", "French", FlagIcon.FR_FRANCE),
	FY_WESTERN_FRISIAN("fy", "Western Frisian", null),
	GA_IRISH("ga", "Irish", FlagIcon.IE_IRELAND),
	GD_GAELIC("gd", "Gaelic", FlagIcon.IE_IRELAND),
	GL_GALICIAN("gl", "Galician", null),
	GN_GUARANI("gn", "Guarani", FlagIcon.PY_PARAGUAY),
	GU_GUJARATI("gu", "Gujarati", FlagIcon.IN_INDIA),
	GV_MANX("gv", "Manx", null),
	HA_HAUSA("ha", "Hausa", null),
	HE_HEBREW("he", "Hebrew", FlagIcon.IL_ISRAEL),
	HI_HINDI("hi", "Hindi", FlagIcon.IN_INDIA),
	HO_HIRI_MOTU("ho", "Hiri Motu", FlagIcon.PG_PAPUA_NEW_GUINEA),
	HR_CROATIAN("hr", "Croatian", FlagIcon.HR_CROATIA),
	HT_HAITIAN_HAITIAN_CREOLE("ht", "Haitian; Haitian Creole", FlagIcon.HT_HAITI),
	HU_HUNGARIAN("hu", "Hungarian", FlagIcon.HU_HUNGARY),
	HY_ARMENIAN("hy", "Armenian", FlagIcon.AM_ARMENIA),
	HZ_HERERO("hz", "Herero", null),
	ID_INDONESIAN("id", "Indonesian", FlagIcon.ID_INDONESIA),
	IG_IGBO("ig", "Igbo", FlagIcon.NG_NIGERIA),
	II_SICHUAN_YI_NUOSU("ii", "Sichuan Yi; Nuosu", null),
	IK_INUPIAQ("ik", "Inupiaq", null),
	IO_IDO("io", "Ido", null),
	IS_ICELANDIC("is", "Icelandic", FlagIcon.IS_ICELAND),
	IT_ITALIAN("it", "Italian", FlagIcon.IT_ITALY),
	IU_INUKTITUT("iu", "Inuktitut", null),
	JA_JAPANESE("ja", "Japanese", FlagIcon.JP_JAPAN),
	JV_JAVANESE("jv", "Javanese", null),
	KA_GEORGIAN("ka", "Georgian", FlagIcon.GE_GEORGIA),
	KG_KONGO("kg", "Kongo", FlagIcon.CG_CONGO__BRAZZAVILLE),
	KI_KIKUYU_GIKUYU("ki", "Kikuyu; Gikuyu", null),
	KJ_KUANYAMA_KWANYAMA("kj", "Kuanyama; Kwanyama", null),
	KK_KAZAKH("kk", "Kazakh", null),
	KL_KALAALLISUT_GREENLANDIC("kl", "Kalaallisut; Greenlandic", FlagIcon.GL_GREENLAND),
	KM_CENTRAL_KHMER("km", "Central Khmer", FlagIcon.KH_CAMBODIA),
	KN_KANNADA("kn", "Kannada", null),
	KO_KOREAN("ko", "Korean", FlagIcon.KR_SOUTH_KOREA),
	KR_KANURI("kr", "Kanuri", null),
	KS_KASHMIRI("ks", "Kashmiri", null),
	KU_KURDISH("ku", "Kurdish", FlagIcon.TR_TURKEY),
	KV_KOMI("kv", "Komi", null),
	KW_CORNISH("kw", "Cornish", null),
	KY_KIRGHIZ_KYRGYZ("ky", "Kirghiz; Kyrgyz", FlagIcon.KG_KYRGYZSTAN),
	LG_GANDA("lg", "Ganda", null),
	LN_LINGALA("ln", "Lingala", null),
	LO_LAO("lo", "Lao", FlagIcon.LA_LAOS),
	LT_LITHUANIAN("lt", "Lithuanian", FlagIcon.LT_LITHUANIA),
	LU_LUBA_KATANGA("lu", "Luba-Katanga", FlagIcon.LU_LUXEMBOURG),
	LV_LATVIAN("lv", "Latvian", FlagIcon.LV_LATVIA),
	MG_MALAGASY("mg", "Malagasy", FlagIcon.MG_MADAGASCAR),
	MH_MARSHALLESE("mh", "Marshallese", FlagIcon.MH_MARSHALL_ISLANDS),
	MI_MAORI("mi", "Maori", null),
	MK_MACEDONIAN("mk", "Macedonian", FlagIcon.MK_MACEDONIA),
	ML_MALAYALAM("ml", "Malayalam", FlagIcon.ML_MALI),
	MN_MONGOLIAN("mn", "Mongolian", FlagIcon.MN_MONGOLIA),
	MR_MARATHI("mr", "Marathi", FlagIcon.IN_INDIA),
	MS_MALAY("ms", "Malay", FlagIcon.MY_MALAYSIA),
	MT_MALTESE("mt", "Maltese", FlagIcon.MT_MALTA),
	MY_BURMESE("my", "Burmese", FlagIcon.MM_MYANMAR),
	NA_NAURU("na", "Nauru", FlagIcon.NR_NAURU),
	ND_NDEBELE_NORTH("nd", "Ndebele, North", null),
	NE_NEPALI("ne", "Nepali", FlagIcon.NP_NEPAL),
	NG_NDONGA("ng", "Ndonga", null),
	NL_DUTCH("nl", "Dutch", FlagIcon.NL_NETHERLANDS),
	NO_NORWEGIAN("no", "Norwegian", FlagIcon.NO_NORWAY),
	NR_NDEBELE_SOUTH("nr", "Ndebele, South", FlagIcon.NR_NAURU),
	NV_NAVAJO_NAVAHO("nv", "Navajo; Navaho", null),
	NY_CHICHEWA_CHEWA_NYANJA("ny", "Chichewa; Chewa; Nyanja", FlagIcon.MW_MALAWI),
	OC_OCCITAN("oc", "Occitan", null),
	OJ_OJIBWA("oj", "Ojibwa", null),
	OM_OROMO("om", "Oromo", null),
	OR_ORIYA("or", "Oriya", null),
	OS_OSSETIAN_OSSETIC("os", "Ossetian; Ossetic", null),
	PA_PANJABI_PUNJABI("pa", "Panjabi; Punjabi", FlagIcon.PK_PAKISTAN),
	PI_PALI("pi", "Pali", null),
	PL_POLISH("pl", "Polish", FlagIcon.PL_POLAND),
	PS_PUSHTO_PASHTO("ps", "Pushto; Pashto", FlagIcon.AF_AFGHANISTAN),
	PT_PORTUGUESE("pt", "Portuguese", FlagIcon.PT_PORTUGAL),
	QU_QUECHUA("qu", "Quechua", null),
	RM_ROMANSH("rm", "Romansh", null),
	RN_RUNDI("rn", "Rundi", FlagIcon.BI_BURUNDI),
	RO_ROMANIAN("ro", "Romanian", FlagIcon.RO_ROMANIA),
	RU_RUSSIAN("ru", "Russian", FlagIcon.RU_RUSSIA),
	RW_KINYARWANDA("rw", "Kinyarwanda", FlagIcon.RW_RWANDA),
	SA_SANSKRIT("sa", "Sanskrit", FlagIcon.IN_INDIA),
	SC_SARDINIAN("sc", "Sardinian", FlagIcon.IT_ITALY),
	SD_SINDHI("sd", "Sindhi", null),
	SE_NORTHERN_SAMI("se", "Northern Sami", null),
	SG_SANGO("sg", "Sango", FlagIcon.CF_CENTRAL_AFRICAN_REPUBLIC),
	SI_SINHALA_SINHALESE("si", "Sinhala; Sinhalese", FlagIcon.LK_SRI_LANKA),
	SK_SLOVAK("sk", "Slovak", FlagIcon.SK_SLOVAKIA),
	SL_SLOVENIAN("sl", "Slovenian", FlagIcon.SI_SLOVENIA),
	SM_SAMOAN("sm", "Samoan", FlagIcon.AS_AMERICAN_SAMOA),
	SN_SHONA("sn", "Shona", null),
	SO_SOMALI("so", "Somali", FlagIcon.SO_SOMALIA),
	SQ_ALBANIAN("sq", "Albanian", FlagIcon.AL_ALBANIA),
	SR_SERBIAN("sr", "Serbian", FlagIcon.RS_SERBIA),
	SS_SWATI("ss", "Swati", FlagIcon.SZ_SWAZILAND),
	ST_SOTHO_SOUTHERN("st", "Sotho, Southern", FlagIcon.AL_ALBANIA),
	SU_SUNDANESE("su", "Sundanese", null),
	SV_SWEDISH("sv", "Swedish", FlagIcon.SE_SWEDEN),
	SW_SWAHILI("sw", "Swahili", FlagIcon.KE_KENYA),
	TA_TAMIL("ta", "Tamil", null),
	TE_TELUGU("te", "Telugu", null),
	TG_TAJIK("tg", "Tajik", null),
	TH_THAI("th", "Thai", FlagIcon.TH_THAILAND),
	TI_TIGRINYA("ti", "Tigrinya", FlagIcon.ER_ERITREA),
	TK_TURKMEN("tk", "Turkmen", FlagIcon.TM_TURKMENISTAN),
	TL_TAGALOG("tl", "Tagalog", FlagIcon.PH_PHILIPPINES),
	TN_TSWANA("tn", "Tswana", FlagIcon.BW_BOTSWANA),
	TO_TONGA("to", "Tonga", FlagIcon.TO_TONGA),
	TR_TURKISH("tr", "Turkish", FlagIcon.TR_TURKEY),
	TS_TSONGA("ts", "Tsonga", null),
	TT_TATAR("tt", "Tatar", null),
	TW_TWI("tw", "Twi", null),
	TY_TAHITIAN("ty", "Tahitian", null),
	UG_UIGHUR_UYGHUR("ug", "Uighur; Uyghur", null),
	UK_UKRAINIAN("uk", "Ukrainian", FlagIcon.UA_UKRAINE),
	UR_URDU("ur", "Urdu", FlagIcon.PK_PAKISTAN),
	UZ_UZBEK("uz", "Uzbek", FlagIcon.UZ_UZBEKISTAN),
	VE_VENDA("ve", "Venda", null),
	VI_VIETNAMESE("vi", "Vietnamese", FlagIcon.VN_VIETNAM),
	VO_VOLAPUK("vo", "Volapük", null),
	WA_WALLOON("wa", "Walloon", null),
	WO_WOLOF("wo", "Wolof", null),
	XH_XHOSA("xh", "Xhosa", null),
	YI_YIDDISH("yi", "Yiddish", null),
	YO_YORUBA("yo", "Yoruba", null),
	ZA_ZHUANG_CHUANG("za", "Zhuang; Chuang", null),
	ZH_CHINESE("zh", "Chinese", FlagIcon.CN_CHINA),
	ZU_ZULU("zu", "Zulu", FlagIcon.ZA_SOUTH_AFRICA),
	PAP_PAPIAMENTO("pap", "Papiamento", FlagIcon.CW_CURACAO),
	;

	private final static Map<String, Language> entryByLanguage = new HashMap<>();
	private final String isoCode;
	private final String value;
	private final FlagIcon icon;

	static {
		for (Language value : values()) {
			entryByLanguage.put(value.getIsoCode(), value);
		}
	}

	public static Language getLanguageByIsoCode(String isoCode) {
		return entryByLanguage.get(isoCode);
	}

	Language(String isoCode, String value, FlagIcon icon) {
		this.isoCode = isoCode;
		this.value = value;
		this.icon = icon;
	}

	public FlagIcon getIcon() {
		return icon;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public String getValue() {
		return value;
	}

	public String getKey() {
		return "org.teamapps.dictionary.language." + name();
	}

	public String getLanguageLocalized(ApplicationLocalizationProvider localizationProvider) {
		return localizationProvider.getLocalized(getKey());
	}

	public static FlagIcon getLanguageIconByIsoCode(String isoCode) {
		Language entry = entryByLanguage.get(isoCode);
		if (entry != null) {
			return entry.getIcon();
		} else {
			return null;
		}
	}

	public static Icon getLanguageIcon(String language, Icon defaultIcon) {
		FlagIcon languageIcon = getLanguageIconByIsoCode(language);
		return languageIcon != null ? languageIcon : defaultIcon;
	}

	private boolean matchLanguage(String query, ApplicationLocalizationProvider localizationProvider) {
		query = query.toLowerCase();
		String localized = getLanguageLocalized(localizationProvider);
		if ((localized != null && localized.toLowerCase().contains(query)) || query.equals(getIsoCode().toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}

	public static String getLocalizedLanguageNameByIsoCode(String isoCode, ApplicationLocalizationProvider localizationProvider) {
		Language entry = entryByLanguage.get(isoCode);
		if (entry != null) {
			return entry.getLanguageLocalized(localizationProvider);
		} else {
			return isoCode;
		}
	}

	public static PropertyExtractor<Language> getPropertyExtractor(ApplicationLocalizationProvider localizationProvider) {
		return (language, s) -> {
			switch (s) {
				case BaseTemplate.PROPERTY_ICON:
					return language.getIcon();
				case BaseTemplate.PROPERTY_CAPTION:
					return language.getLanguageLocalized(localizationProvider);
				case BaseTemplate.PROPERTY_DESCRIPTION:
					return language.getIsoCode();
			}
			return null;
		};
	}

	public static ComboBox<Language> createComboBox(ApplicationLocalizationProvider localizationProvider) {
		return createComboBox(localizationProvider, null);
	}

	public static ComboBox<Language> createComboBox(ApplicationLocalizationProvider localizationProvider, Set<Language> allowedLanguages) {
		ComboBox<Language> comboBox = new ComboBox<>(BaseTemplate.LIST_ITEM_SMALL_ICON_SINGLE_LINE);
		comboBox.setDropDownTemplate(BaseTemplate.LIST_ITEM_MEDIUM_ICON_TWO_LINES);
		comboBox.setPropertyExtractor(getPropertyExtractor(localizationProvider));
		List<Language> languages = Arrays.stream(Language.values())
				.filter(language -> allowedLanguages == null || allowedLanguages.contains(language))
				.collect(Collectors.toList());
		comboBox.setModel(getComboBoxModel(localizationProvider, languages));
		comboBox.setRecordToStringFunction(language -> language.getLanguageLocalized(localizationProvider));
		return comboBox;
	}

	public static TagComboBox<Language> createTagComboBox(ApplicationLocalizationProvider applicationInstanceData) {
		return createTagComboBox(applicationInstanceData, null);
	}

	public static TagComboBox<Language> createTagComboBox(ApplicationLocalizationProvider localizationProvider, Set<Language> allowedLanguages) {
		TagComboBox<Language> tagComboBox = new TagComboBox<>();
		tagComboBox.setTemplate(BaseTemplate.LIST_ITEM_SMALL_ICON_SINGLE_LINE);
		tagComboBox.setDropDownTemplate(BaseTemplate.LIST_ITEM_MEDIUM_ICON_TWO_LINES);
		List<Language> languages = Arrays.stream(Language.values())
				.filter(language -> allowedLanguages == null || allowedLanguages.contains(language))
				.collect(Collectors.toList());
		tagComboBox.setModel(getComboBoxModel(localizationProvider, languages));
		tagComboBox.setPropertyExtractor(getPropertyExtractor(localizationProvider));
		tagComboBox.setRecordToStringFunction(language -> language.getLanguageLocalized(localizationProvider));
		return tagComboBox;
	}

	private static ComboBoxModel<Language> getComboBoxModel(ApplicationLocalizationProvider localizationProvider, List<Language> languages) {
				return s -> {
			if (s == null || s.isBlank()) {
				return languages;
			} else {
				final String query = s.toLowerCase();
				return languages.stream()
						.filter(language -> language.matchLanguage(query, localizationProvider))
						.collect(Collectors.toList());
			}
		};
	}


}
