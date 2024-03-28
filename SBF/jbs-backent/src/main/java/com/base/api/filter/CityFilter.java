package com.base.api.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CityFilter extends FilterBase {
	public String cityName;
	public String stateName;
	public String status;
}
