package com.humaine.admin.api.dto;

import com.humaine.admin.api.enums.PaginationSortDirection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaginationSort {

	String field;
	
	PaginationSortDirection direction;
}
