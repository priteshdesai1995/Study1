package com.base.api.request.dto;

import java.util.UUID;

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
public class MediaDTO {
	
	private UUID id;
	private String name;
	private String disk;
	private String type;
	private String size;
	private String mimetype;
	private String parent_id;
	private String folderPath;
	private String createdby;
	private String media_url;
	private String relative_path;

}
