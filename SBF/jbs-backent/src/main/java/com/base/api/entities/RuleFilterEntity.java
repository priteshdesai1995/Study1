package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "rule_filters")
@AttributeOverride(name = "id", column = @Column(name = "filter_id"))
public class RuleFilterEntity extends BaseEntity  {

	private static final long serialVersionUID = -7214613521105796384L;

	@Column(name = "filter_no")
	private String filterNo;

	@Column(name = "item_name")
	private String itemName;

	@Column(name = "verb_description")
	private String verbDescription;

	@Column(name = "specified_input")
	private String specifiedInput;

	@Column(name = "time_frame")
	@JsonProperty(value="time_frame")
	private String timeFrame;

	@Column(name = "action")
	private String action;

	@Column(name = "notification")
	private String notification;

	@OneToOne
	@JsonIgnore
	private RuleEntity rule;

}
