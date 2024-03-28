package com.humaine.collection.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
	
	@Column(name = "accountid")
	Long account;
	
	@Column(name = "pageurl")
	String pageUrl;
	
	@Column(name = "pagename")
	String pageName;
	
	@Column(name = "createdon")
	OffsetDateTime createdOn;

	@Column(name = "modifiedon")
	OffsetDateTime modifiedOn;
}
