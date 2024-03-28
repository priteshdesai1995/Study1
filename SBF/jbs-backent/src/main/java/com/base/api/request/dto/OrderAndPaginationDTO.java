/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.request.dto;

import org.springframework.data.domain.Sort;

import lombok.Data;

/**
 * This class is a request DTO for OrderAndPagination with default values.
 * 
 * @author minesh_prajapati
 *
 */
@Data
public class OrderAndPaginationDTO {
	// public int startFrom = 0;
	// public int limit = 10;
	private int pageNumber = 0;
	private int pageSize = 10;
	private String orderBy = "createdDate";
	private Sort.Direction orderType = Sort.Direction.DESC;
	public String commonSearch;
	public String startRec;
	public String endRec;
	public String order;
	public String sortingOrder;
	public String columnName;
}
