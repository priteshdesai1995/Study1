package com.base.api.dto.filter;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RuleFilter extends FilterBase {

	public String name;
	public String active;
	public String description;
	public String status;
	public int priority;
	public int on_action;
	public List<Map<String, String>> filters;
	public String sort_param;
	public String sort_type;
}
