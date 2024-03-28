package com.humaine.portal.api.enums;

public enum DashboardJourneyTimePeriod {

	MONTHLY("MONTHLY"), TODAY("TODAY");

	public String period;

	DashboardJourneyTimePeriod() {
	}

	DashboardJourneyTimePeriod(String period) {
		this.period = period;
	}

	public String value() {
		return period;
	}
}
