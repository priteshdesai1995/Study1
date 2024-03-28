package com.humaine.portal.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "user_group_master_deleted")
public class UserGroupDeleted {

	@Column(name = "user_group_id")
	Long userGroupId;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial")
	Long id;

	@Column(name = "account_id")
	Long account;

	@Column(name = "user_group_name", length = 64)
	String name;

	Long gender;

	@Column(name = "age_group")
	Long ageGroup;

	@Column(name = "ethnicity")
	Long ethnicity;

	@Column(name = "family_size")
	Long familySize;

	@Column(name = "big_five")
	Long bigFive;

	@Column(name = "values")
	Long values;

	@Column(name = "education")
	Long education;

	@Column(name = "persuasive")
	Long persuasive;

	@Column(name = "is_external_factor")
	Boolean isExternalFactor;

	String state;

	@Column(name = "success_match")
	String successMatch;

	@Column(name = "icon", length = 255)
	String icon;

	@Column(name = "buying")
	Long buying;

	@Column(name = "createdon")
	OffsetDateTime createdOn;

	@Column(name = "modifiedon")
	OffsetDateTime modifiedOn;

	public UserGroupDeleted(UserGroup group) {
		this.userGroupId = group.getId();
		this.account = group.getAccount().getId();
		this.name = group.getName();
		this.gender = getId(group.getGender());
		this.ageGroup = getId(group.getAgeGroup());
		this.ethnicity = getId(group.getEthnicity());
		this.familySize = getId(group.getFamilySize());
		this.bigFive = getId(group.getBigFive());
		this.values = getId(group.getValues());
		this.education = getId(group.getEducation());
		this.persuasive = getId(group.getPersuasive());
		this.isExternalFactor = group.getIsExternalFactor();
		this.state = group.getState();
		this.successMatch = group.getSuccessMatch();
		this.icon = group.getIcon();
		this.buying = getId(group.getBuying());
		this.createdOn = group.getCreatedOn();
		this.modifiedOn = group.getModifiedOn();
	}

	private <T extends AttributeBaseModel> Long getId(T obj) {
		if (obj.getValue() != null) {
			return obj.getId();
		}
		return null;
	}
}
