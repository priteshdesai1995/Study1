package com.base.api.entities;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "plan_profile", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
@AttributeOverride(name = "plan_id", column = @Column(name = "user_profile_id"))
public class PlanProfile extends BaseEntity implements Serializable{
	
	
	private static final long serialVersionUID = 1L;

	@Column(name="plan_name",unique=true,nullable = false)
	private String planName;
	
	@Column(name="plan_validity")
	private String PlanValidity;
	
	@Column(name="plan_price")
	private Integer planPrice;
	
	@Column(name="plan_type")
	private String planType;
	
}
