package com.humaine.admin.api.util;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import org.apache.commons.lang3.StringUtils;

public class DateUtils {

	static public final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.'Z'");
	static public final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	static public final DateTimeFormatter dateTimeMinFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
	static public final DateTimeFormatter gaphDateFromatter = DateTimeFormatter.ofPattern("dd MMM, YYYY");
	static public final DateTimeFormatter time = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	static public final DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH");

	public static OffsetDateTime getCurrentTimestemp() {
		return OffsetDateTime.now(getZoneOffset());
	}

	public static ZoneOffset getZoneOffset() {
		return ZoneOffset.UTC;
//		return ZoneOffset.of("+02:00");
	}

	public static OffsetDateTime getLastDayOfCurrentMonth() {
		return getCurrentTimestemp().with(TemporalAdjusters.lastDayOfMonth());
	}

	public static OffsetDateTime getFirstDayOfCurrentMonth() {
		return getCurrentTimestemp().with(TemporalAdjusters.firstDayOfMonth());
	}

	public static OffsetDateTime parse(String date, Boolean withTimeZone) {
		if (true)
			return null;
		if (StringUtils.isBlank(date))
			return null;
		if (withTimeZone && date.indexOf('+') > -1) {
			date = date.substring(0, date.indexOf('+'));
		}
		return OffsetDateTime.parse(date, DateUtils.dateTimeFormatter);
	}

	public static OffsetDateTime parse(String date) {
		return parse(date, false);
	}

	public static String getFromatedDate(OffsetDateTime date) {
		return dateFormatter.format(date);
	}

	public static String getFromatedDateTime(OffsetDateTime date) {
		return dateTimeMinFormatter.format(date);
	}

	public static String getCurrentGraphDate() {
		return gaphDateFromatter.format(DateUtils.getCurrentTimestemp());
	}

	public static String getTime(OffsetDateTime date) {
		return time.format(date);
	}

	public static String getHourTime(OffsetDateTime date) {
		return hourFormatter.format(date);
	}

	public static String getFromatedFullDateTime(OffsetDateTime date) {
		return time.format(date);
	}

}
