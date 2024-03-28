package com.humaine.collection.api.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ProductAttributeRequest {

	@JsonProperty("attribute_value")
	String attributeValue;

	@JsonProperty("attribute_name")
	String attributeName;
}
