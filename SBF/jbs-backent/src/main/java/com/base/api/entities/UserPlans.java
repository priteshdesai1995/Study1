package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.base.api.enums.PlanType;
import com.base.api.request.dto.PlanDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "plan_details")
@AttributeOverride(name = "id", column = @Column(name = "plan_id"))
public class UserPlans extends BaseEntity {

	private static final long serialVersionUID = 1871356301526326257L;

	@Column(name = "name")
	private String name;

	@Column(name = "validity")
	private String validity;
	
	@Column(name = "description")
	private String description;

	@Column(name = "price")
	private String price;
	
	@Column(name = "discount")
	private String discount;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "plan_type")
	@Enumerated(EnumType.STRING)
	private  PlanType planType;

	@Column(name = "start_date")
	private String startDate;

	@Column(name = "end_date")
	private String endDate;
	
//	@Column(name = "plan_status")
//	@Enumerated(EnumType.STRING)
//	private PlanStatus status;
	
	//@Column(name = "plan_status")
	//@Enumerated(EnumType.STRING)
	//private UserStatus status;
	
	//@Column(name = "plan_action")
	//private String planAction;

	public UserPlans(PlanDTO planDto) {
		this.name = planDto.getName();
		this.validity = planDto.getValidity();
		this.description = planDto.getDescription();
		//this.planAction = planDto.getPlanAction();
		this.planType = planDto.getIsTrial() ? PlanType.TRIAL : PlanType.PAID;
		this.price = planDto.getPrice();
		if (planDto.getIsTrial()) {
			this.price = "0";
			this.discount = "0";
		}
		this.discount =planDto.getDiscount();
		this.startDate = planDto.getStartDate();
		this.endDate = planDto.getEndDate();
		this.status = planDto.getStatus();
	}

	public void update(PlanDTO planDTO) {
		this.setName(planDTO.getName());
		this.setValidity(planDTO.getValidity());
		this.setPrice(planDTO.getPrice());
		this.setDiscount(planDTO.getDiscount());
		this.setStartDate(planDTO.getStartDate());
		this.setEndDate(planDTO.getEndDate());
	}
	
	
}




