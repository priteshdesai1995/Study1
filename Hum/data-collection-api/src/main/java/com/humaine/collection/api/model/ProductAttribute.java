package com.humaine.collection.api.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

import com.humaine.collection.api.request.dto.ProductAttributeRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class ProductAttribute {

	@Column(name = "attr_name")
	String attributeName;

	@Column(name = "attr_value")
	String attributeValue;

	public ProductAttribute(ProductAttributeRequest request) {
		this.attributeName = request.getAttributeName();
		this.attributeValue = request.getAttributeValue();
	}
}
