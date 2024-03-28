package com.humaine.admin.api.enums;

public enum PaginationSortDirection {

	ASC("ASC"), DESC("DESC");

	private String direction;

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	private PaginationSortDirection(String direction) {
		this.direction = direction;
	}

}
