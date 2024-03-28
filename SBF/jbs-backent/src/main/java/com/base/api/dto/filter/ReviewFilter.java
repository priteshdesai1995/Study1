package com.base.api.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data 
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewFilter extends FilterBase {
	
	public String search_keyword;
	public String sort_param;
	public String sort_type;
	
}

