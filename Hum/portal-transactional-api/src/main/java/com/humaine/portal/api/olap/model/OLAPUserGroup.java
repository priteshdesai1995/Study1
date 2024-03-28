package com.humaine.portal.api.olap.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "user_group")

public class OLAPUserGroup {

	@Id
	@Column(name = "user_group_id")
	String groupId;

	@Column(name = "account_id", nullable = false)
	@JsonIgnore
	Long account;

	@Transient
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

	@Column(name = "created_date")
	OffsetDateTime createdDate;

	@Column(name = "group_flag_id")
	Integer flag;

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

	public OLAPUserGroup(String groupId, Long account, String name) {
		this.groupId = groupId;
		this.account = account;
//		this.name = name;
	}

	public OLAPUserGroup(String groupId, Long account, String name, String bigFive, String values,
			String persuasiveStratergies, String motivationToBuy, Float successMatch, OffsetDateTime timestamp,
			Integer flag) {
		super();
		this.flag = flag;
		this.groupId = groupId;
		this.account = account;
//		this.name = name;
		this.bigFive = bigFive;
		this.values = values;
		this.persuasiveStratergies = persuasiveStratergies;
		this.motivationToBuy = motivationToBuy;
		this.successMatch = successMatch;
//		this.timestamp = timestamp;
	}

	public OLAPUserGroup(String groupId, Long account, String name, String bigFive, String values,
			String persuasiveStratergies, String motivationToBuy, Float successMatch, OffsetDateTime timestamp,
			Integer flag, Long minFamilySize, Long maxFamilySize, Long minEducation, Long maxEducation, Long minAge,
			Long maxAge, Float malePercent, Float femalePercent, Float otherPercent, Long noOfUser) {
		super();
		this.groupId = groupId;
		this.account = account;
//		this.name = name;
		this.bigFive = bigFive;
		this.values = values;
		this.persuasiveStratergies = persuasiveStratergies;
		this.motivationToBuy = motivationToBuy;
		this.successMatch = successMatch;
//		this.timestamp = timestamp;
		this.flag = flag;
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

}
