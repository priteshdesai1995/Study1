package com.humaine.admin.api.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.humaine.admin.api.util.DateUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "accountmaster")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "accountid", columnDefinition = "bigserial")
	Long id;

	@Column(name = "siteurl")
	String siteUrl;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	AccountStatus status;

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
	@CollectionTable(name = "data_tracking_providers", joinColumns = @JoinColumn(name = "accountid"))
	@Column(name = "provider")
	List<String> dataTrackingProviders = new ArrayList<String>();

	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Page> pages = new ArrayList<Page>();

	@Column(name = "api_key", length = 64)
	String apiKey;

	@Column(name = "is_deleted")
	Boolean deleted;

	public void setRegistrationInfo(RegistrationRequest request) {
		this.setRegistrationInfo(request, false);
	}

	public void setRegistrationInfo(RegistrationRequest request, Boolean savePageInfo) {
		if (this.getBusinessInformation() == null)
			this.setBusinessInformation(new BusinessInformation());
		this.getBusinessInformation().setRegistrationInfo(request);
		Account acc = new Account();
		acc.setId(this.getId());
		if (this.getBusinessInformation().getAccount() == null) {
			this.getBusinessInformation().setAccount(acc);
		}
		if (request.getDetailedPage() == null)
			request.setDetailedPage(new ArrayList<PageMasterData>());
		OffsetDateTime timestemp = DateUtils.getCurrentTimestemp();
		this.getPages().clear();
		this.getPages().addAll(
				request.getDetailedPage().stream().map(p -> new Page(p, acc, timestemp)).collect(Collectors.toList()));
		if (request.getIndustry() == null)
			request.setIndustry(new ArrayList<String>());
		if (request.getProvider() == null)
			request.setProvider(dataTrackingProviders);
		this.setIndustries(request.getIndustry());
		this.setDataTrackingProviders(request.getProvider());
		this.setSiteUrl(request.getHomePageUrl());
	}
}