package com.base.api.constants;

public interface SecurityConstants {

	 String CLIENT_ID = "common";
	 String CLIENT_SECRET = "{bcrypt}$2a$10$IfbOPaObtuq7DmQX/byZWOI73EvnBA/nc2EL.sz9TMjAxgaLmpf4G";
	 String GRANT_TYPE_PASSWORD = "password";
	 String AUTHORIZATION_CODE = "authorization_code";
	 String REFRESH_TOKEN = "refresh_token";
	 String IMPLICIT = "implicit";
	 String SCOPE_READ = "read";
	 String SCOPE_WRITE = "write";
	 String TRUST = "trust";
	 int ACCESS_TOKEN_VALIDITY_SECONDS = 86400;
	 int REFRESH_TOKEN_VALIDITY_SECONDS = 31449600;
}
