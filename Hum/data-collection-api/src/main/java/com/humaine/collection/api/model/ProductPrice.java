package com.humaine.collection.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.humaine.collection.api.request.dto.ProductRequest;
import com.humaine.collection.api.util.DateUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_price")
@NoArgsConstructor
@Getter
@Setter
public class ProductPrice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial")
	Long id;

	@ManyToOne
	@JoinColumn(name = "account_id")
	Account account;

	@Column(name = "price")
	String price;

	@ManyToOne
	@JoinColumn(name = "product_id")
	Product product;

	@Column(name = "special_price")
	String specialPrice;

	@Column(name = "created_date")
	OffsetDateTime createdOn;
	
	public ProductPrice(Account account, ProductRequest req, Product p) {
		this.account = account;
		this.product = p;
		this.price = req.getPrice();
		this.specialPrice = req.getSpecialPrice();
		this.createdOn = DateUtils.getCurrentTimestemp();
	}
}
