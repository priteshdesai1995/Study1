package com.humaine.portal.api.response.dto;

import com.humaine.portal.api.model.AttributeBaseModel;
import com.humaine.portal.api.model.UserGroup;

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
public class UserGroupResponse {

	Long groupId;

	String name;

	String gender;

	String ageGroup;

	String state;

	String ethnicity;

	String familySize;

	Boolean isExternalFactor;

	String bigFive;

	String values;

	String persuasiveStratergies;

	String motivationToBuy;

	String education;

	String icon;

	Float successMatch;

	public UserGroupResponse(UserGroup group, String emptyValue) {
		if (group != null) {
			this.groupId = group.getId();
			this.name = group.getName();
			this.gender = getValue(group.getGender());
			this.ageGroup = getValue(group.getAgeGroup());
			this.state = group.getState();
			if (!emptyValue.equals(getValue(group.getEthnicity()))) {
				this.ethnicity = getValue(group.getEthnicity());
			}
			if (!emptyValue.equals(getValue(group.getGender()))) {
				this.gender = getValue(group.getGender());
			}
			this.familySize = getValue(group.getFamilySize());
			this.isExternalFactor = group.getIsExternalFactor();
			this.bigFive = getValue(group.getBigFive());
			this.values = getValue(group.getValues());
			this.persuasiveStratergies = getValue(group.getPersuasive());
			this.motivationToBuy = getValue(group.getBuying());
			this.education = getValue(group.getEducation());
			this.icon = group.getIcon();
		}
	}

	private <T extends AttributeBaseModel> String getValue(T obj) {
		if (obj.getValue() != null) {
			return obj.getValue();
		}
		return null;
	}
}
