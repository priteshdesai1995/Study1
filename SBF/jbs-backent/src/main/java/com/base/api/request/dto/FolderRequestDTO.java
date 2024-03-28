package com.base.api.request.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FolderRequestDTO {
	
	private String name;
	private String folderPath;
	private UUID parentId;

}
