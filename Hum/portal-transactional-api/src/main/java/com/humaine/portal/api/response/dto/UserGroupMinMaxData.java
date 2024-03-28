package com.humaine.portal.api.response.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.humaine.portal.api.enums.Genders;
import com.humaine.portal.api.model.AIUserGroup;
import com.humaine.portal.api.olap.model.OLAPUserGroup;
import com.humaine.portal.api.util.CommonUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserGroupMinMaxData {

	Long minFamilySize;

	Long maxFamilySize;

	Long minEducation;

	Long maxEducation;

	Long minAge;

	Long maxAge;

	Float malePercent;

	Float femalePercent;

	Float otherPercent;

	Long noOfUser;

	public UserGroupMinMaxData(Long minFamilySize, Long maxFamilySize, Long minEducation, Long maxEducation,
			Long minAge, Long maxAge, Float malePercent, Float femalePercent, Float otherPercent, Long noOfUser) {
		super();
		this.minFamilySize = minFamilySize;
		this.maxFamilySize = maxFamilySize;
		this.minEducation = minEducation;
		this.maxEducation = maxEducation;
		this.minAge = minAge;
		this.maxAge = maxAge;
		this.malePercent = malePercent;
		this.femalePercent = femalePercent;
		this.otherPercent = otherPercent;
		this.noOfUser = noOfUser;
	}

	public UserGroupMinMaxData(OLAPUserGroup group) {
		if (group != null) {
			this.minFamilySize = group.getMinFamilySize();
			this.maxFamilySize = group.getMaxFamilySize();
			this.minEducation = group.getMinEducation();
			this.maxEducation = group.getMaxEducation();
			this.minAge = group.getMinAge();
			this.maxAge = group.getMaxAge();
			this.malePercent = group.getMalePercent();
			this.femalePercent = group.getFemalePercent();
			this.otherPercent = group.getOtherPercent();
			this.noOfUser = group.getNoOfUser();
		}
	}

	public UserGroupMinMaxData(AIUserGroup group) {
		if (group != null) {
			this.minFamilySize = group.getMinFamilySize();
			this.maxFamilySize = group.getMaxFamilySize();
			this.minEducation = group.getMinEducation();
			this.maxEducation = group.getMaxEducation();
			this.minAge = group.getMinAge();
			this.maxAge = group.getMaxAge();
			this.malePercent = group.getMalePercent();
			this.femalePercent = group.getFemalePercent();
			this.otherPercent = group.getOtherPercent();
			this.noOfUser = group.getNoOfUser();
		}
	}

	public String getAgeRange() {
		if (minAge == null && maxAge == null) {
			return null;
		} else if (minAge != null && maxAge == null) {
			return CommonUtils.getAge(minAge);
		} else if (minAge == null && maxAge != null) {
			return CommonUtils.getAge(maxAge);
		} else if (minAge == maxAge) {
			return CommonUtils.getAge(maxAge);
		}
		List<String> result = new ArrayList<>();
		Collections.addAll(result, CommonUtils.getAge(minAge).split("-"));
		Collections.addAll(result, CommonUtils.getAge(maxAge).split("-"));
		return result.get(0) + "-" + result.get(result.size()-1);
	}

	public String getFamilySize() {
		Long min = minFamilySize;
		Long max = maxFamilySize;
		if (min == null && max == null) {
			return null;
		} else if (min != null && min > 0 && max == null) {
			return CommonUtils.getFamilySize(min);
		} else if (min == null && max != null && max > 0) {
			return CommonUtils.getFamilySize(max);
		} else if (min == max && min!=0 && max!=0) {
			return CommonUtils.getFamilySize(max);
		}
		
		List result = new ArrayList<String>();
		if (min == 0 && max > 0) {
			min = 1L;
		}
		if (min > 0) {
			result.add(CommonUtils.getFamilySize(min));
		}
		if (max > 0) {
			result.add(CommonUtils.getFamilySize(max));
		}
		return String.join("-", result);
	}

	public String getEducation() {
		Long min = minEducation;
		Long max = maxEducation;
		if (min == null && max == null) {
			return null;
		} else if (min != null && min > 0 && max == null) {
			return CommonUtils.getEducation(min);
		} else if (min == null && max != null && max > 0) {
			return CommonUtils.getEducation(max);
		} else if (min == max && min!=0 && max!=0) {
			return CommonUtils.getEducation(max);
		}
		
		List result = new ArrayList<String>();
		if (min == 0 && max > 0) {
			min = 1L;
		}
		if (min > 0) {
			result.add(CommonUtils.getEducation(min));
		}
		if (max > 0) {
			result.add(CommonUtils.getEducation(max));
		}
		return String.join("-", result);
	}

	public String getGender() {
		List<Float> percentage = new ArrayList<>();
		if (malePercent != null && malePercent > 0)
			percentage.add(malePercent);
		if (femalePercent != null && femalePercent > 0)
			percentage.add(femalePercent);
		if (otherPercent != null && otherPercent > 0)
			percentage.add(otherPercent);
		Collections.sort(percentage);

		if (percentage.isEmpty())
			return null;

		Float max = percentage.get(percentage.size() - 1);

		if (max == malePercent)
			return Genders.MALE.value();
		if (max == femalePercent)
			return Genders.FEMALE.value();
		if (max == otherPercent)
			return Genders.OTHER.value();

		return null;
	}

	private String getRange(Long min, Long max) {
		if (min == null && max == null) {
			return null;
		} else if (min != null && max == null) {
			return String.valueOf(min);
		} else if (min == null && max != null) {
			return String.valueOf(max);
		} else if (min == max) {
			return String.valueOf(max);
		}
		
		return String.valueOf(min) + "-" + String.valueOf(max);
	}
}
