package com.base.api.request.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.base.api.entities.Category;

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
public class CategoryTreeViewDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6167649175048274470L;

	private List<Category> children;
	private String name;
	private UUID id;

}
