package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author preyansh_prajapati
 *
 *         This is the entity class for report
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "reports")
@Entity
@AttributeOverride(name = "id", column = @Column(name = "report_id"))
public class Report extends BaseEntity {

	private static final long serialVersionUID = 7039259336610702771L;

	@OneToOne(cascade = { CascadeType.ALL })
	private User username;

	@OneToOne(cascade = { CascadeType.ALL })
	private User reportedBy;

	@Column(name = "category")
	private String category;

	@Column(name = "note")
	private String note;

	@Column(name = "status")
	private String status;

}
