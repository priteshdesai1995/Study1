package com.base.api.response.dto;

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
public class ContentListDTO {

	private String assets;
	private String cms_page_name;
	private String component;
	private String css;
	private String description;
	private String html;
	private String id;
	private String locale;
	private String meta_description;
	private String meta_keywords;
	private String page_title;
	private String page_type;
	private String slug;
	private String status;
	private String styles;
}
