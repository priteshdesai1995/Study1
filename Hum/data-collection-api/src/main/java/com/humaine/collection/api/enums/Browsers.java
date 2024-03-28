package com.humaine.collection.api.enums;

public enum Browsers {
	SAFARI("Safari"), 
	CHROME("Chrome"),
	CHROMIUM("Chromium"),
	EDGE("Edge"),
	IE("IE"),
	FIREFOX("Firefox"),
	OPERA("Opera"),
	OTHER("Other");
	
	

	public String browserName;

	Browsers() {
	}

	Browsers(String browserName) {
		this.browserName = browserName;
	}

	public String value() {
		return browserName;
	}
}
