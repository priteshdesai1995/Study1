package com.base.api.dto.filter;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SurveyFilter extends FilterBase {
	public String title;
	public String description;
	public List<Integer> department_ids;
	public List<Integer> location_ids;
	public Date survey_start_date;
	public Date survey_end_date;
	public String status;
	public String survey_status;
	public String sort_param;
	public String sort_type;
}
