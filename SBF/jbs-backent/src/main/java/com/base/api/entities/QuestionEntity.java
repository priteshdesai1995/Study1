package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "surveyquestions_table")
@AttributeOverride(name = "id", column = @Column(name = "question_id"))
public class QuestionEntity extends BaseEntity{

	private static final long serialVersionUID = 1L;

	@Column(name = "question")
	private String question;

	@ManyToOne
	private SurveyEntity survey;

	

}