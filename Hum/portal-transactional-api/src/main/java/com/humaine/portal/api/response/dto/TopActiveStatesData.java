package com.humaine.portal.api.response.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class TopActiveStatesData {

	String state;
	
	Long totalSoldAmount;
}
