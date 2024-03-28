package com.humaine.portal.api.response.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConversionChartResultResponse<T> {

	Map<String, Double> data;
	
	T totalCount;
	
	Double percentageCount;
	
	Double lastHourComparison;
}
