package com.humaine.portal.api.response.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChartDataWithRange<T> {
	private Map<String, T> data;
	
	private String minDate;
	
	private String maxDate;
}
