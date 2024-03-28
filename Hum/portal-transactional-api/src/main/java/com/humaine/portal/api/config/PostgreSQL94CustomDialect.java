package com.humaine.portal.api.config;

import org.hibernate.dialect.PostgreSQL94Dialect;

import com.vladmihalcea.hibernate.type.array.StringArrayType;

public class PostgreSQL94CustomDialect extends PostgreSQL94Dialect {

	public PostgreSQL94CustomDialect() {
		super();
		this.registerHibernateType(2003, StringArrayType.class.getName());
	}
}
