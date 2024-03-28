package com.humaine.portal.api.response.dto;

import java.util.List;
import java.util.Map;

import com.humaine.portal.api.enums.DurationGraphTimeUnit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionDurationChartResponse {

	Map<String, List<ChartData<Double>>> data;
	
	DurationGraphTimeUnit timeUnit;
	
	String twentyFourHoursDate;
	
	String fourtyEightHoursDate;
}
