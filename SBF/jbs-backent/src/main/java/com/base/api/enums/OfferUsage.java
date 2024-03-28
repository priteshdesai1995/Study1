package com.base.api.enums;

public enum OfferUsage {
	
	ONETIME("ONETIME"),MULTIPLETIME("MULTIPLETIME");
	
	private String offerUsage;
	
	OfferUsage(String offerUsage) {
		this.offerUsage = offerUsage;
	}

	public String getOfferUsage() {
		return offerUsage;
	}

}
