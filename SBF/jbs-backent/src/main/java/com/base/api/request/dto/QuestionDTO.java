package com.base.api.request.dto;

import java.util.List;

import com.base.api.entities.AnswerEntity;
import com.base.api.entities.QuestionEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {

	private String answerType;
	private QuestionEntity question;
	private List<AnswerEntity> answers;

}