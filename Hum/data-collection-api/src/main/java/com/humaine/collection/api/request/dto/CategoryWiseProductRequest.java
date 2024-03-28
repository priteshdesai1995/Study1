package com.humaine.collection.api.request.dto;

import java.util.List;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Data
@ToString
public class CategoryWiseProductRequest {

	@JsonProperty("category_id")
	String categoryId;

	@JsonProperty("name")
	String categoryName;

	@JsonProperty("parent_category_id")
	String parentCategory;

	List<ProductRequest> products;
}
