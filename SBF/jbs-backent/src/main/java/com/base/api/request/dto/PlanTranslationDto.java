package com.base.api.request.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class PlanTranslationDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2207027785724658395L;

	private String planName;
	private String planType;
	private String planValidity ;
	private Integer planId;
}
