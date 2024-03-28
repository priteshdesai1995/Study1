package com.base.api.gateway.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import com.base.api.dto.filter.FilterBase;
import com.base.api.filter.UserFilter;



public class Util {

    /**
     * Gets the filter query.
     *
     * @param userFilter the user filter
     * @param query the query
     * @return the filter query
     */
    public static String getFilterQueryForSuggestion(FilterBase userFilter, String query) {
        String dir = "asc";
        String columnStr = "";
        if (userFilter.getSortingOrder() != null) {
            dir = userFilter.getSortingOrder();
        }
        if (userFilter.getColumnName() != null && !userFilter.getColumnName().isEmpty()) {
            columnStr = " order by u." + userFilter.getColumnName() + " " + dir;
        } else {
            columnStr = " order by u.createdDate desc";
        }
        String queryParam = query + columnStr;
        return queryParam;
    }
    
    
    /**
	 * Gets the filter query.
	 *
	 * @param userFilter the user filter
	 * @param query the query
	 * @return the filter query
	 */
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
	
	/**
	 * Gets the filter query.
	 *
	 * @param userFilter the user filter
	 * @param query the query
	 * @return the filter query
	 */
	public static String getFilterQuery(UserFilter userFilter, String query) {
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

	/**
	 * Gets the current utc time.
	 *
	 * @return the current utc time
	 */
	public static Date getCurrentUtcTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
		try {
			return localDateFormat.parse(simpleDateFormat.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Generate UUID.
	 *
	 * @return the string
	 */
	public static String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * Gets the filter native query.
	 *
	 * @param userFilter the user filter
	 * @param query the query
	 * @return the filter native query
	 */
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
}
