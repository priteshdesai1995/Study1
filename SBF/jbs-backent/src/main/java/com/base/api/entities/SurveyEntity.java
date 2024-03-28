package com.base.api.entities;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.base.api.request.dto.SurveyDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "surveys_table")
@AttributeOverride(name = "id", column = @Column(name = "surveyId"))
public class SurveyEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "survey_title")
	private String surveyTitle; 

	@Column(name = "description")
	private String description;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "status")
	private String status;

	@Column(name = "survey_status")
	private String surveyStatus;

	public SurveyEntity(SurveyDTO surveyDTO) {
		this.description = surveyDTO.getDescription();
		this.status = surveyDTO.getStatus();
		this.surveyTitle = surveyDTO.getTitle();
		this.startDate = surveyDTO.getSurvey_start_date();
		this.endDate = surveyDTO.getSurvey_end_date();
		this.surveyStatus = surveyDTO.getSurvey_status();
	}

}
