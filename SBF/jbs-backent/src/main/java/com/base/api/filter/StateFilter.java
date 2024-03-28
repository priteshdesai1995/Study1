package com.base.api.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StateFilter extends FilterBase {
	private String stateName;
	private String stateCode;
	private String countryName;
	private String status;
}
