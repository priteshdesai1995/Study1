package com.base.api.dto.filter;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AnnouncementFilter extends FilterBase{

	public String title;
	public String description;
	public String type;
	public String user_type;
	public String inclusion;
	public List<Object> users;
	public String registration_start_date;
	public String registration_end_date;
	public String status;
	public Object push_image;
	public Object email_attachment;
	public String sort_param;
	public String sort_type;
}
