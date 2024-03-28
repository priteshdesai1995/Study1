package com.humaine.collection.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
@Table(name = "saledataarchive", indexes = {
		@Index(name = "index_saledataarchive", columnList = "saleid,userid, accountid") })
@NoArgsConstructor
@Getter
@Setter
public class SaleDataArchive {

	@Column(name = "saleid", columnDefinition = "bigserial")
	Long id;

	@Column(name = "userid")
	String user;

	@Column(name = "accountid")
	Long account;

	@Column(name = "sessionid")
	String session;

	@Column(name = "productid")
	String product;

	@Column(name = "productquantity")
	Long productQuantity;

	@Column(name = "saleamount")
	Float saleAmount;

	@Column(name = "saleon")
	OffsetDateTime saleOn;

	@Column(name = "usereventid")
	Long userEventId;
}
