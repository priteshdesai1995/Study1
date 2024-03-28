package com.base.api.gateway.util;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MetaResponse {

	private boolean status;
	private Integer statusCode;
	private String message;
	private String messageCode;
	private int count;

	public MetaResponse(boolean status) {
		this.status = status;
	}
}
