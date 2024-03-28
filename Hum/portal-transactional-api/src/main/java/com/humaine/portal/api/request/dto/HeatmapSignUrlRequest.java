package com.humaine.portal.api.request.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class HeatmapSignUrlRequest {

	@NotBlank(message = "{api.error.category.null}{error.code.separator}{api.error.category.null.code}")
	String category;
}
