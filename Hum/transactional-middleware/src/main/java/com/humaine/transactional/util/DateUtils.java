package com.humaine.transactional.util;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class DateUtils {
	static public final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public static OffsetDateTime getCurrentTimestemp() {
		return OffsetDateTime.now(getZoneOffset());
	}

	public static ZoneOffset getZoneOffset() {
		return ZoneOffset.of("+02:00");
	}

	public static OffsetDateTime getSchedularRunningOffset() {
		return getCurrentTimestemp().minusMonths(2);
	}

	public static boolean checkIfLastDay() {
		final Calendar c = Calendar.getInstance();
		return c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE);
	}

	public static String getFromatedDate(OffsetDateTime date) {
		return dateFormatter.format(date);
	}
}
