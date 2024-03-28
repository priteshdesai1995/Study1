/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.database.seeders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.api.annotations.DatabaseSeeder;
import com.base.api.entities.OauthClientDetailEntity;
import com.base.api.repository.OAuth2ClientRepository;

/**
 * This class contain constatns value for Permission.
 * 
 * @author jay_patel
 * @author minesh_prajapati
 *
 */
@DatabaseSeeder
@Component("OAuthClientDetailsSeeder")
public class OAuthClientDetailsSeeder implements BaseSeeder {

	@Autowired
	OAuth2ClientRepository oAuth2ClientRepository;

	@Override
	public void seed() {
		OauthClientDetailEntity oauthClientDetailEntity = new OauthClientDetailEntity();
		oauthClientDetailEntity.setClientId("common");
		oauthClientDetailEntity.setAccessTokenValidity(86400);
		oauthClientDetailEntity.setAdditionalInformation(null);
		oauthClientDetailEntity.setAuthorities("ROLE_SUPERADMIN,ROLE_ADMIN,ROLE_USER,ROLE_SUBADMIN,ROLE_SUPER_ADMIN");
		oauthClientDetailEntity.setAuthorizedGrantTypes("password,refresh_token");
		oauthClientDetailEntity.setClientSecret("{bcrypt}$2a$10$IfbOPaObtuq7DmQX/byZWOI73EvnBA/nc2EL.sz9TMjAxgaLmpf4G");
		oauthClientDetailEntity.setRefreshTokenValidity(31449600);
		oauthClientDetailEntity.setResourceIds(null);
		oauthClientDetailEntity.setScope("read,write,trust");
		oauthClientDetailEntity.setWebServerRedirectUri(null);
		oAuth2ClientRepository.save(oauthClientDetailEntity);
	}
}
