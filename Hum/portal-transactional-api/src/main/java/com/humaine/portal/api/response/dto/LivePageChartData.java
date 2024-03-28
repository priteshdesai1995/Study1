package com.humaine.portal.api.response.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LivePageChartData {

	private Map<String, Long> hourWiseData = new HashMap<>();
	
	private Long count;
	
	Long lastHourComparison;
}
