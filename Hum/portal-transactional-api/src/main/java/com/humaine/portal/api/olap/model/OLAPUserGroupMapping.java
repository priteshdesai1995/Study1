package com.humaine.portal.api.olap.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "user_group_mapping")
public class OLAPUserGroupMapping {

	@Id
	@Column(name = "id")
	String id;

	@Column(name = "account_id", nullable = false)
	Long account;

	@Column(name = "user_id", nullable = false)
	Long userId;

	@Column(name = "user_group_id", nullable = false)
	String userGroupId;

	@Column(name = "group_flag_id")
	Integer flag;
}
