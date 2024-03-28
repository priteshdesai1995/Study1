package com.humaine.portal.api.response.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChartData<T> {

	private String key;
	private String date;
	private String fullDate;
	private T value;
}
