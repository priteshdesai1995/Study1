package com.base.api.response.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FAQTopicDTO {

	private String topic_name;
	private UUID id;

}
