package com.base.api.request.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MoveFolderRequestDTO {
	
	private UUID destinationId;
	private UUID sourceId;
	private Boolean specificFolder;
	
}
