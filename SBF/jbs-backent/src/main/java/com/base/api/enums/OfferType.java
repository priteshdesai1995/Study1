package com.base.api.enums;

public enum OfferType {
	
	PERCENTAGE("PERCENTAGE"), AMOUNT("AMOUNT");
	
	private String offerType;

	OfferType(String offerType) {
		this.offerType = offerType;
	}

	public String getOfferType() {
		return offerType;
	}
	
}
