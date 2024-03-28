package com.humaine.collection.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "g_form_status")
@NoArgsConstructor
@Getter
@Setter
public class GiftCard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial")
	Long id;

	@Column(name = "created_on")
	OffsetDateTime surveyStartTime;

	@Column(name = "submitted_on")
	OffsetDateTime surveyEndTime;

	@Column(name = "user_id")
	String userId;

	@Column(name = "session_id")
	String sessionId;

	@Column(name = "account_id")
	Long accountId;

	public GiftCard(OffsetDateTime surveyStartTime, String surveyUuid, String userId, String sessionId,
			Long accountId) {
		super();
		this.surveyStartTime = surveyStartTime;
		this.userId = userId;
		this.sessionId = sessionId;
		this.accountId = accountId;
	}

}
