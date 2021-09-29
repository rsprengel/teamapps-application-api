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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum Country {

	AF_AFGHANISTAN("AF", "Afghanistan", FlagIcon.AF_AFGHANISTAN),
	AX_ALAND_ISLANDS("AX", "Åland Islands", FlagIcon.AX_ALAND_ISLANDS),
	AL_ALBANIA("AL", "Albania", FlagIcon.AL_ALBANIA),
	DZ_ALGERIA("DZ", "Algeria", FlagIcon.DZ_ALGERIA),
	AS_AMERICAN_SAMOA("AS", "American Samoa", FlagIcon.AS_AMERICAN_SAMOA),
	AD_ANDORRA("AD", "Andorra", FlagIcon.AD_ANDORRA),
	AO_ANGOLA("AO", "Angola", FlagIcon.AO_ANGOLA),
	AI_ANGUILLA("AI", "Anguilla", FlagIcon.AI_ANGUILLA),
	AQ_ANTARCTICA("AQ", "Antarctica", FlagIcon.AQ_ANTARCTICA),
	AG_ANTIGUA_AND_BARBUDA("AG", "Antigua and Barbuda", FlagIcon.AG_ANTIGUA_BARBUDA),
	AR_ARGENTINA("AR", "Argentina", FlagIcon.AR_ARGENTINA),
	AM_ARMENIA("AM", "Armenia", FlagIcon.AM_ARMENIA),
	AW_ARUBA("AW", "Aruba", FlagIcon.AW_ARUBA),
	AU_AUSTRALIA("AU", "Australia", FlagIcon.AU_AUSTRALIA),
	AT_AUSTRIA("AT", "Austria", FlagIcon.AT_AUSTRIA),
	AZ_AZERBAIJAN("AZ", "Azerbaijan", FlagIcon.AZ_AZERBAIJAN),
	BH_BAHRAIN("BH", "Bahrain", FlagIcon.BH_BAHRAIN),
	BS_BAHAMAS("BS", "Bahamas", FlagIcon.BS_BAHAMAS),
	BD_BANGLADESH("BD", "Bangladesh", FlagIcon.BD_BANGLADESH),
	BB_BARBADOS("BB", "Barbados", FlagIcon.BB_BARBADOS),
	BY_BELARUS("BY", "Belarus", FlagIcon.BY_BELARUS),
	BE_BELGIUM("BE", "Belgium", FlagIcon.BE_BELGIUM),
	BZ_BELIZE("BZ", "Belize", FlagIcon.BZ_BELIZE),
	BJ_BENIN("BJ", "Benin", FlagIcon.BJ_BENIN),
	BM_BERMUDA("BM", "Bermuda", FlagIcon.BM_BERMUDA),
	BT_BHUTAN("BT", "Bhutan", FlagIcon.BT_BHUTAN),
	BO_BOLIVIA("BO", "Bolivia", FlagIcon.BO_BOLIVIA),
	BQ_BONAIRE_SINT_EUSTATIUS_AND_SABA("BQ", "Bonaire, Sint Eustatius and Saba", FlagIcon.BQ_CARIBBEAN_NETHERLANDS),
	BA_BOSNIA_AND_HERZEGOVINA("BA", "Bosnia and Herzegovina", FlagIcon.BA_BOSNIA),
	BW_BOTSWANA("BW", "Botswana", FlagIcon.BW_BOTSWANA),
	BV_BOUVET_ISLAND("BV", "Bouvet Island", FlagIcon.BV_BOUVET_ISLAND),
	BR_BRAZIL("BR", "Brazil", FlagIcon.BR_BRAZIL),
	IO_BRITISH_INDIAN_OCEAN_TERRITORY("IO", "British Indian Ocean Territory", FlagIcon.IO_BRITISH_INDIAN_OCEAN_TERRITORY),
	BN_BRUNEI_DARUSSALAM("BN", "Brunei Darussalam", FlagIcon.BN_BRUNEI),
	BG_BULGARIA("BG", "Bulgaria", FlagIcon.BG_BULGARIA),
	BF_BURKINA_FASO("BF", "Burkina Faso", FlagIcon.BF_BURKINA_FASO),
	BI_BURUNDI("BI", "Burundi", FlagIcon.BI_BURUNDI),
	KH_CAMBODIA("KH", "Cambodia", FlagIcon.KH_CAMBODIA),
	CM_CAMEROON("CM", "Cameroon", FlagIcon.CM_CAMEROON),
	CA_CANADA("CA", "Canada", FlagIcon.CA_CANADA),
	CV_CAPE_VERDE("CV", "Cape Verde", FlagIcon.CV_CAPE_VERDE),
	KY_CAYMAN_ISLANDS("KY", "Cayman Islands", FlagIcon.KY_CAYMAN_ISLANDS),
	CF_CENTRAL_AFRICAN_REPUBLIC("CF", "Central African Republic", FlagIcon.CF_CENTRAL_AFRICAN_REPUBLIC),
	TD_CHAD("TD", "Chad", FlagIcon.TD_CHAD),
	CL_CHILE("CL", "Chile", FlagIcon.CL_CHILE),
	CN_CHINA("CN", "China", FlagIcon.CN_CHINA),
	CX_CHRISTMAS_ISLAND("CX", "Christmas Island", FlagIcon.CX_CHRISTMAS_ISLAND),
	CC_COCOS__KEELING_ISLANDS("CC", "Cocos (Keeling) Islands", FlagIcon.CC_COCOS_ISLANDS),
	CO_COLOMBIA("CO", "Colombia", FlagIcon.CO_COLOMBIA),
	KM_COMOROS("KM", "Comoros", FlagIcon.KM_COMOROS),
	CG_CONGO("CG", "Congo", FlagIcon.CG_CONGO__BRAZZAVILLE),
	CD_CONGO_THE_DEMOCRATIC_REPUBLIC_OF_THE("CD", "Congo, the Democratic Republic of the", FlagIcon.CD_CONGO__KINSHASA),
	CK_COOK_ISLANDS("CK", "Cook Islands", FlagIcon.CK_COOK_ISLANDS),
	CR_COSTA_RICA("CR", "Costa Rica", FlagIcon.CR_COSTA_RICA),
	CI_COTE_D_IVOIRE("CI", "Côte d'Ivoire", FlagIcon.CI_COTE_D_IVOIRE),
	HR_CROATIA("HR", "Croatia", FlagIcon.HR_CROATIA),
	CU_CUBA("CU", "Cuba", FlagIcon.CU_CUBA),
	CW_CURACAO("CW", "Curaçao", FlagIcon.CW_CURACAO),
	CY_CYPRUS("CY", "Cyprus", FlagIcon.CY_CYPRUS),
	CZ_CZECH_REPUBLIC("CZ", "Czech Republic", FlagIcon.CZ_CZECHIA),
	DK_DENMARK("DK", "Denmark", FlagIcon.DK_DENMARK),
	DJ_DJIBOUTI("DJ", "Djibouti", FlagIcon.DJ_DJIBOUTI),
	DM_DOMINICA("DM", "Dominica", FlagIcon.DM_DOMINICA),
	DO_DOMINICAN_REPUBLIC("DO", "Dominican Republic", FlagIcon.DO_DOMINICAN_REPUBLIC),
	EC_ECUADOR("EC", "Ecuador", FlagIcon.EC_ECUADOR),
	EG_EGYPT("EG", "Egypt", FlagIcon.EG_EGYPT),
	SV_EL_SALVADOR("SV", "El Salvador", FlagIcon.SV_EL_SALVADOR),
	GQ_EQUATORIAL_GUINEA("GQ", "Equatorial Guinea", FlagIcon.GQ_EQUATORIAL_GUINEA),
	ER_ERITREA("ER", "Eritrea", FlagIcon.ER_ERITREA),
	EE_ESTONIA("EE", "Estonia", FlagIcon.EE_ESTONIA),
	ET_ETHIOPIA("ET", "Ethiopia", FlagIcon.ET_ETHIOPIA),
	FK_FALKLAND_ISLANDS__MALVINAS_("FK", "Falkland Islands (Malvinas)", FlagIcon.FK_FALKLAND_ISLANDS),
	FO_FAROE_ISLANDS("FO", "Faroe Islands", FlagIcon.FO_FAROE_ISLANDS),
	FJ_FIJI("FJ", "Fiji", FlagIcon.FJ_FIJI),
	FI_FINLAND("FI", "Finland", FlagIcon.FI_FINLAND),
	FR_FRANCE("FR", "France", FlagIcon.FR_FRANCE),
	GF_FRENCH_GUIANA("GF", "French Guiana", FlagIcon.GF_FRENCH_GUIANA),
	PF_FRENCH_POLYNESIA("PF", "French Polynesia", FlagIcon.PF_FRENCH_POLYNESIA),
	TF_FRENCH_SOUTHERN_TERRITORIES("TF", "French Southern Territories", FlagIcon.TF_FRENCH_SOUTHERN_TERRITORIES),
	GA_GABON("GA", "Gabon", FlagIcon.GA_GABON),
	GM_GAMBIA("GM", "Gambia", FlagIcon.GM_GAMBIA),
	GE_GEORGIA("GE", "Georgia", FlagIcon.GE_GEORGIA),
	DE_GERMANY("DE", "Germany", FlagIcon.DE_GERMANY),
	GH_GHANA("GH", "Ghana", FlagIcon.GH_GHANA),
	GI_GIBRALTAR("GI", "Gibraltar", FlagIcon.GI_GIBRALTAR),
	GR_GREECE("GR", "Greece", FlagIcon.GR_GREECE),
	GL_GREENLAND("GL", "Greenland", FlagIcon.GL_GREENLAND),
	GD_GRENADA("GD", "Grenada", FlagIcon.GD_GRENADA),
	GP_GUADELOUPE("GP", "Guadeloupe", FlagIcon.GP_GUADELOUPE),
	GU_GUAM("GU", "Guam", FlagIcon.GU_GUAM),
	GT_GUATEMALA("GT", "Guatemala", FlagIcon.GT_GUATEMALA),
	GG_GUERNSEY("GG", "Guernsey", FlagIcon.GG_GUERNSEY),
	GN_GUINEA("GN", "Guinea", FlagIcon.GN_GUINEA),
	GW_GUINEA_BISSAU("GW", "Guinea-Bissau", FlagIcon.GW_GUINEA_BISSAU),
	GY_GUYANA("GY", "Guyana", FlagIcon.GY_GUYANA),
	HT_HAITI("HT", "Haiti", FlagIcon.HT_HAITI),
	HM_HEARD_ISLAND_AND_MC_DONALD_ISLANDS("HM", "Heard Island and McDonald Islands", FlagIcon.HM_HEARD_MCDONALD_ISLANDS),
	VA_VATICAN_CITY_STATE("VA", "Vatican City State", FlagIcon.VA_VATICAN_CITY),
	HN_HONDURAS("HN", "Honduras", FlagIcon.HN_HONDURAS),
	HK_HONG_KONG("HK", "Hong Kong", FlagIcon.HK_HONG_KONG),
	HU_HUNGARY("HU", "Hungary", FlagIcon.HU_HUNGARY),
	IS_ICELAND("IS", "Iceland", FlagIcon.IS_ICELAND),
	IN_INDIA("IN", "India", FlagIcon.IN_INDIA),
	ID_INDONESIA("ID", "Indonesia", FlagIcon.ID_INDONESIA),
	IR_IRAN("IR", "Iran", FlagIcon.IR_IRAN),
	IQ_IRAQ("IQ", "Iraq", FlagIcon.IQ_IRAQ),
	IE_IRELAND("IE", "Ireland", FlagIcon.IE_IRELAND),
	IM_ISLE_OF_MAN("IM", "Isle of Man", FlagIcon.IM_ISLE_OF_MAN),
	IL_ISRAEL("IL", "Israel", FlagIcon.IL_ISRAEL),
	IT_ITALY("IT", "Italy", FlagIcon.IT_ITALY),
	JM_JAMAICA("JM", "Jamaica", FlagIcon.JM_JAMAICA),
	JP_JAPAN("JP", "Japan", FlagIcon.JP_JAPAN),
	JE_JERSEY("JE", "Jersey", FlagIcon.JE_JERSEY),
	JO_JORDAN("JO", "Jordan", FlagIcon.JO_JORDAN),
	KZ_KAZAKHSTAN("KZ", "Kazakhstan", FlagIcon.KZ_KAZAKHSTAN),
	KE_KENYA("KE", "Kenya", FlagIcon.KE_KENYA),
	KI_KIRIBATI("KI", "Kiribati", FlagIcon.KI_KIRIBATI),
	KP_KOREA_DEMOCRATIC_PEOPLE_S_REPUBLIC_OF("KP", "Korea, Democratic People's Republic of", FlagIcon.KP_NORTH_KOREA),
	KR_KOREA_REPUBLIC_OF("KR", "Korea, Republic of", FlagIcon.KR_SOUTH_KOREA),
	KW_KUWAIT("KW", "Kuwait", FlagIcon.KW_KUWAIT),
	KG_KYRGYZSTAN("KG", "Kyrgyzstan", FlagIcon.KG_KYRGYZSTAN),
	LA_LAO_PEOPLE_S_DEMOCRATIC_REPUBLIC("LA", "Lao People's Democratic Republic", FlagIcon.LA_LAOS),
	LV_LATVIA("LV", "Latvia", FlagIcon.LV_LATVIA),
	LB_LEBANON("LB", "Lebanon", FlagIcon.LB_LEBANON),
	LS_LESOTHO("LS", "Lesotho", FlagIcon.LS_LESOTHO),
	LR_LIBERIA("LR", "Liberia", FlagIcon.LR_LIBERIA),
	LY_LIBYA("LY", "Libya", FlagIcon.LY_LIBYA),
	LI_LIECHTENSTEIN("LI", "Liechtenstein", FlagIcon.LI_LIECHTENSTEIN),
	LT_LITHUANIA("LT", "Lithuania", FlagIcon.LT_LITHUANIA),
	LU_LUXEMBOURG("LU", "Luxembourg", FlagIcon.LU_LUXEMBOURG),
	MO_MACAO("MO", "Macao", FlagIcon.MO_MACAU),
	MK_MACEDONIA_THE_FORMER_YUGOSLAV_REPUBLIC_OF("MK", "Macedonia, the Former Yugoslav Republic of", FlagIcon.MK_MACEDONIA),
	MG_MADAGASCAR("MG", "Madagascar", FlagIcon.MG_MADAGASCAR),
	MW_MALAWI("MW", "Malawi", FlagIcon.MW_MALAWI),
	MY_MALAYSIA("MY", "Malaysia", FlagIcon.MY_MALAYSIA),
	MV_MALDIVES("MV", "Maldives", FlagIcon.MV_MALDIVES),
	ML_MALI("ML", "Mali", FlagIcon.ML_MALI),
	MT_MALTA("MT", "Malta", FlagIcon.MT_MALTA),
	MH_MARSHALL_ISLANDS("MH", "Marshall Islands", FlagIcon.MH_MARSHALL_ISLANDS),
	MQ_MARTINIQUE("MQ", "Martinique", FlagIcon.MQ_MARTINIQUE),
	MR_MAURITANIA("MR", "Mauritania", FlagIcon.MR_MAURITANIA),
	MU_MAURITIUS("MU", "Mauritius", FlagIcon.MU_MAURITIUS),
	YT_MAYOTTE("YT", "Mayotte", FlagIcon.YT_MAYOTTE),
	MX_MEXICO("MX", "Mexico", FlagIcon.MX_MEXICO),
	FM_MICRONESIA("FM", "Micronesia", FlagIcon.FM_MICRONESIA),
	MD_MOLDOVA("MD", "Moldova", FlagIcon.MD_MOLDOVA),
	MC_MONACO("MC", "Monaco", FlagIcon.MC_MONACO),
	MN_MONGOLIA("MN", "Mongolia", FlagIcon.MN_MONGOLIA),
	ME_MONTENEGRO("ME", "Montenegro", FlagIcon.ME_MONTENEGRO),
	MS_MONTSERRAT("MS", "Montserrat", FlagIcon.MS_MONTSERRAT),
	MA_MOROCCO("MA", "Morocco", FlagIcon.MA_MOROCCO),
	MZ_MOZAMBIQUE("MZ", "Mozambique", FlagIcon.MZ_MOZAMBIQUE),
	MM_MYANMAR("MM", "Myanmar", FlagIcon.MM_MYANMAR),
	NA_NAMIBIA("NA", "Namibia", FlagIcon.NA_NAMIBIA),
	NR_NAURU("NR", "Nauru", FlagIcon.NR_NAURU),
	NP_NEPAL("NP", "Nepal", FlagIcon.NP_NEPAL),
	NL_NETHERLANDS("NL", "Netherlands", FlagIcon.NL_NETHERLANDS),
	NC_NEW_CALEDONIA("NC", "New Caledonia", FlagIcon.NC_NEW_CALEDONIA),
	NZ_NEW_ZEALAND("NZ", "New Zealand", FlagIcon.NZ_NEW_ZEALAND),
	NI_NICARAGUA("NI", "Nicaragua", FlagIcon.NI_NICARAGUA),
	NE_NIGER("NE", "Niger", FlagIcon.NE_NIGER),
	NG_NIGERIA("NG", "Nigeria", FlagIcon.NG_NIGERIA),
	NU_NIUE("NU", "Niue", FlagIcon.NU_NIUE),
	NF_NORFOLK_ISLAND("NF", "Norfolk Island", FlagIcon.NF_NORFOLK_ISLAND),
	MP_NORTHERN_MARIANA_ISLANDS("MP", "Northern Mariana Islands", FlagIcon.MP_NORTHERN_MARIANA_ISLANDS),
	NO_NORWAY("NO", "Norway", FlagIcon.NO_NORWAY),
	OM_OMAN("OM", "Oman", FlagIcon.OM_OMAN),
	PK_PAKISTAN("PK", "Pakistan", FlagIcon.PK_PAKISTAN),
	PW_PALAU("PW", "Palau", FlagIcon.PW_PALAU),
	PS_PALESTINE_STATE_OF("PS", "Palestine, State of", FlagIcon.PS_PALESTINE),
	PA_PANAMA("PA", "Panama", FlagIcon.PA_PANAMA),
	PG_PAPUA_NEW_GUINEA("PG", "Papua New Guinea", FlagIcon.PG_PAPUA_NEW_GUINEA),
	PY_PARAGUAY("PY", "Paraguay", FlagIcon.PY_PARAGUAY),
	PE_PERU("PE", "Peru", FlagIcon.PE_PERU),
	PH_PHILIPPINES("PH", "Philippines", FlagIcon.PH_PHILIPPINES),
	PN_PITCAIRN("PN", "Pitcairn", FlagIcon.PN_PITCAIRN_ISLANDS),
	PL_POLAND("PL", "Poland", FlagIcon.PL_POLAND),
	PT_PORTUGAL("PT", "Portugal", FlagIcon.PT_PORTUGAL),
	PR_PUERTO_RICO("PR", "Puerto Rico", FlagIcon.PR_PUERTO_RICO),
	QA_QATAR("QA", "Qatar", FlagIcon.QA_QATAR),
	RE_REUNION("RE", "Réunion", FlagIcon.RE_REUNION),
	RO_ROMANIA("RO", "Romania", FlagIcon.RO_ROMANIA),
	RU_RUSSIAN_FEDERATION("RU", "Russian Federation", FlagIcon.RU_RUSSIA),
	RW_RWANDA("RW", "Rwanda", FlagIcon.RW_RWANDA),
	BL_SAINT_BARTHELEMY("BL", "Saint Barthélemy", FlagIcon.BL_ST_BARTHELEMY),
	SH_SAINT_HELENA_ASCENSION_AND_TRISTAN_DA_CUNHA("SH", "Saint Helena, Ascension and Tristan da Cunha", FlagIcon.SH_ST_HELENA),
	KN_SAINT_KITTS_AND_NEVIS("KN", "Saint Kitts and Nevis", FlagIcon.KN_ST_KITTS_NEVIS),
	LC_SAINT_LUCIA("LC", "Saint Lucia", FlagIcon.LC_ST_LUCIA),
	MF_SAINT_MARTIN__FRENCH_PART_("MF", "Saint Martin (French part)", FlagIcon.MF_ST_MARTIN),
	PM_SAINT_PIERRE_AND_MIQUELON("PM", "Saint Pierre and Miquelon", FlagIcon.PM_ST_PIERRE_MIQUELON),
	VC_SAINT_VINCENT_AND_THE_GRENADINES("VC", "Saint Vincent and the Grenadines", FlagIcon.VC_ST_VINCENT_GRENADINES),
	WS_SAMOA("WS", "Samoa", FlagIcon.WS_SAMOA),
	SM_SAN_MARINO("SM", "San Marino", FlagIcon.SM_SAN_MARINO),
	ST_SAO_TOME_AND_PRINCIPE("ST", "Sao Tome and Principe", FlagIcon.ST_SAO_TOME_PRINCIPE),
	SA_SAUDI_ARABIA("SA", "Saudi Arabia", FlagIcon.SA_SAUDI_ARABIA),
	SN_SENEGAL("SN", "Senegal", FlagIcon.SN_SENEGAL),
	RS_SERBIA("RS", "Serbia", FlagIcon.RS_SERBIA),
	SC_SEYCHELLES("SC", "Seychelles", FlagIcon.SC_SEYCHELLES),
	SL_SIERRA_LEONE("SL", "Sierra Leone", FlagIcon.SL_SIERRA_LEONE),
	SG_SINGAPORE("SG", "Singapore", FlagIcon.SG_SINGAPORE),
	SX_SINT_MAARTEN__DUTCH_PART_("SX", "Sint Maarten (Dutch part)", FlagIcon.SX_SINT_MAARTEN),
	SK_SLOVAKIA("SK", "Slovakia", FlagIcon.SK_SLOVAKIA),
	SI_SLOVENIA("SI", "Slovenia", FlagIcon.SI_SLOVENIA),
	SB_SOLOMON_ISLANDS("SB", "Solomon Islands", FlagIcon.SB_SOLOMON_ISLANDS),
	SO_SOMALIA("SO", "Somalia", FlagIcon.SO_SOMALIA),
	ZA_SOUTH_AFRICA("ZA", "South Africa", FlagIcon.ZA_SOUTH_AFRICA),
	GS_SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLANDS("GS", "South Georgia and the South Sandwich Islands", FlagIcon.GS_SOUTH_GEORGIA_SOUTH_SANDWICH_ISLANDS),
	SS_SOUTH_SUDAN("SS", "South Sudan", FlagIcon.SS_SOUTH_SUDAN),
	ES_SPAIN("ES", "Spain", FlagIcon.ES_SPAIN),
	LK_SRI_LANKA("LK", "Sri Lanka", FlagIcon.LK_SRI_LANKA),
	SD_SUDAN("SD", "Sudan", FlagIcon.SD_SUDAN),
	SR_SURINAME("SR", "Suriname", FlagIcon.SR_SURINAME),
	SJ_SVALBARD_AND_JAN_MAYEN("SJ", "Svalbard and Jan Mayen", FlagIcon.SJ_SVALBARD_JAN_MAYEN),
	SZ_SWAZILAND("SZ", "Swaziland", FlagIcon.SZ_SWAZILAND),
	SE_SWEDEN("SE", "Sweden", FlagIcon.SE_SWEDEN),
	CH_SWITZERLAND("CH", "Switzerland", FlagIcon.CH_SWITZERLAND),
	SY_SYRIAN_ARAB_REPUBLIC("SY", "Syrian Arab Republic", FlagIcon.SY_SYRIA),
	TW_TAIWAN("TW", "Taiwan", FlagIcon.TW_TAIWAN),
	TJ_TAJIKISTAN("TJ", "Tajikistan", FlagIcon.TJ_TAJIKISTAN),
	TZ_TANZANIA("TZ", "Tanzania", FlagIcon.TZ_TANZANIA),
	TH_THAILAND("TH", "Thailand", FlagIcon.TH_THAILAND),
	TL_TIMOR_LESTE("TL", "Timor-Leste", FlagIcon.TL_TIMOR_LESTE),
	TG_TOGO("TG", "Togo", FlagIcon.TG_TOGO),
	TK_TOKELAU("TK", "Tokelau", FlagIcon.TK_TOKELAU),
	TO_TONGA("TO", "Tonga", FlagIcon.TO_TONGA),
	TT_TRINIDAD_AND_TOBAGO("TT", "Trinidad and Tobago", FlagIcon.TT_TRINIDAD_TOBAGO),
	TN_TUNISIA("TN", "Tunisia", FlagIcon.TN_TUNISIA),
	TR_TURKEY("TR", "Turkey", FlagIcon.TR_TURKEY),
	TM_TURKMENISTAN("TM", "Turkmenistan", FlagIcon.TM_TURKMENISTAN),
	TC_TURKS_AND_CAICOS_ISLANDS("TC", "Turks and Caicos Islands", FlagIcon.TC_TURKS_CAICOS_ISLANDS),
	TV_TUVALU("TV", "Tuvalu", FlagIcon.TV_TUVALU),
	UG_UGANDA("UG", "Uganda", FlagIcon.UG_UGANDA),
	UA_UKRAINE("UA", "Ukraine", FlagIcon.UA_UKRAINE),
	AE_UNITED_ARAB_EMIRATES("AE", "United Arab Emirates", FlagIcon.AE_UNITED_ARAB_EMIRATES),
	GB_UNITED_KINGDOM("GB", "United Kingdom", FlagIcon.GB_GREAT_BRITAIN),
	US_UNITED_STATES("US", "United States", FlagIcon.US_UNITED_STATES),
	UM_UNITED_STATES_MINOR_OUTLYING_ISLANDS("UM", "United States Minor Outlying Islands", FlagIcon.UM_US_OUTLYING_ISLANDS),
	UY_URUGUAY("UY", "Uruguay", FlagIcon.UY_URUGUAY),
	UZ_UZBEKISTAN("UZ", "Uzbekistan", FlagIcon.UZ_UZBEKISTAN),
	VU_VANUATU("VU", "Vanuatu", FlagIcon.VU_VANUATU),
	VE_VENEZUELA("VE", "Venezuela", FlagIcon.VE_VENEZUELA),
	VN_VIETNAM("VN", "Vietnam", FlagIcon.VN_VIETNAM),
	VG_VIRGIN_ISLANDS_BRITISH("VG", "Virgin Islands, British", FlagIcon.VG_BRITISH_VIRGIN_ISLANDS),
	VI_VIRGIN_ISLANDS_U_S("VI", "Virgin Islands, U.S.", FlagIcon.VI_US_VIRGIN_ISLANDS),
	WF_WALLIS_AND_FUTUNA("WF", "Wallis and Futuna", FlagIcon.WF_WALLIS_FUTUNA),
	EH_WESTERN_SAHARA("EH", "Western Sahara", FlagIcon.EH_WESTERN_SAHARA),
	YE_YEMEN("YE", "Yemen", FlagIcon.YE_YEMEN),
	ZM_ZAMBIA("ZM", "Zambia", FlagIcon.ZM_ZAMBIA),
	ZW_ZIMBABWE("ZW", "Zimbabwe", FlagIcon.ZW_ZIMBABWE),

	XK_KOSOVO("XK", "Kosovo", FlagIcon.XK_KOSOVO),
	;

	private final static Map<String, Country> entryByCountryIsoCode = new HashMap<>();

	static {
		for (Country value : values()) {
			entryByCountryIsoCode.put(value.getIsoCode(), value);
		}
	}

	public static Country getCountryByIsoCode(String countryIso) {
		return entryByCountryIsoCode.get(countryIso);
	}

	private final String isoCode;
	private final String value;
	private final Icon icon;

	Country(String isoCode, String value, Icon icon) {
		this.isoCode = isoCode;
		this.value = value;
		this.icon = icon;
	}

	public Icon getIcon() {
		return icon;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public String getValue() {
		return value;
	}

	public String getKey() {
		return "org.teamapps.dictionary.country." + name();
	}

	public String getLocalized(ApplicationInstanceData applicationInstanceData) {
		return applicationInstanceData.getLocalized(getKey());
	}

	private boolean matchCountry(String query, ApplicationInstanceData applicationInstanceData) {
		query = query.toLowerCase();
		String localized = getLocalized(applicationInstanceData);
		if ((localized != null && localized.toLowerCase().contains(query)) || query.equals(getIsoCode().toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}

	public static PropertyExtractor<Country> getPropertyExtractor(ApplicationInstanceData applicationInstanceData) {
		return (country, s) -> {
			switch (s) {
				case BaseTemplate.PROPERTY_ICON:
					return country.getIcon();
				case BaseTemplate.PROPERTY_CAPTION:
					return country.getLocalized(applicationInstanceData);
				case BaseTemplate.PROPERTY_DESCRIPTION:
					return country.getIsoCode();
			}
			return null;
		};
	}

	public static ComboBox<Country> createComboBox(ApplicationInstanceData applicationInstanceData) {
		ComboBox<Country> comboBox = new ComboBox<>(BaseTemplate.LIST_ITEM_SMALL_ICON_SINGLE_LINE);
		comboBox.setDropDownTemplate(BaseTemplate.LIST_ITEM_MEDIUM_ICON_TWO_LINES);
		List<Country> countries = Arrays.asList(Country.values());
		comboBox.setModel(getCountryComboBoxModel(applicationInstanceData, countries));
		comboBox.setPropertyExtractor(getPropertyExtractor(applicationInstanceData));
		comboBox.setRecordToStringFunction(country -> country.getLocalized(applicationInstanceData));
		return comboBox;
	}

	public static TagComboBox<Country> createTagComboBox(ApplicationInstanceData applicationInstanceData) {
		TagComboBox<Country> tagComboBox = new TagComboBox<>();
		tagComboBox.setTemplate(BaseTemplate.LIST_ITEM_SMALL_ICON_SINGLE_LINE);
		tagComboBox.setDropDownTemplate(BaseTemplate.LIST_ITEM_MEDIUM_ICON_TWO_LINES);
		List<Country> countries = Arrays.asList(Country.values());
		tagComboBox.setModel(getCountryComboBoxModel(applicationInstanceData, countries));
		tagComboBox.setPropertyExtractor(getPropertyExtractor(applicationInstanceData));
		tagComboBox.setRecordToStringFunction(country -> country.getLocalized(applicationInstanceData));
		return tagComboBox;
	}

	private static ComboBoxModel<Country> getCountryComboBoxModel(ApplicationInstanceData applicationInstanceData, List<Country> countries) {
		return s -> {
			if (s == null || s.isBlank()) {
				return countries;
			} else {
				final String query = s.toLowerCase();
				return countries.stream()
						.filter(language -> language.matchCountry(query, applicationInstanceData))
						.collect(Collectors.toList());
			}
		};
	}

}
