package com.base.api.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CountryFilter extends FilterBase {
	
	public String countryCode;
	public String name;
	public String status;
}