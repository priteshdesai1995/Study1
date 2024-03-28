package com.humaine.portal.api.response.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class LiveDashboardPageResponse {

	List<TopActiveStatesData> activeStates = new ArrayList<>();

}
