package com.humaine.portal.api.request.dto;

import com.humaine.portal.api.enums.SortOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SortBy {

	private String field;
	
	private SortOrder order; 
}
