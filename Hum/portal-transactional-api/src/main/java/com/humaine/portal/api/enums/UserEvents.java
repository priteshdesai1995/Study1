package com.humaine.portal.api.enums;

public enum UserEvents {

	START("START"), NAV("NAV"), PRODVIEW("PRODVIEW"), ADDCART("ADDCART"), REMCART("REMCART"), ADDLIST("ADDLIST"),
	REMLIST("REMLIST"), BUY("BUY"), REVIEW("REVIEW"), RATE("RATE"), END("END"),
	DISCOVER("DISCOVER"), NEWSLETTER_SUBSCRIBE("NEWSLETTER_SUBSCRIBE"), SEARCH("SEARCH"), MENU("MENU"),
	BACK_NAV("BACK_NAV"), SAVE_FOR_LATER("SAVE_FOR_LATER"), PROD_RETURN("PROD_RETURN"), VISIT_BLOG_POST("VISIT_BLOG_POST"),
	VISIT_SOCIAL_MEDIA("VISIT_SOCIAL_MEDIA"), DELETE("DELETE");

	public String eventID;

	UserEvents() {
	}

	UserEvents(String eventID) {
		this.eventID = eventID;
	}

	public String value() {
		return eventID;
	}
}
