package com.base.api.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ActivityFilter extends FilterBase {
	public String name;
	public String email;
	public String activity_desc;
	public String from_date;
	public String to_date;
	public String sort_param;
	public String sort_type;
}
