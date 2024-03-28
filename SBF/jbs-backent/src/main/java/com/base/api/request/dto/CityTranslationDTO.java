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
public class CityTranslationDTO implements Serializable {

	private static final long serialVersionUID = 414397231386379818L;
	private String name;
	private String locale;
	private String uuid;
}
