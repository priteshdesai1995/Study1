package com.base.api.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OfferFilter extends FilterBase {
	
	public String name;
	public String code;
	public String type;
	public int value;
	public String applicable;
	public String usage;
	public String users;
	public String start_date;
	public String end_date;
	public String status;
	public String sort_param;
	public String sort_type;
	
}
