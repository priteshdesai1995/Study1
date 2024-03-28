package com.humaine.portal.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "accounttracker")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AccountTracker {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial")
	Long id;

	@OneToOne
	@JsonIgnore
	@JoinColumn(name = "accountid", referencedColumnName = "accountid")
	Account account;

	String email;

	String filename;

	@Column(name = "ismailsend")
	Boolean isEmailSend;

	String description;

	@Column(name = "createdon")
	OffsetDateTime createdOn;

	@Column(name = "modifiedon")
	OffsetDateTime modifiedOn;

	public AccountTracker(Account ac, String filename, Boolean isEmailSend, String description, OffsetDateTime created,
			OffsetDateTime updated) {
		Account acc = new Account();
		acc.setId(ac.getId());
		this.account = acc;
		this.email = ac.getEmail();
		this.filename = filename;
		this.description = description;
		this.isEmailSend = isEmailSend;
		this.createdOn = created;
		this.modifiedOn = updated;
	}
}
