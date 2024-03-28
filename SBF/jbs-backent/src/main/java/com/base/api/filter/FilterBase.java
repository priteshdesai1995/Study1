package com.base.api.filter;

import lombok.Data;

@Data
public class FilterBase {

	public Integer startRec = 0;
	public Integer endRec = 10;
	public String order = "createdDate";
	public String sortingOrder = "DESC";
	public String columnName;
}
