package com.base.api.request.dto;

import java.util.List;
import java.util.UUID;

import com.base.api.entities.Translatable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryManagementDTO {
	private String status;
	private UUID parent_id;
	private List<Translatable> translable;
}
