package com.base.api.request.dto;

import java.util.List;

import com.base.api.entities.SurveyEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyReportDTO {

	private SurveyEntity survey;
	private List<QuestionDTO> questions;

}
