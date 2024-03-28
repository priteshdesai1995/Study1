package com.humaine.collection.api.util;

import java.time.OffsetDateTime;

public class ElasticIndexUtils {

	public static String getSchemaLocation(String schemaFolderLocation) {
		String s = schemaFolderLocation;
		if (!s.endsWith("/"))
			s = s + "/";
		return s;
	}

	public static String getIndexName(OffsetDateTime date, String prefix) {
		return prefix + DateUtils.getFromatedDate(date).replaceAll("-", "");
	}

	public static String getCurrentDateIndexName(String prefix) {
		return getIndexName(DateUtils.getCurrentTimestemp(), prefix);
	}

}
