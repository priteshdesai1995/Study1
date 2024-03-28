package com.base.api.request.dto;

import lombok.Data;

@Data
public class FiltertDTO {
	
	public Integer startRec = 0;
	public Integer endRec = 10;
	public String order = "createdDate";
	public String sortingOrder = "DESC";
	public String columnName;
}
