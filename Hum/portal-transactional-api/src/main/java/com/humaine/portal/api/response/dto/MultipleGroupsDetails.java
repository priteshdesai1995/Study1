package com.humaine.portal.api.response.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.humaine.portal.api.olap.model.OLAPUserGroup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MultipleGroupsDetails {

	List<String> ageGroup;

	List<String> familySize;

	public MultipleGroupsDetails(List<OLAPUserGroup> groups, Map<Long, Long> rankMap) {
		this.ageGroup = new ArrayList<>();

		this.familySize = new ArrayList<>();
	}
}
