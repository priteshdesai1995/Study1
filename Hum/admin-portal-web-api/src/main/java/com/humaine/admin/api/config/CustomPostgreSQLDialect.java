package com.humaine.admin.api.config;

import java.sql.Types;

import org.hibernate.dialect.PostgreSQL94Dialect;

public class CustomPostgreSQLDialect extends PostgreSQL94Dialect {

	public CustomPostgreSQLDialect() {
		this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
	}
}
