package com.humaine.portal.api.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorField {
	private String code;
	private String message;

	@Override
	public String toString() {
		return "ErrorField [code=" + code + ", message=" + message + "]";
	}

}
