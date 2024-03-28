package com.humaine.portal.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.humaine.portal.api.olap.model.OLAPUserGroup;

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
@Table(name = "user_group_deleted")
public class OLAPUserGroupDeleted {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial", nullable = false, unique = true)
	Long id;

	@Column(name = "user_group_id")
	String groupId;

	@Column(name = "account_id")
	@JsonIgnore
	Long account;

	@Column(name = "group_name")
	String name;

	@Column(name = "bigfive")
	String bigFive;

	@Column(name = "value")
	String values;

	@Column(name = "persuasive")
	String persuasiveStratergies;

	@Column(name = "motivation")
	String motivationToBuy;

	@Column(name = "success_rate")
	String successMatch;

	@Column(name = "time_stamp")
	OffsetDateTime timestamp;

	public OLAPUserGroupDeleted(OLAPUserGroup userGroup) {
		if (userGroup != null) {
			this.groupId = userGroup.getGroupId();
			this.account = userGroup.getAccount();
			this.name = userGroup.getGroupId();
			this.bigFive = userGroup.getBigFive();
			this.values = userGroup.getValues();
			this.persuasiveStratergies = userGroup.getPersuasiveStratergies();
			this.motivationToBuy = userGroup.getMotivationToBuy();
			this.successMatch = userGroup.getMotivationToBuy();
//			this.timestamp = userGroup.getTimestamp();
		}
	}
}
