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

@Entity
@Table(name = "saledata")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Sale {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "saleid", unique = true, nullable = false, columnDefinition = "bigserial")
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
	
	@Override
	public String toString() {
		return "Sale [id=" + id + ", user=" + user + ", account=" + account + ", session=" + session + ", product="
				+ product + ", productQuantity=" + productQuantity + ", saleAmount=" + saleAmount + ", saleOn=" + saleOn
				+ "]";
	}

}
