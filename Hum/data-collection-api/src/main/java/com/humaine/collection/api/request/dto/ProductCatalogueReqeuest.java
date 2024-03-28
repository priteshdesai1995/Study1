package com.humaine.collection.api.request.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Data
@ToString
public class ProductCatalogueReqeuest {

	List<CategoryWiseProductRequest> categories; 
}
