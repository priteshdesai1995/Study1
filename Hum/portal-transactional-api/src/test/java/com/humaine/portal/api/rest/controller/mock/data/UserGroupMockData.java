package com.humaine.portal.api.rest.controller.mock.data;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.humaine.portal.api.enums.GroupFlag;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.AgeGroup;
import com.humaine.portal.api.model.BigFive;
import com.humaine.portal.api.model.Buying;
import com.humaine.portal.api.model.Education;
import com.humaine.portal.api.model.Ethnicity;
import com.humaine.portal.api.model.FamilySize;
import com.humaine.portal.api.model.Gender;
import com.humaine.portal.api.model.Persuasive;
import com.humaine.portal.api.model.UserGroup;
import com.humaine.portal.api.model.Values;
import com.humaine.portal.api.olap.model.OLAPManualUserGroup;
import com.humaine.portal.api.olap.model.OLAPUserGroup;
import com.humaine.portal.api.olap.model.OLAPUserGroupMapping;
import com.humaine.portal.api.request.dto.UserGroupRequest;
import com.humaine.portal.api.util.DateUtils;

public class UserGroupMockData {

	static OffsetDateTime timestemp = DateUtils.getCurrentTimestemp();

	final static String attributeEmptyValue = "N/A";

	// Mock Data for Gender
	public static List<Gender> genders = Arrays.asList(new Gender(1L, "MALE", timestemp),
			new Gender(2L, "FEMALE", timestemp), new Gender(3L, attributeEmptyValue, timestemp));

	// Mock Data for AgeGroup
	public static List<AgeGroup> ageGroups = Arrays.asList(new AgeGroup(1L, "Under 17", timestemp),
			new AgeGroup(2L, "18 - 24", timestemp));

	// Mock Data for FamilySize
	public static List<FamilySize> familySizes = Arrays.asList(new FamilySize(1L, "0sib", timestemp),
			new FamilySize(2L, "1Sib", timestemp));

	// Mock Data for Ethnicity
	public static List<Ethnicity> ethnicity = Arrays.asList(new Ethnicity(1L, "USA", timestemp),
			new Ethnicity(2L, "India", timestemp), new Ethnicity(3L, attributeEmptyValue, timestemp));

	// Mock Data for Values
	public static List<Values> values = Arrays.asList(new Values(1L, "Achievement", timestemp),
			new Values(2L, "self-direction", timestemp));

	// Mock Data for Persuasive Strategies
	public static List<Persuasive> persuasive = Arrays.asList(new Persuasive(1L, "authority", timestemp),
			new Persuasive(2L, "liking", timestemp));

	// Mock Data for Motivation To Buy
	public static List<Buying> buying = Arrays.asList(new Buying(1L, "Compulsive", timestemp),
			new Buying(2L, attributeEmptyValue, timestemp));

	public static List<BigFive> bigFive = new ArrayList<>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 271693808295010233L;

		{
			add(new BigFive(1L, "Agreeableness", timestemp, values.stream().collect(Collectors.toSet()),
					Arrays.asList(persuasive.get(0)).stream().collect(Collectors.toSet()), new HashSet<>()));
			add(new BigFive(2L, "Conscientiousness", timestemp, values.stream().collect(Collectors.toSet()),
					Arrays.asList(persuasive.get(1)).stream().collect(Collectors.toSet()),
					Arrays.asList(buying.get(0)).stream().collect(Collectors.toSet())));
			add(new BigFive(3L, "Openness", timestemp, values.stream().collect(Collectors.toSet()),
					Arrays.asList(persuasive.get(0)).stream().collect(Collectors.toSet()), new HashSet<>()));
		}
	};

	public static List<Education> educations = Arrays.asList(new Education(1L, "NA", timestemp),
			new Education(2L, "Less than high school", timestemp));

	public static List<UserGroup> groups = new ArrayList<>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7583148905132146812L;

		{
			add(new UserGroup(1L, AuthenticationMockData.account, "Test Group 1", genders.get(0), ageGroups.get(0),
					ethnicity.get(0), familySizes.get(0), bigFive.get(0), values.get(0), educations.get(0),
					persuasive.get(0), true, "Florida", "", "12.png", buying.get(0), timestemp, timestemp));
			add(new UserGroup(2L, new Account(2L), "Test Group 2", genders.get(0), ageGroups.get(0), ethnicity.get(0),
					familySizes.get(0), bigFive.get(0), values.get(0), educations.get(0), persuasive.get(0), true,
					"Florida", "", "12.png", buying.get(0), timestemp, timestemp));
			add(new UserGroup(3L, AuthenticationMockData.account, "Test Group 3", genders.get(0), ageGroups.get(0),
					ethnicity.get(0), familySizes.get(0), bigFive.get(0), values.get(0), educations.get(0),
					persuasive.get(0), true, "Florida", "", "12.png", buying.get(0), timestemp, timestemp));
			add(new UserGroup(4L, AuthenticationMockData.account, "Test Group 4", genders.get(0), ageGroups.get(0),
					ethnicity.get(0), familySizes.get(0), bigFive.get(0), values.get(0), educations.get(0),
					persuasive.get(0), true, "Florida", "", "12.png", buying.get(0), timestemp, timestemp));

		}
	};

	public static List<OLAPManualUserGroup> manualUserGroups = new ArrayList<>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 87708373176088233L;

		{
			add(new OLAPManualUserGroup(1L, groups.get(0).getAccount().getId(), groups.get(0).getId(), 10f, timestemp));
			add(new OLAPManualUserGroup(2L, groups.get(1).getAccount().getId(), groups.get(1).getId(), 75f, timestemp));
			add(new OLAPManualUserGroup(19L, groups.get(2).getAccount().getId(), groups.get(2).getId(), 76f,
					timestemp));
		}
	};

	public static UserGroupRequest getRequest() {

		return new UserGroupRequest("New User Group", genders.get(0).getId(), ageGroups.get(0).getId(), "Florida",
				ethnicity.get(0).getId(), familySizes.get(0).getId(), true, bigFive.get(1).getId(),
				values.get(0).getId(), persuasive.get(1).getId(), buying.get(0).getId(), educations.get(0).getId());
	}

	public static List<OLAPUserGroup> olapUserGroups = new ArrayList<>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -6236903573486752169L;
		{

			// Added For My User Group
			add(new OLAPUserGroup("1", AuthenticationMockData.account.getId(), "Group 1", bigFive.get(2).getValue(),
					"power", "authority", "impulsive", 20f, timestemp, GroupFlag.MY_USER_GROUP.value()));
			add(new OLAPUserGroup("2", AuthenticationMockData.account.getId(), "Group 2", bigFive.get(2).getValue(),
					"power", "authority", "impulsive", 30f, timestemp, GroupFlag.MY_USER_GROUP.value()));
			add(new OLAPUserGroup("3", 2L, "Group 3", bigFive.get(2).getValue(), "power", "authority", "impulsive", 40f,
					timestemp, GroupFlag.MY_USER_GROUP.value()));
			add(new OLAPUserGroup("4", AuthenticationMockData.account.getId(), "Group 4", bigFive.get(0).getValue(),
					"power", "authority", "impulsive", 50f, timestemp, GroupFlag.MY_USER_GROUP.value()));

			// Added For AI Generated UserGroup
			add(new OLAPUserGroup("5", AuthenticationMockData.account.getId(), "Group 5", "Openness", "power",
					"authority", "impulsive", 20f, timestemp, GroupFlag.AI_GENERATED.value()));
			add(new OLAPUserGroup("6", AuthenticationMockData.account.getId(), "Group 6", "Openness", "power",
					"authority", "impulsive", 30f, timestemp, GroupFlag.AI_GENERATED.value()));
			add(new OLAPUserGroup("7", 2L, "Group 7", "Openness", "power", "authority", "impulsive", 40f, timestemp,
					GroupFlag.AI_GENERATED.value()));
			add(new OLAPUserGroup("8", AuthenticationMockData.account.getId(), "Group 8", "Openness", "power",
					"authority", "impulsive", 50f, timestemp, GroupFlag.AI_GENERATED.value()));
		}
	};

	public static List<OLAPUserGroupMapping> groupMapping = new ArrayList<>() {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6236903573486752169L;
		{
			// added for Manual User Group
			add(new OLAPUserGroupMapping("1", groups.get(0).getAccount().getId(), 1L, groups.get(0).getId().toString(),
					GroupFlag.MANUAL.value()));
			add(new OLAPUserGroupMapping("2", groups.get(0).getAccount().getId(), 2L, groups.get(0).getId().toString(),
					GroupFlag.MANUAL.value()));
			add(new OLAPUserGroupMapping("3", groups.get(1).getAccount().getId(), 3L, groups.get(1).getId().toString(),
					GroupFlag.MANUAL.value()));
			add(new OLAPUserGroupMapping("4", groups.get(2).getAccount().getId(), 4L, groups.get(2).getId().toString(),
					GroupFlag.MANUAL.value()));

			// added for My User Group
			add(new OLAPUserGroupMapping("5", olapUserGroups.get(0).getAccount(), 5L, olapUserGroups.get(0).getGroupId(),
					GroupFlag.MY_USER_GROUP.value()));
			add(new OLAPUserGroupMapping("6", olapUserGroups.get(0).getAccount(), 6L, olapUserGroups.get(0).getGroupId(),
					GroupFlag.MY_USER_GROUP.value()));
			add(new OLAPUserGroupMapping("7", olapUserGroups.get(1).getAccount(), 7L, olapUserGroups.get(1).getGroupId(),
					GroupFlag.MY_USER_GROUP.value()));
			add(new OLAPUserGroupMapping("8", olapUserGroups.get(2).getAccount(), 8L, olapUserGroups.get(2).getGroupId(),
					GroupFlag.MY_USER_GROUP.value()));
			add(new OLAPUserGroupMapping("9", olapUserGroups.get(3).getAccount(), 9L, olapUserGroups.get(3).getGroupId(),
					GroupFlag.MY_USER_GROUP.value()));

			// added for AI Generated User Groups
			add(new OLAPUserGroupMapping("10", olapUserGroups.get(4).getAccount(), 10L,
					olapUserGroups.get(4).getGroupId(), GroupFlag.MY_USER_GROUP.value()));
			add(new OLAPUserGroupMapping("11", olapUserGroups.get(4).getAccount(), 11L,
					olapUserGroups.get(4).getGroupId(), GroupFlag.MY_USER_GROUP.value()));
			add(new OLAPUserGroupMapping("12", olapUserGroups.get(5).getAccount(), 12L,
					olapUserGroups.get(5).getGroupId(), GroupFlag.MY_USER_GROUP.value()));
			add(new OLAPUserGroupMapping("13", olapUserGroups.get(6).getAccount(), 13L,
					olapUserGroups.get(6).getGroupId(), GroupFlag.MY_USER_GROUP.value()));
			add(new OLAPUserGroupMapping("14", olapUserGroups.get(7).getAccount(), 14L,
					olapUserGroups.get(7).getGroupId(), GroupFlag.MY_USER_GROUP.value()));
		}
	};
}
