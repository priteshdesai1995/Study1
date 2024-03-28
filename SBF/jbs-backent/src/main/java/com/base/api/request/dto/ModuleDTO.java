package com.base.api.request.dto;

import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleDTO {

	private UUID id;
	private String parent;
	private String type;
	private String text;
	private String permission_key;
	private Map<String, Boolean> state;

}
