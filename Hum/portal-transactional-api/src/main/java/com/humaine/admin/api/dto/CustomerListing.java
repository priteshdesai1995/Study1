package com.humaine.admin.api.dto;

import java.util.Date;

public interface CustomerListing {

	String getFullName();

	String getEmail();

	String getUrl();

	String getAddress();

	String getCity();

	String getState();

	Date getRegisteredOn();

	Long getAccountId();

	String getIndustry();
	
	String getStatus();
}
