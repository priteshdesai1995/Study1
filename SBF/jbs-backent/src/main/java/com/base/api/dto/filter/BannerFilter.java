package com.base.api.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BannerFilter extends FilterBase{

	public String banner_title;
	public String created_by;
	public String status;
	public String userId;
	public String banner_image;
	public String sort_param;
	public String sort_type;
}
