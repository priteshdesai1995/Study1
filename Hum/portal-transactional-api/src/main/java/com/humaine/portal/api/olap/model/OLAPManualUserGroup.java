package com.humaine.portal.api.olap.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "manual_user_group")

public class OLAPManualUserGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial")
	Long id;

	@Column(name = "account_id", nullable = false)
	@JsonIgnore
	Long account;

	@Column(name = "manual_user_group_id", nullable = false)
	Long groupId;

	@Column(name = "success_rate")
	Float successMatch;

	@Column(name = "number_of_user")
	Long noOfUsers;

	@Column(name = "tiimestamp")
	OffsetDateTime tiimestamp;

	public OLAPManualUserGroup(Long id, Long account, Long groupId, Float successMatch, OffsetDateTime tiimestamp) {
		super();
		this.id = id;
		this.account = account;
		this.groupId = groupId;
		this.successMatch = successMatch;
		this.tiimestamp = tiimestamp;
	}
}
