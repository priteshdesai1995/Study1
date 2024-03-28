package com.base.api.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "media")
public class Media extends BaseEntity{

	private static final long serialVersionUID = 8117821519039100421L;

	@Column(name = "name")
	private String name;
	
	@Column(name = "disk")
	private String disk;

	@Column(name = "type")
	private String type;

	@Column(name = "size")
	private String size;
	
	@Column(name = "mime_type")
	private String mimetype;

	@Column(name = "parent_id")
	private UUID parent_id;
	
	@Column(name = "folder_path")
	private String folderpath;

	@Column(name = "created_by")
	private String createdby;

	@Column(name = "media_url")
	private String media_url;

	@Column(name = "relative_path")
	private String relative_path;
	
}
