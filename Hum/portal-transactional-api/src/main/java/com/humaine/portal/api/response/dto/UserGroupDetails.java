package com.humaine.portal.api.response.dto;

import java.time.OffsetDateTime;

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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserGroupDetails {

	Long groupId;

	String name;

	Gender gender;

	AgeGroup ageGroup;

	Ethnicity ethnicity;

	FamilySize familySize;

	BigFive bigFive;

	Values values;

	Education education;

	Persuasive persuasiveStratergies;

	Boolean isExternalFactor;

	String state;

	String successMatch;

	String icon;

	Buying motivationToBuy;

	OffsetDateTime createdOn;

	OffsetDateTime modifiedOn;

	public UserGroupDetails(UserGroup group) {
		this.groupId = group.getId();
		this.name = group.getName();
		this.gender = group.getGender();
		this.ageGroup = group.getAgeGroup();
		this.ethnicity = group.getEthnicity();
		this.familySize = group.getFamilySize();
		this.bigFive = group.getBigFive();
		this.values = group.getValues();
		this.education = group.getEducation();
		this.persuasiveStratergies = group.getPersuasive();
		this.isExternalFactor = group.getIsExternalFactor();
		this.state = group.getState();
		this.successMatch = group.getSuccessMatch();
		this.icon = group.getIcon();
		this.motivationToBuy = group.getBuying();
		this.createdOn = group.getCreatedOn();
		this.modifiedOn = group.getModifiedOn();
	}
}
