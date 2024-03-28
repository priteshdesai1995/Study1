package com.base.api.request.dto;

import java.util.List;
import java.util.UUID;

import com.base.api.entities.RuleFilterEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleFilterDTO {

	private UUID ruleId;
	private String name;
	private String description;
	private int priority;
	private int on_action;
	private List<RuleFilterEntity> filters;
}
