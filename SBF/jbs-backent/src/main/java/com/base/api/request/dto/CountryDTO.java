package com.base.api.request.dto;

import java.util.List;

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
public class CountryDTO {

	public String countryCode;
	public String status;
	public List<CountryTranslationDTO> locationtranslableDtos;
	
}
