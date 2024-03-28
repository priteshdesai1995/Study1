package com.humaine.collection.api.util;

import com.humaine.collection.api.enums.Browsers;

public class BrowserUtils {

	public static String getBrowser(String browser) {
		if (browser == null) return null;
		
		if (browser.contains(Browsers.CHROME.value()) || browser.contains(Browsers.CHROMIUM.value())) {
			return Browsers.CHROME.value();
		} else if (browser.contains(Browsers.SAFARI.value())) {
			return Browsers.SAFARI.value();
		} else if (browser.contains(Browsers.IE.value())) {
			return Browsers.IE.value();
		} else if (browser.contains(Browsers.EDGE.value())) {
			return Browsers.EDGE.value();
		} else if (browser.contains(Browsers.OPERA.value())) {
			return Browsers.OPERA.value();
		} else if (browser.contains(Browsers.FIREFOX.value())) {
			return Browsers.FIREFOX.value();
		} else {
			return Browsers.OTHER.value();
		} 
	}
}
