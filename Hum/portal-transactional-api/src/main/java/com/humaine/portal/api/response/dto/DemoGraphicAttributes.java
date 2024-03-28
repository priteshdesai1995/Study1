package com.humaine.portal.api.response.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.humaine.portal.api.model.AgeGroup;
import com.humaine.portal.api.model.BigFive;
import com.humaine.portal.api.model.Education;
import com.humaine.portal.api.model.Ethnicity;
import com.humaine.portal.api.model.FamilySize;
import com.humaine.portal.api.model.Gender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DemoGraphicAttributes {

	private List<Gender> gender = new ArrayList<>();
	
	private List<Education> education = new ArrayList<>();

	private List<AgeGroup> ageGroup = new ArrayList<>();

	private List<Ethnicity> ethnicity = new ArrayList<>();

	private List<FamilySize> familySize = new ArrayList<>();

	private List<BigFive> bigFive = new ArrayList<>();
}
