package com.base.api.service.impl;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.base.api.entities.ContentManagement;
import com.base.api.entities.OauthClientDetailEntity;
import com.base.api.entities.PersonAddress;
import com.base.api.entities.Translation;
import com.base.api.entities.User;
import com.base.api.entities.UserProfile;
import com.base.api.entities.UserRole;
import com.base.api.enums.UserStatus;
import com.base.api.request.dto.AddressDTO;
import com.base.api.request.dto.AdminAddUserDTO;
import com.base.api.request.dto.ContentManagementDTO;
import com.base.api.request.dto.RolesDto;
import com.base.api.request.dto.TranslationDTO;
import com.base.api.request.dto.UserSignupDTO;


@Component
public class MapperUserService implements Serializable {

	private static final long serialVersionUID = 2088976571645017995L;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Map user from DTO.
	 *
	 * @param userSignupDTO the user signup DTO
	 * @return the user
	 */
	public User mapUserFromDTO(UserSignupDTO userSignupDTO) {
		User userEntity = new User();
		userEntity.setUserName(userSignupDTO.getUserName());
		userEntity.setStatus(UserStatus.ACTIVE);
		userEntity.setPassword(passwordEncoder.encode(userSignupDTO.getPassword()));
		userEntity.setCreatedDate(LocalDateTime.now());
		return userEntity;
	}

	/**
	 * Map person from DTO.
	 *
	 * @param userSignupDTO the user signup DTO
	 * @return the user profile
	 */
	public UserProfile mapPersonFromDTO(UserSignupDTO userSignupDTO) {
		UserProfile profileEntity = new UserProfile();
		profileEntity.setFirstName(userSignupDTO.getFirstName());
		profileEntity.setMiddleName(userSignupDTO.getMiddleName());
		profileEntity.setLastName(userSignupDTO.getLastName());
		profileEntity.setDateOfBirth(userSignupDTO.getDateOfBirth());
		profileEntity.setGender(userSignupDTO.getGender());
		profileEntity.setEmail(userSignupDTO.getEmail());
		profileEntity.setCellPhone(userSignupDTO.getCellPhone());
		profileEntity.setHomePhone(userSignupDTO.getHomePhone());
		profileEntity.setWorkPhone(userSignupDTO.getWorkPhone());
		profileEntity.setOccupation(userSignupDTO.getOccupation());
		profileEntity.setEmployer(userSignupDTO.getEmployer());
		profileEntity.setCreatedDate(LocalDateTime.now());
		return profileEntity;
	}

	/**
	 * Map address from DTO.
	 *
	 * @param addressDTO the address DTO
	 * @return the person address
	 */
	public PersonAddress mapAddressFromDTO(AddressDTO addressDTO) {
		PersonAddress addressEntity = new PersonAddress();
		addressEntity.setAddressLineOne(addressDTO.getAddressLineOne());
		addressEntity.setAddressLineTwo(addressDTO.getAddressLineTwo());
		addressEntity.setAddressType(addressDTO.getAddressType());
		addressEntity.setProvince(addressDTO.getProvince());
		addressEntity.setCity(addressDTO.getCity());
		addressEntity.setPostalCode(addressDTO.getPostalCode());
		return addressEntity;
	}

	/**
	 * Map role from dto.
	 *
	 * @param roleDto the role dto
	 * @return the user role
	 */
	public UserRole mapRoleFromDto(RolesDto roleDto) {

		UserRole userRoleEntity = new UserRole();
		userRoleEntity.setRoleName(roleDto.getName());
		userRoleEntity.setStatus(UserStatus.ACTIVE);
		userRoleEntity.setCreatedDate(LocalDateTime.now());
		return userRoleEntity;
	}

	/**
	 * Map client from dto.
	 *
	 * @return the oauth client detail entity
	 */
	public OauthClientDetailEntity mapClientFromDto() {
		OauthClientDetailEntity oauthClientDetailEntity = new OauthClientDetailEntity();
		oauthClientDetailEntity.setClientId("common");
		oauthClientDetailEntity.setAccessTokenValidity(86400);
		oauthClientDetailEntity.setAdditionalInformation(null);
		oauthClientDetailEntity.setAuthorities("ROLE_SUPERADMIN,ROLE_ADMIN,ROLE_USER,ROLE_SUBADMIN");
		oauthClientDetailEntity.setAuthorizedGrantTypes("password,refresh_token");
		oauthClientDetailEntity.setClientSecret("{bcrypt}$2a$10$IfbOPaObtuq7DmQX/byZWOI73EvnBA/nc2EL.sz9TMjAxgaLmpf4G");
		oauthClientDetailEntity.setRefreshTokenValidity(31449600);
		oauthClientDetailEntity.setResourceIds(null);
		oauthClientDetailEntity.setScope("read,write,trust");
		oauthClientDetailEntity.setWebServerRedirectUri(null);
		return oauthClientDetailEntity;
	}

	/**
	 * Map person from DTO admin.
	 *
	 * @param addUserDTO the add user DTO
	 * @return the user profile
	 */
	public UserProfile mapPersonFromDTOAdmin(AdminAddUserDTO addUserDTO) {
		UserProfile profileEntity = new UserProfile();
		profileEntity.setFirstName(addUserDTO.getFirstName());
		profileEntity.setLastName(addUserDTO.getLastName());
		profileEntity.setDateOfBirth(addUserDTO.getDateOfBirth());
		profileEntity.setGender(addUserDTO.getGender());
		profileEntity.setEmail(addUserDTO.getEmail());
		profileEntity.setCellPhone(addUserDTO.getCellPhone());
		profileEntity.setCreatedDate(LocalDateTime.now());
		return profileEntity;
	}

	/**
	 * Map user from admin DTO.
	 *
	 * @param userSignupDTO the user signup DTO
	 * @return the user
	 */
	public User mapUserFromAdminDTO(AdminAddUserDTO userSignupDTO) {
		User userEntity = new User();
		userEntity.setUserName(userSignupDTO.getUserName());
		userEntity.setStatus(UserStatus.ACTIVE);
		userEntity.setPassword(passwordEncoder.encode(userSignupDTO.getPassword()));
		userEntity.setCreatedDate(LocalDateTime.now());
		return userEntity;
	}

	/**
	 * Map content from DTO.
	 *
	 * @param contentManagementDTO the content management DTO
	 * @param operationType the operation type
	 * @return the content management
	 */
	public ContentManagement mapContentFromDTO(ContentManagementDTO contentManagementDTO, String operationType) {
		ContentManagement contentManagement = new ContentManagement();
		if (operationType.equals("create")) {
			contentManagement.setPageType("cms");
			List<TranslationDTO> translationEntities = contentManagementDTO.getTranslations().stream()
					.filter(p -> p.locale.equals("en")).collect(Collectors.toList());
			String s[] = translationEntities.get(0).getPageTitle().toLowerCase().split(" ");
			for (int i = 0; i < s.length; i++) {
				if (s[i].equals("-")) {
					s[i] = null;
				}
			}
//			String slug = StringUtils.join(s, "-");
			String slug = String.join("-", s);
			contentManagement.setSlug(slug);
			contentManagement.setCreatedDate(LocalDateTime.now());
		}
		if (operationType.equals("update")) {
			contentManagement.setId(UUID.fromString(contentManagementDTO.getCmsId()));
		}
		contentManagement.setStatus(contentManagementDTO.getStatus());

		return contentManagement;
	}

	/**
	 * Map translation entity.
	 *
	 * @param translationDTOs the translation DT os
	 * @param operationType the operation type
	 * @return the list
	 */
	public List<Translation> mapTranslationEntity(List<TranslationDTO> translationDTOs, String operationType) {
		List<Translation> translationEntities = new ArrayList<Translation>();
		for (int i = 0; i < translationDTOs.size(); i++) {
			Translation entity = new Translation();
			if (operationType.equals("create")) {
				entity.setCreatedDate(LocalDateTime.now());
			}
			if (operationType.equals("update")) {
				entity.setId(UUID.fromString(translationDTOs.get(i).getId()));
			}
			entity.setDescription(translationDTOs.get(i).getDescription());
			entity.setLocale(translationDTOs.get(i).getLocale());
			entity.setMetaDescription(translationDTOs.get(i).getMetaDescription());
			entity.setMetaKeywords(translationDTOs.get(i).getMetaKeywords());
			entity.setPageTitle(translationDTOs.get(i).getPageTitle());
			translationEntities.add(entity);
		}
		return translationEntities;
	}
}
