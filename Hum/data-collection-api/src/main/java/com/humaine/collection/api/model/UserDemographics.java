package com.humaine.collection.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "userdemographics")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDemographics {

	@Id
	@Column(name = "userid")
	String user;

	@Column(name = "accountid")
	Long account;

	@Column(name = "age")
	Long age;

	@Column(name = "gender")
	String gender;

	@Column(name = "education")
	String education;

	@Column(name = "familysize")
	Long familySize;

	@Column(name = "rece")
	String rece;

	@Column(name = "income")
	Float income;

	public UserDemographics(String user, Long account) {
		super();
		this.user = user;
		this.account = account;
	}
}
