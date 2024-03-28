package com.humaine.portal.api.util;

import java.util.HashMap;
import java.util.Map;

public class JPAPropertiesUtility {

	public static Map<String, Object> getProperties() {
		return getProperties(false, false);
	}

	public static Map<String, Object> getProperties(boolean showSQL, boolean ddlAuto) {
		Map<String, Object> properties = new HashMap<String, Object>();
		if (ddlAuto) {
			properties.put("hibernate.hbm2ddl.auto", "update");
			properties.put("spring.jpa.hibernate.ddl-auto", "update");
		}
		return properties;
	}
}
