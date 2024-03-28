package com.base.api.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentPageLoadDTO {

	@JsonProperty("gjs-assets")
	private String gjsAssets;
	
	@JsonProperty("gjs-components")
	private String gjsComponents;
	
	@JsonProperty("gjs-css")
	private String gjsCSS;
	
	@JsonProperty("gjs-html")
	private String gjsHtml;
	
	@JsonProperty("gjs-styles")
	private String gjsStyles;
	
	@JsonProperty("cms_page_name")
	private String cmsPageName;

}
