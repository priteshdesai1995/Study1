package com.base.api.request.dto;

import java.io.Serializable;
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
public class CountryTranslationDTO implements Serializable {

	private static final long serialVersionUID = -1124386252624329975L;

	public String name;
	public String locale;
	public UUID transId;
	public UUID countryId;
}
