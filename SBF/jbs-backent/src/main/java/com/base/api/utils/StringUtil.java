/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.utils;

/**
 * This class provide string related operation.
 * 
 * @author minesh_prajapati
 *
 */
public class StringUtil {

	/**
	 * use for remove all white space from string.
	 * 
	 * @param value string value.
	 * @return string value.
	 */
	public static String removeWhiteSpace(String value) {
		return value.replaceAll("\\s+", "");
	}

	/**
	 * use for split string by forward slash
	 * 
	 * @param value string value.
	 * @return string array.
	 */
	public static String[] splitByForwardSlash(String value) {
		return value.split("/");
	}
}
