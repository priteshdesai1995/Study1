package com.humaine.portal.api.request.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MultipleDeleteRequests<T> {

	private List<T> ids; 
}
