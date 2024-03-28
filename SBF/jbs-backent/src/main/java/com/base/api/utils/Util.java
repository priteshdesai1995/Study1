/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.utils;


import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.TimeZone;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;

import com.base.api.filter.FilterBase;
import com.base.api.security.UserPrincipal;

/**
 * This class provide common operation.
 * 
 * @author minesh_prajapati
 *
 */
public class Util {

	public static boolean isEmpty(Collection obj) {
		return obj == null || obj.isEmpty();
	}

	public static boolean isEmpty(String string) {
		return string == null || string.trim().isEmpty();
	}


	public static boolean isEmpty(Object obj) {
		return obj == null || obj.toString().trim().isEmpty();
	}

 

	public static String getFilterQuery(FilterBase userFilter, String query) {
		String dir = "asc";
		String columnStr = "";
		if (userFilter.getSortingOrder() != null) {
			dir = userFilter.getSortingOrder();
		}
		if (userFilter.getColumnName() != null && !userFilter.getColumnName().isEmpty()) {
			columnStr = " order by " + userFilter.getColumnName() + " " + dir;
		} else {
			columnStr = " order by u.createdDate desc";
		}
		String queryParam = query + columnStr;
		return queryParam;
	}

	public static java.util.Date getCurrentUtcTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
		try {
			return localDateFormat.parse(simpleDateFormat.format(new Date(0)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	// Do by native query
	public static String getFilterNativeQuery(FilterBase userFilter, String query) {
		String limit = "";
		String dir = "asc";
		String columnStr = "";
		if (userFilter.getSortingOrder() != null) {
			dir = userFilter.getSortingOrder();
		}
		if (userFilter.getColumnName() != null && !userFilter.getColumnName().isEmpty()) {
			columnStr = " order by " + userFilter.getColumnName() + " " + dir;
		} else {
			columnStr = " order by u.createDate desc";
		}
		if (userFilter.getStartRec() != null) {
			limit = " limit " + userFilter.getEndRec() + " offset " + userFilter.getStartRec();
		}
		String queryParam = query + columnStr + limit;
		return queryParam;
	}

	public static OffsetDateTime getCurrentTimestamp() {
		return OffsetDateTime.now(getZoneOffset());
	}

	public static ZoneOffset getZoneOffset() {
		return ZoneOffset.UTC;
//		return ZoneOffset.of("+02:00");
	}
	
	public static UserPrincipal getLoggedInUser() {
		UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return principal;
	}

	public static String getFullName() {
		UserPrincipal principal = getLoggedInUser();
		String userName = principal.getFirstName() + " " + principal.getLastName();
		return userName;
	}
	
	
}
