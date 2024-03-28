package com.humaine.collection.api.es.projection.model;

public interface ElasticPageLoadData {

	Long getId();

	Long getAccountId();

	String getUserId();

	String getSessionId();

	String getPageURL();

	Long getPageLoadTime();

	String getTimestamp();
}
