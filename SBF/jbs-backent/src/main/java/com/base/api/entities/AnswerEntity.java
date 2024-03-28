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
@Table(name = "surveyanswers_table")
@AttributeOverride(name = "id", column = @Column(name = "answer_id"))
public class AnswerEntity extends BaseEntity{

	private static final long serialVersionUID = 1L;

	@Column(name = "answer")
	private String answer;

	@Column(name = "answer_type")
	private String answerType;

	@ManyToOne
	private QuestionEntity question;
}
