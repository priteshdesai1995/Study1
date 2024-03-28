package com.base.api.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubscriptionFilter extends FilterBase {
	
	public String name;
	public String description;
	public String validity;
	public String price;
	public String discount;
	public String start_date;
	public String end_date;
	public String is_trial_plan;
	public String sort_param;
	public String sort_type;

}
