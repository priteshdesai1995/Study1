package com.humaine.portal.api.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class CommonUtils {

	public static Map<Long, String> education = new HashMap<>() {
		private static final long serialVersionUID = 304273806932324743L;

		{
			put(0L, "N/A");
			put(1L, "Less than high school");
			put(2L, "High school");
			put(3L, "University degree");
			put(4L, "Graduate degree");
		}
	};

	public static Map<Long, String> age = new HashMap<>() {
		private static final long serialVersionUID = 304273806932324744L;

		{
			put(0L, "Under 17");
			put(1L, "18-24");
			put(2L, "25-34");
			put(3L, "35-44");
			put(4L, "45-54");
			put(5L, "55-64");
			put(6L, "65+");
		}
	};

	public static Map<Long, String> gender = new HashMap<>() {
		private static final long serialVersionUID = 304273806932324745L;

		{
			put(0L, "N/A");
			put(1L, "Male");
			put(2L, "Female");
			put(3L, "Other");
		}
	};

	public static Map<Long, String> familySize = new HashMap<>() {
		private static final long serialVersionUID = 304273806932324746L;

		{
			put(0L, "N/A");
			put(1L, "0 Sib");
			put(2L, "1 Sib");
			put(3L, "2 Sib");
			put(4L, "3 Sib");
			put(5L, "4 Sib");
			put(6L, "5+ Sib");
		}
	};

	public static String getAge(Long id) {
		if (id == null)
			return null;
		return age.get(id);
	}

	public static String getEducation(Long id) {
		if (id == null)
			return null;
		return education.get(id);
	}

	public static String getFamilySize(Long id) {
		if (id == null)
			return null;
		return familySize.get(id);
	}

	public static Double formatDouble(Double value) {
		return formatDouble(value, 2);
	}

	public static Double formatDouble(Double value, int decimalPlaces) {
		if (value == null)
			return value;
		BigDecimal bd = new BigDecimal(value).setScale(decimalPlaces, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static Float format(Float value) {
		return format(value, 2);
	}

	public static Float format(Float value, int decimalPlaces) {
		if (value == null)
			return value;
		BigDecimal bd = new BigDecimal(value).setScale(decimalPlaces, RoundingMode.HALF_UP);
		return bd.floatValue();
	}

	public static String generateUnique(String value) {
		if (StringUtils.isBlank(value))
			return "";
		return String.join("-", value.trim().toLowerCase().trim());
	}

	public static String formatTime(Float value) {
		Float m = (value % 3600) / 60;
		String s = String.valueOf(Math.round(m));
		return s;
	}

	public static String formatURL(String url) {
		if (url == null)
			url = "";
		return url.trim().replaceAll("(?://)/+", "/");
	}

}
