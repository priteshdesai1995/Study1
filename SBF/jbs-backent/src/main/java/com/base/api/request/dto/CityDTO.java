package com.base.api.request.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

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
public class CityDTO implements Serializable {

	private static final long serialVersionUID = -2593018598490395472L;
	
	private UUID countryId;
	private String countryUuid;
	private String stateUuid;
	private List<CityTranslationDTO> cityTranslableDtos;
	private String status;
}
