package com.humaine.portal.api.enums;

public enum JourneyElementMasterIds {

	FIRST_INTEREST(1L), 
	DECISION(2L), 
	PURCHASE_ADD_TO_CART(3L),
	PURCHASE_BUY(4L),
	PURCHASE_RATE_PRODUCT(5L);

	public Long id;

	JourneyElementMasterIds() {
	}

	JourneyElementMasterIds(Long id) {
		this.id = id;
	}

	public Long value() {
		return id;
	}
}
