package com.humaine.portal.api.rest.controller.mock.data;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.humaine.portal.api.enums.JourneyElementMasterIds;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.BigFive;
import com.humaine.portal.api.model.JourneyElementMaster;
import com.humaine.portal.api.model.TestJourney;
import com.humaine.portal.api.model.UserGroup;
import com.humaine.portal.api.request.dto.CreateTestJourneyRequest;
import com.humaine.portal.api.util.DateUtils;

public class JournyMockData {

	public static OffsetDateTime timestemp = DateUtils.getCurrentTimestemp();

	public static final String BLOG_POST = "Blog Post";
	public static final String COLLECTIONS = "Collections";
	public static final String ADD_TO_CART = "Add To Cart";
	public static final String BUY = "Buy";
	public static final String RATE_PRODUCT = "Rate Product";

	public static List<JourneyElementMaster> elementMaster = Arrays.asList(
			new JourneyElementMaster(JourneyElementMasterIds.FIRST_INTEREST.value(), "FIRST_INTEREST",
					Arrays.asList(BLOG_POST)),
			new JourneyElementMaster(JourneyElementMasterIds.DECISION.value(), "DECISION", Arrays.asList(COLLECTIONS)),
			new JourneyElementMaster(JourneyElementMasterIds.PURCHASE_ADD_TO_CART.value(), "PURCHASE_ADD_TO_CART",
					Arrays.asList(ADD_TO_CART)),
			new JourneyElementMaster(JourneyElementMasterIds.PURCHASE_BUY.value(), "PURCHASE_BUY", Arrays.asList(BUY)),
			new JourneyElementMaster(JourneyElementMasterIds.PURCHASE_RATE_PRODUCT.value(), "PURCHASE_RATE_PRODUCT",
					Arrays.asList(RATE_PRODUCT)));

	public static List<BigFive> bigFive = new ArrayList<>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 271693808295010233L;

		{
			add(new BigFive(1L, "Agreeableness", timestemp, new HashSet<>(), new HashSet<>(), new HashSet<>()));
			add(new BigFive(2L, "Conscientiousness", timestemp, new HashSet<>(), new HashSet<>(), new HashSet<>()));
		}
	};

	public static List<UserGroup> groups = new ArrayList<>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7583148905132146812L;

		{
			add(new UserGroup(1L, AuthenticationMockData.account, "Test Group 1", bigFive.get(0)));
			add(new UserGroup(2L, new Account(2L), "Test Group 2", bigFive.get(0)));
			add(new UserGroup(3L, AuthenticationMockData.account, "Test Group 3", bigFive.get(0)));
		}
	};

	public static List<TestJourney> journeys = new ArrayList<>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7583148905132146812L;

		{
			add(new TestJourney(1L, groups.get(0).getId(), groups.get(0).getAccount()));
			add(new TestJourney(2L, groups.get(1).getId(), groups.get(1).getAccount()));
			add(new TestJourney(3L, groups.get(2).getId(), groups.get(2).getAccount()));
		}
	};

	public static CreateTestJourneyRequest getRquest() {
		return new CreateTestJourneyRequest(groups.get(0).getId(), BLOG_POST, COLLECTIONS, null, null, RATE_PRODUCT);
	}
}
