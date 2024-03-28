package com.humaine.transactional.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accountmaster")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "accountid", columnDefinition = "bigserial")
	Long id;

	@Column(name = "siteurl")
	String siteUrl;

	@Column(name = "status")
	String status;

	@Column(name = "username")
	String username;

	@Column(name = "email")
	String email;
	
	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
	BusinessInformation businessInformation;

	@Column(name = "sessiontimeout")
	Integer sessionTimeout;

	@Column(name = "createdon")
	OffsetDateTime createdOn;

	@Column(name = "modifiedon")
	OffsetDateTime modifiedOn;

	@ElementCollection(targetClass = String.class)
	@CollectionTable(name = "account_industries", joinColumns = @JoinColumn(name = "accountid"))
	@Column(name = "industry")
	List<String> industries = new ArrayList<String>();

	@ElementCollection(targetClass = String.class)
	@CollectionTable(name = "account_data_tracking_providers", joinColumns = @JoinColumn(name = "accountid"))
	@Column(name = "provider")
	List<String> dataTrackingProviders = new ArrayList<String>();

	public Account(Long id, String name, String siteUrl, String status, String plan, Integer sessionTimeout,
			OffsetDateTime createdOn, OffsetDateTime modifiedOn) {
		super();
		this.id = id;
		this.siteUrl = siteUrl;
		this.status = status;
		this.sessionTimeout = sessionTimeout;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
	}
}