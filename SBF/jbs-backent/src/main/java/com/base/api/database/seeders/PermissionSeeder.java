/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.database.seeders;

import static com.base.api.constants.PermissionConstants.*;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.api.annotations.DatabaseSeeder;
import com.base.api.entities.Privilege;
import com.base.api.repository.PrivilegeRepository;

/**
 * This class save the permission to the database.
 * 
 * @author minesh_prajapati
 *
 */
//@Slf4j
@DatabaseSeeder
@Component("PermissionSeeder")
public class PermissionSeeder implements BaseSeeder {

	@Autowired
	private PrivilegeRepository privilegeRepository;

	private Set<String> permissions = new HashSet<>() {
		private static final long serialVersionUID = -6936946465811687758L;
		{
			add(ADD_CATEGORY);
			add(ADD_COUNTRY);
			add(ADD_QUESTIONS);
			add(ADD_REVIEW);
			add(ADD_USER);
			add(ANNOUNCEMENT_MANAGE);
			add(ANNOUNCEMENT_VIEW);
			add(BANNER_LIST);
			add(CATEGORY_TREE_VIEW);
			add(CHANGE_BANNER_STATUS);
			add(CHANGE_CITY_STATUS);
			add(CHANGE_COUNTRY_STATUS);
			add(CHANGE_FAQ_STATUS);
			add(CHANGE_OFFER_STATUS);
			add(CHANGE_PASSWORD);
			add(CHANGE_PLAN_STATUS);
			add(CHANGE_REPORT_STATUS);
			add(CHANGE_REVIEW_STATUS);
			add(CHANGE_STATUS);
			add(CHANGE_STATUS);
			add(CHANGE_TEMPLATES_STATUS);
			add(CREATE_BANNER);
			add(CREATE_CITY);
			add(CREATE_ENQUIRY_DATA);
			add(CREATE_EVENT);
			add(CREATE_FAQ);
			add(CREATE_FOLDER);
			add(CREATE_MEDIA);
			add(CREATE_OFFER);
			add(CREATE_PLAN);
			add(CREATE_RULE);
			add(CREATE_STATE);
			add(CREATE_SURVEY);
			add(CREATE_SUGGESTION);
			add(CREATE_TEMPLATES);
			add(DELETE_ACTIVITIES);
			add(DELETE_BANNER);
			add(DELETE_CATEGORY);
			add(DELETE_CITY);
			add(DELETE_COUNTRY_BY_ID);
			add(DELETE_ENQUIRY_DATA);
			add(DELETE_EVENT_BY_ID);
			add(DELETE_FAQ);
			add(DELETE_MEDIA);
			add(DELETE_OFFER);
			add(DELETE_PLAN);
			add(DELETE_REPORT);
			add(DELETE_REVIEW);
			add(DELETE_RULE);
			add(DELETE_STATE);
			add(DELETE_SURVEY);
			add(DELETE_USER);
			add(EDIT_REVIEW);
			add(EXPORT_CSV);
			add(EXPORT_TO_CSV);
			add(EXPORT_TO_EXCEL);
			add(EXPORT_TO_PDF);
			add(EXPORT_USER_IN_PDF);
			add(GET_ACTIVE_USERS);
			add(GET_ACTIVITIES);
			add(GET_ADMIN_LIST);
			add(GET_ALL_CATEGORY);
			add(GET_ALL_COUNTRIES);
			add(GET_ALL_ENQUIRY_DATA);
			add(GET_ALL_EVENTS);
			add(GET_ALL_FAQS);
			add(GET_ALL_FAQS_TOPICS);
			add(GET_ALL_MEDIA);
			add(GET_ALL_OFFERS);
			add(GET_ALL_PLANS);
			add(GET_ALL_REVIEWS);
			add(GET_ALL_RULES);
			add(GET_ALL_STATE);
			add(GET_ALL_SURVEYS);
			add(GET_ALL_TEMPLATES);
			add(GET_BANNER);
			add(GET_CATEGORY);
			add(GET_CITY_BY_ID);
			add(GET_CONFIGURATION);
			add(GET_COUNTRY_BY_ID);
			add(GET_DETAILS);
			add(GET_ENQUIRY_DATA);
			add(GET_EVENT_BY_ID);
			add(GET_FAQS_BY_ID);
			add(GET_LOGO);
			add(GET_OFFER);
			add(GET_PLAN);
			add(GET_PROFILE_BY_USERID);
			add(GET_QUESTIONS);
			add(GET_REPORTS);
			add(GET_RULE);
			add(GET_STATE_BY_ID);
			add(GET_SURVEYS);
			add(GET_TEMPLATE_BY_ID);
			add(GET_USER_BY_ID);
			add(GET_USER_LIST);
			add(MOVE_FOLDER);
			add(PRIVILEGE_MANAGE);
			add(RENAME_MEDIA);
			add(ROLE_MANAGE);
			add(SAMPLE_PERMISSION);
			add(SAMPLE_PERMISSION2);
			add(SAVE_CSV);
			add(SEARCH_COUNTRY);
			add(UPDATE_BANNER);
			add(UPDATE_CATEGORY);
			add(UPDATE_CATEGORY_TREE_VIEW);
			add(UPDATE_CITY);
			add(UPDATE_COUNTRY);
			add(UPDATE_COUNTRY_BY_ID);
			add(UPDATE_ENQUIRY_DATA);
			add(UPDATE_EVENT);
			add(UPDATE_FAQ);
			add(UPDATE_LOGO);
			add(UPDATE_OFFER);
			add(UPDATE_PLAN);
			add(UPDATE_QUESTIONS);
			add(UPDATE_RULE);
			add(UPDATE_RULE_STATUS);
			add(UPDATE_STATE);
			add(UPDATE_SURVEY_STATUS);
			add(UPDATE_SURVEYS);
			add(UPDATE_TEMPLATE);
			add(UPDATE_USER);
			add(UPDATE_USER);
			add(UPDATE_USER_PROFILE);
			add(USER_CHANGE_PASSWORD);
			add(USER_CHECK_PASSWORD_TOKEN_VELIDITY);
			add(USER_FORGOT_PASSWORD);
			add(USER_LIST);
			add(USER_REPORT);
			add(USER_SIGNUP);
			add(USER_LOGOUT);
			add(CREATE_BANNER);
			add(BANNER_LIST);
			add(CHANGE_BANNER_STATUS);
			add(GET_BANNER);
			add(UPDATE_BANNER);
			add(DELETE_BANNER);
			
			add(CREATE_CONTENT);
			add(UPDATE_CONTENT);
			add(GET_CONTENTS);
			add(GET_CONTENT);
			add(CHANGE_CONTENT_STATUS);
			add(CMS_PAGE_LOAD);
			add(ADD_EDIT_CONTENT);
			add(GET_CONTACTS);
		}
	};

	@Override
	public void seed() {
		for (String permission : permissions) {
			try {
				privilegeRepository.save(new Privilege(permission));
			} catch (Exception e) {
//				e.printStackTrace();
				System.err.println(e.getMessage());
				// log.error("PermissionSeeder: fail to save {}", e.getMessage());
			}
		}
	}
}
