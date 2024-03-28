package com.humaine.portal.api.response.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActiveDeviceUsedResponse {

	private String browser;
	
	private Long count;
}
