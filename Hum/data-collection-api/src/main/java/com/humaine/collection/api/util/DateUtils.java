package com.humaine.collection.api.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

import org.apache.commons.lang3.StringUtils;

public class DateUtils {
	static public final String AMERICA_LOS_ANGELES_ZONE_ID = "America/Los_Angeles";
	static public final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.'Z'");
	static public final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	static public final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static OffsetDateTime getCurrentTimestemp() {
		return OffsetDateTime.now(getZoneOffset());
	}

	public static OffsetDateTime getCurrentTimestemp(String timezone) {
		ZoneId zoneId = ZoneId.of( "America/Los_Angeles" );
		return OffsetDateTime.now(zoneId);
	}
	public static ZoneOffset getZoneOffset() {
		return ZoneOffset.UTC;
//		return ZoneOffset.of("+02:00");
	}

	public static boolean isSameDay(OffsetDateTime date1, OffsetDateTime date2) {
		LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate localDate2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return localDate1.isEqual(localDate2);
	}

	public static OffsetDateTime getLastDayOfCurrentMonth() {
		return getCurrentTimestemp().with(TemporalAdjusters.lastDayOfMonth());
	}

	public static OffsetDateTime getFirstDayOfCurrentMonth() {
		return getCurrentTimestemp().with(TemporalAdjusters.firstDayOfMonth());
	}

	public static String parse(String date) {
		if (StringUtils.isBlank(date))
			return null;
		date = date.replace(" ", "T");
		return date;

	}

	public static String getFromatedDate(OffsetDateTime date) {
		return dateFormatter.format(date);
	}

	public static Long getDuration(OffsetDateTime date) {
		LocalDateTime req = LocalDateTime.parse(timeFormatter.format(date.atZoneSameInstant(getZoneOffset())),
				timeFormatter);
		LocalDateTime current = LocalDateTime.parse(timeFormatter.format(getCurrentTimestemp()), timeFormatter);
		return ChronoUnit.SECONDS.between(req, current);
	}

	public static OffsetDateTime parseDate(String date) {
		if (StringUtils.isBlank(date))
			return null;
		OffsetDateTime result = LocalDateTime.parse(date, timeFormatter).atZone(ZoneId.of("UTC")).toOffsetDateTime();
		return result;
	}
}
