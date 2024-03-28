package com.humaine.collection.api.response.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class SurveyStatusResponse {

	private Boolean submitted;
	
	private String promoCode;
	
}
