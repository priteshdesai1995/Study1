package com.base.api.entities;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "suggestions")
@AttributeOverride(name = "id", column = @Column(name = "suggestion_id"))
public class Suggestion extends BaseEntity implements Serializable {


	private static final long serialVersionUID = 2728693084624329812L;

	@Column(name = "category")
	private String category;

	@Column(name = "suggestion")
	private String suggestion;

	@Column(name = "status")
	private String status;

	@OneToOne(fetch =  FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;

	@Column(name = "notes")
	private String notes;
}
