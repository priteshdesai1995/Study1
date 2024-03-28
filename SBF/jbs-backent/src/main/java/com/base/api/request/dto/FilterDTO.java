package com.base.api.request.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FilterDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	public Integer startRec = 0;
	public Integer endRec = 10;
	public String order = "createdDate";
	public String sortingOrder = "DESC";
	public String columnName;
}
