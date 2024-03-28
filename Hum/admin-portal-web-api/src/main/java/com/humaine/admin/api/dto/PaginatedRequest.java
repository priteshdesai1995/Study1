package com.humaine.admin.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaginatedRequest {

	Integer page;
	
	Integer size;
	
	PaginationSort sort;
}
