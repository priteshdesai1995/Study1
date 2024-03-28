package com.humaine.portal.api.response.dto;

import java.util.List;
import java.util.Map;

import com.humaine.portal.api.model.AIUserGroup;
import com.humaine.portal.api.model.BigFive;
import com.humaine.portal.api.model.PersonaDetailsMaster;
import com.humaine.portal.api.model.UserGroup;
import com.humaine.portal.api.olap.model.OLAPUserGroup;
import com.humaine.portal.api.util.CommonUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Persona {

	Long userCount;

	String ageGroup;

	String state;

	String familySize;

	String gender;

	String education;

	String bigFive;

	GroupDetail group;

	PersonaDetails details;

	public Persona(Long userCount, Long rank, UserGroup group) {
		this.userCount = userCount;
		if (group != null) {
			this.group = new GroupDetail(rank, group.getName());
			this.ageGroup = group.getAgeGroup().getValue();
			this.state = group.getState();
			this.gender = group.getGender().getValue();
			this.education = group.getEducation().getValue();
			this.bigFive = group.getBigFive().getValue();
			this.details = new PersonaDetails(group);
			this.familySize = group.getFamilySize().getValue();
		}
	}
	
	public Persona(Long userCount, Long rank, UserGroup group, PersonaDetailsMaster pm) {
		this.userCount = userCount;
		if (group != null) {
			this.group = new GroupDetail(rank, group.getName());
			this.ageGroup = group.getAgeGroup().getValue();
			this.state = group.getState();
			this.gender = group.getGender().getValue();
			this.education = group.getEducation().getValue();
			this.bigFive = group.getBigFive().getValue();
			this.details = new PersonaDetails(pm);
			this.familySize = group.getFamilySize().getValue();
		}
	}

	public Persona(Long userCount, Long rank, OLAPUserGroup group, BigFive bigFive) {
		this.userCount = userCount;
		if (group != null) {
			UserGroupMinMaxData rangeData = new UserGroupMinMaxData(group);
			this.group = new GroupDetail(rank, group.getGroupId());
			this.state = null;
			this.ageGroup = rangeData.getAgeRange();
			this.gender = rangeData.getGender();
			this.education = rangeData.getEducation();
			this.familySize = rangeData.getFamilySize();
			this.bigFive = group.getBigFive();
			this.details = new PersonaDetails(group, bigFive);
		}
	}
	
	public Persona(Long userCount, Long rank, OLAPUserGroup group, BigFive bigFive, PersonaDetailsMaster pm) {
		this.userCount = userCount;
		if (group != null) {
			UserGroupMinMaxData rangeData = new UserGroupMinMaxData(group);
			this.group = new GroupDetail(rank, group.getGroupId());
			this.state = null;
			this.ageGroup = rangeData.getAgeRange();
			this.gender = rangeData.getGender();
			this.education = rangeData.getEducation();
			this.familySize = rangeData.getFamilySize();
			this.bigFive = group.getBigFive();
			this.details = new PersonaDetails(group, bigFive, pm);
		}
	}

	public Persona(Long userCount, OLAPUserGroup topGroup, Long topGroupRank, List<OLAPUserGroup> groups,
			BigFive bigFive, UserGroupMinMaxData rangeData) {
		this.userCount = userCount;
		if (groups != null) {
			this.group = new GroupDetail(topGroupRank, topGroup.getGroupId());
			this.state = null;
			if (rangeData != null) {
				this.ageGroup = rangeData.getAgeRange();
				this.gender = rangeData.getGender();
				this.education = rangeData.getEducation();
				this.familySize = rangeData.getFamilySize();
			}
			this.bigFive = bigFive.getValue();
			this.details = new PersonaDetails(groups, bigFive);
		}
	}
	
	public Persona(Long userCount, OLAPUserGroup topGroup, Long topGroupRank, List<OLAPUserGroup> groups,
			BigFive bigFive, UserGroupMinMaxData rangeData, Map<String, PersonaDetailsMaster> personaDetailsMap) {
		this.userCount = userCount;
		if (groups != null) {
			this.group = new GroupDetail(topGroupRank, topGroup.getGroupId());
			this.state = null;
			if (rangeData != null) {
				this.ageGroup = rangeData.getAgeRange();
				this.gender = rangeData.getGender();
				this.education = rangeData.getEducation();
				this.familySize = rangeData.getFamilySize();
			}
			this.bigFive = bigFive.getValue();
			this.details = new PersonaDetails(groups, bigFive, personaDetailsMap);
		}
	}

	public Persona(Long userCount, Long rank, AIUserGroup group, BigFive bigFive) {
		this.userCount = userCount;
		if (group != null) {
			UserGroupMinMaxData rangeData = new UserGroupMinMaxData(group);
			this.group = new GroupDetail(rank, group.getName());
			this.state = null;
			this.ageGroup = rangeData.getAgeRange();
			this.gender = rangeData.getGender();
			this.education = rangeData.getEducation();
			this.bigFive = group.getBigFive();
			this.details = new PersonaDetails(group, bigFive);
			this.familySize = rangeData.getFamilySize();
		}
	}
	
	public Persona(Long userCount, Long rank, AIUserGroup group, BigFive bigFive, PersonaDetailsMaster pm) {
		this.userCount = userCount;
		if (group != null) {
			UserGroupMinMaxData rangeData = new UserGroupMinMaxData(group);
			this.group = new GroupDetail(rank, group.getName());
			this.state = null;
			this.ageGroup = rangeData.getAgeRange();
			this.gender = rangeData.getGender();
			this.education = rangeData.getEducation();
			this.bigFive = group.getBigFive();
			this.details = new PersonaDetails(group, bigFive, pm);
			this.familySize = rangeData.getFamilySize();
		}
	}
}
