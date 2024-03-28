package com.humaine.portal.api.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.humaine.portal.api.projection.model.ProductIntelligenceObject;

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
@Table(name = "daily_product_intelligence")
public class DailyProductIntelligence {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "daily_product_intelligence_id", columnDefinition = "bigserial")
	private Long id;
	
	@Column(name = "accountid")
	private Long accountId;
	
	@Column(name = "product_id")
	private String productId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "product_image")
	private String productImage;
	
	@Column(name = "product_metadata")
	@Type(type = "JsonType")
	private Map<String, Double> count = new HashMap<String, Double>();
	
	@Column(name = "totalqty")
	private Double totalQty;
	
	public DailyProductIntelligence(ProductIntelligenceObject obj) {
		this.productId = obj.getProductId();
		this.name = obj.getName();
		this.productImage = obj.getProductImage();
		this.totalQty = obj.getTotalQty();
	}

}
