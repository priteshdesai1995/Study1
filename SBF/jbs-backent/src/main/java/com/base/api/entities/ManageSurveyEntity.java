package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
public class ManageSurveyEntity extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "survey_title")
	private String surveyTitle;
	
	@Column(name = "description")
	private String description;

	@Column(name = "status")
	private String status;

	@Column(name = "survey_status")
	private String surveyStatus;
	
}


