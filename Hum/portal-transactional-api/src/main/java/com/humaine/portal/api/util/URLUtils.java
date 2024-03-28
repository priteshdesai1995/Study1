package com.humaine.portal.api.util;

public class URLUtils {

	public static String getURL(String url) {
		if (url == null)
			return "/";
		if (url.endsWith("/"))
			return url;
		return url + "/";
	}
	
	public static String getFullContextPathURL(String url,String contextPath) {
		if (url == null)
			return "/";
		String URL = url;
		if (URL.endsWith("/")) URL = URL.substring(0, URL.length()-1);
		return URL + contextPath;
	}
}
