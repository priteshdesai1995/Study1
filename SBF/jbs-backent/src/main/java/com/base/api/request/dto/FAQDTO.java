package com.base.api.request.dto;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class FAQDTO {

	private String answer;
	private String question;
	private String topic_name;
	private String status;
	private UUID faq_topic_id;
	private UUID id;
	private String locale;
	private Date created_at;

}
