package com.base.api.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MediaFilter extends FilterBase{
	
	public String folderPath;
	public String filterType;
	public String searchBy;
	public String orderBy;
	public String sortBy;

}
