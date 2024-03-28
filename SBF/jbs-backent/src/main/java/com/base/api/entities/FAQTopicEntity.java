package com.base.api.entities;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "faq_topic")
@AttributeOverride(name = "id", column = @Column(name = "faq_topic_id"))
public class FAQTopicEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1425512076681661391L;

	@Column(name = "topic_name")
	private String topicName;

}
