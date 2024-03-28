package com.humaine.portal.api.response.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class LiveDashboardChartData {

	private LivePageChartData totalSessions;

	private ConversionChartResultResponse overallConversionRate;

	private ConversionChartResultResponse totalConversions;

	private ConversionChartResultResponse activeUsers;
}
