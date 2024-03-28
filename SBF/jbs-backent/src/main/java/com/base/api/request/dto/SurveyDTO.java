package com.base.api.request.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.base.api.dto.filter.FilterBase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SurveyDTO extends FilterBase {
	private UUID surveyId;
	public String title;
	public String description;
	public List<UUID> department_ids;
	public List<UUID> location_ids;
	public Date survey_start_date;
	public Date survey_end_date;
	public String status;
	public String survey_status;
	public String sort_param;
	public String sort_type;

}
