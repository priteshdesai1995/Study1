package com.humaine.admin.api.util;

public class SecurityConstants {

	public static final String CLIENT_ID = "common";
	public static final String CLIENT_SECRET = "{bcrypt}$2a$10$IfbOPaObtuq7DmQX/byZWOI73EvnBA/nc2EL.sz9TMjAxgaLmpf4G";
	public static final String GRANT_TYPE_PASSWORD = "password";
	public static final String AUTHORIZATION_CODE = "authorization_code";
	public static final String REFRESH_TOKEN = "refresh_token";
	public static final String IMPLICIT = "implicit";
	public static final String SCOPE_READ = "read";
	public static final String SCOPE_WRITE = "write";
	public static final String TRUST = "trust";
	public static final int ACCESS_TOKEN_VALIDITY_SECONDS = 86400;
	public static final int REFRESH_TOKEN_VALIDITY_SECONDS = 31449600;

}
