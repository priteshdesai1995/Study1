package com.base.api.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactFilter extends FilterBase{

	public String name;
	public String enquiryDetail;
	public String status;
}