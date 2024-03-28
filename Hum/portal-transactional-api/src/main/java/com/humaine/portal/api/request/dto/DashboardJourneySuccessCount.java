package com.humaine.portal.api.request.dto;

import javax.validation.constraints.NotNull;

import com.humaine.portal.api.enums.DashboardJourneyTimePeriod;

public class DashboardJourneySuccessCount {

	@NotNull(message = "{api.error.time-period.ids.null}{error.code.separator}{api.error.time-period.ids.null}")
	public DashboardJourneyTimePeriod period; 
}
