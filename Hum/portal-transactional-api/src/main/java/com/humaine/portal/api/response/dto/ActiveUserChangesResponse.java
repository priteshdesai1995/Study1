package com.humaine.portal.api.response.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActiveUserChangesResponse {

	private Map<String, Long> hourWiseActiveUsers = new HashMap<>();
	
	private String date;
}
