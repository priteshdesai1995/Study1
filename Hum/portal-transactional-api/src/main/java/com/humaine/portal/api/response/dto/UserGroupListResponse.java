package com.humaine.portal.api.response.dto;

import com.humaine.portal.api.model.AttributeBaseModel;
import com.humaine.portal.api.model.UserGroup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserGroupListResponse {
	
	Long groupId;

	String name;

	String bigFive;
	
	String icon;

	Float successMatch;
	
	Long userCount;
	
	public UserGroupListResponse(UserGroup group, String emptyValue) {
		if (group != null) {
			this.groupId = group.getId();
			this.name = group.getName();
			this.bigFive = getValue(group.getBigFive());
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
