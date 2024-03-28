package com.base.api.request.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.base.api.entities.Category;
import com.base.api.entities.Translatable;

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
public class CategoryListDTO implements Serializable {

	private String description;
	private String name;
	private UUID id;
	private UUID parent_id;
	private String parent_name;
	private String status;
	private String slug;
	private LocalDateTime createdDate;
	private List<Translatable> translatable;
    public List<Category> children;
}