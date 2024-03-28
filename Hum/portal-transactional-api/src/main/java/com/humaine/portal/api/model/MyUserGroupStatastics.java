package com.humaine.portal.api.model;

import com.humaine.portal.api.response.dto.ConversionChartResultResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MyUserGroupStatastics {

	ConversionChartResultResponse<Long> overallConversionRate;
	
	ConversionChartResultResponse<Double> averageSpend;
}
