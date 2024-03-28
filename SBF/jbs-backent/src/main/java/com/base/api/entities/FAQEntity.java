package com.base.api.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "faqs")
@AttributeOverride(name = "id", column = @Column(name = "faq_id"))
public class FAQEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -4749084518861396634L;

	@Column(name = "status")
	private String status;

	@Column(name = "locale")
	private String locale;

	@Column(name = "question")
	private String question;

	@Column(name = "answer")
	private String answer;

	@Column(name = "created_at")
	private Date createdAt;

	@ManyToOne()
	@JoinColumn(name = "faq_topic_id", nullable = false)
	private FAQTopicEntity faqTopicEntity;

}
