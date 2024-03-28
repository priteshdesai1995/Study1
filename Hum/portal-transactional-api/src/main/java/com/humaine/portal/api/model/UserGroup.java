package com.humaine.portal.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.humaine.portal.api.request.dto.UserGroupRequest;
import com.humaine.portal.api.util.DateUtils;

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
@Table(name = "user_group_master")
public class UserGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial")
	Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accountid", nullable = false)
	@JsonIgnore
	Account account;

	@Column(name = "user_group_name", length = 64, unique = true)
	@Pattern(regexp = "^[a-zA-Z0-9 _]{1,64}$",
    message = "group name must be of 1 to 64 length with no special characters")
	String name;

	@OneToOne
	@JoinColumn(name = "gender", referencedColumnName = "id")
	Gender gender;

	@OneToOne
	@JoinColumn(name = "age_group", referencedColumnName = "id")
	AgeGroup ageGroup;

	@OneToOne
	@JoinColumn(name = "ethnicity", referencedColumnName = "id")
	Ethnicity ethnicity;

	@OneToOne
	@JoinColumn(name = "family_size", referencedColumnName = "id")
	FamilySize familySize;

	@OneToOne
	@JoinColumn(name = "big_five", referencedColumnName = "id")
	BigFive bigFive;

	@OneToOne
	@JoinColumn(name = "values", referencedColumnName = "id")
	Values values;

	@OneToOne
	@JoinColumn(name = "education", referencedColumnName = "id")
	Education education;

	@OneToOne
	@JoinColumn(name = "persuasive", referencedColumnName = "id")
	Persuasive persuasive;

	@Column(name = "is_external_factor")
	Boolean isExternalFactor;

	String state;

	@Column(name = "success_match")
	String successMatch;

	@Column(name = "icon", length = 255)
	String icon;

	@OneToOne
	@JoinColumn(name = "buying", referencedColumnName = "id")
	Buying buying;

	@Column(name = "createdon")
	OffsetDateTime createdOn;

	@Column(name = "modifiedon")
	OffsetDateTime modifiedOn;

	public UserGroup(UserGroupRequest request, Account account) {
		if (request != null) {
			OffsetDateTime timestemp = DateUtils.getCurrentTimestemp();
			if (account != null) {
				this.account = new Account(account.getId());
			}
			this.name = request.getName();
			this.gender = new Gender(request.getGender());
			this.ageGroup = new AgeGroup(request.getAgeGroup());
			this.ethnicity = new Ethnicity(request.getEthnicity());
			this.familySize = new FamilySize(request.getFamilySize());
			this.bigFive = new BigFive(request.getBigFive());
			this.values = new Values(request.getValues());
			this.persuasive = new Persuasive(request.getPersuasiveStratergies());
			this.isExternalFactor = request.getIsExternalFactor();
			if (request.getState() != null)
				request.setState(request.getState().trim());
			this.state = request.getState();
			this.buying = new Buying(request.getMotivationToBuy());
			this.education = new Education(request.getEducation());
			this.createdOn = timestemp;
			this.modifiedOn = timestemp;
		}
	}

	public UserGroup(UserGroupRequest request, Account account, Long groupId) {
		this(request, account);
		this.setId(groupId);
	}
	
	public UserGroup(Account account, Long groupId) {
		this.account =  account;
		this.setId(groupId);
	}
	
	public UserGroup(Long id, Account account, String name, BigFive bigFive) {
		this.setId(id);
		this.setAccount(account);
		this.name = name;
		this.bigFive = bigFive;
	}
}
