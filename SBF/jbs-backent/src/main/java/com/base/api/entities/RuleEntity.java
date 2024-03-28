package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "rules")
@AttributeOverride(name = "id", column = @Column(name = "rule_id"))
public class RuleEntity extends BaseEntity{

	private static final long serialVersionUID = 5761227242773876348L;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "active")
	private String active;

	@Column(name = "priority")
	private int priority;

	@Column(name = "times_triggered")
	@ColumnDefault("0")
	private int timesTriggered;

	@Column(name = "on_action")
	private int onAction;

}
