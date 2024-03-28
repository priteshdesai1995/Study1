package com.humaine.collection.api.es.projection.model;

public interface EnddedSession {
	Long getAccountID();

	String getSessionID();

	String getUserID();

	String getStartTime();
	
	String getLastEventTime();

	String getLastPageLoadTime();
}
