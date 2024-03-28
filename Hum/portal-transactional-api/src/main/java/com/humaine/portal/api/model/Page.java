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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.humaine.portal.api.request.dto.PageMasterData;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pagemaster")
@NoArgsConstructor
@Getter
@Setter
public class Page {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pageid", columnDefinition = "bigserial")
	Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accountid", nullable = false)
	@JsonIgnore
	Account account;

	@Column(name = "pageurl")
	String pageUrl;

	@Column(name = "pagename")
	String pageName;

	@Column(name = "createdon")
	OffsetDateTime createdOn;

	@Column(name = "modifiedon")
	OffsetDateTime modifiedOn;

	public Page(PageMasterData page, Account account, OffsetDateTime timestemp) {
		this.pageName = page.getPageName();
		this.pageUrl = page.getPageURL();
		this.account = account;
		this.createdOn = timestemp;
		this.modifiedOn = timestemp;
	}

}
