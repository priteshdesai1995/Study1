package com.humaine.portal.api.olap.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "my_user_group_master_backup")
public class MyUserGroupMasterBackUp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_group_id")
	String id;

	@Column(name = "big_five")
	String bigFive;

	@Column(name = "motivation")
	String motivation;

	@Column(name = "persuasive")
	String persuasive;

	@Column(name = "value")
	String value;

}
