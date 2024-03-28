package com.humaine.admin.api.enums;

public enum Pages {
	HOME_LANDINGPAGE("Home/Landing Page");

	private String pageName;

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	private Pages(String pageName) {
		this.pageName = pageName;
	}

}
