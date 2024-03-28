package com.base.api.request.dto;

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
public class TranslationDTO {

	private String pageTitle;
	private String description;
	private String metaKeywords;
	private String metaDescription;
	private String status;
	public String locale;
	private String id;
}
