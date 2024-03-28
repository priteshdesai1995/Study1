package com.humaine.portal.api.olap.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "test_journey")
public class OLAPManualJourney {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial")
	Long id;

	@Column(name = "account_id", nullable = false)
	@JsonIgnore
	Long account;

	@Column(name = "group_id", nullable = false)
	Long groupId;

	@Column(name = "journey_success")
	Float journeySuccess;

	@Column(name = "journey_time")
	Float journeyTime;

	@Column(name = "tiimestamp")
	OffsetDateTime tiimestamp;

	public OLAPManualJourney(Long id, Long account, Long groupId, Float journeySuccess, Float journeyTime,
			OffsetDateTime tiimestamp) {
		super();
		this.id = id;
		this.account = account;
		this.groupId = groupId;
		this.journeySuccess = journeySuccess;
		this.journeyTime = journeyTime;
		this.tiimestamp = tiimestamp;
	}
}
