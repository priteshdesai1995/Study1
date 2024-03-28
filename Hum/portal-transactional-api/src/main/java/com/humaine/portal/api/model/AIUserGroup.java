package com.humaine.portal.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.humaine.portal.api.olap.model.OLAPUserGroup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "ai_user_group")
public class AIUserGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial")
	Long id;

	@Column(name = "user_group_id", nullable = false)
	String userGroupId;

	@Column(name = "icon", length = 255)
	String icon;

	@Column(name = "account_id", nullable = false)
	@JsonIgnore
	Long account;

	@Column(name = "group_name")
	String name;

	@Column(name = "big_five")
	String bigFive;

	@Column(name = "value")
	String values;

	@Column(name = "persuasive")
	String persuasiveStratergies;

	@Column(name = "motivation")
	String motivationToBuy;

	@Column(name = "success_rate")
	Float successMatch;

	@Column(name = "time_stamp")
	OffsetDateTime timestamp;

	@Column(name = "minimum_family_size")
	Long minFamilySize;

	@Column(name = "maximum_family_size")
	Long maxFamilySize;

	@Column(name = "minimum_education")
	Long minEducation;

	@Column(name = "maximum_education")
	Long maxEducation;

	@Column(name = "minimum_age")
	Long minAge;

	@Column(name = "maximum_age")
	Long maxAge;

	@Column(name = "male_percent")
	Float malePercent;

	@Column(name = "female_percent")
	Float femalePercent;

	@Column(name = "other_percent")
	Float otherPercent;

	@Column(name = "number_of_user")
	Long noOfUser;

	public AIUserGroup(OLAPUserGroup group) {
		this.userGroupId = group.getGroupId();
		this.account = group.getAccount();
		this.name = group.getGroupId();
		this.bigFive = group.getBigFive();
		this.values = group.getValues();
		this.persuasiveStratergies = group.getPersuasiveStratergies();
		this.motivationToBuy = group.getMotivationToBuy();
		this.successMatch = group.getSuccessMatch();
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

	public AIUserGroup(Long id, String groupId, Long account) {
		this.id = id;
		this.account = account;
		this.userGroupId = groupId;
	}
}
