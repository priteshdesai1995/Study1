package com.base.api.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SuggestionFilter extends FilterBase {

	public String category_name;
	public String suggestion;
	public String posted_name;
	public String status;
	public String sort_param;
	public String sort_type;

}
