package com.base.api.request.dto;

import java.util.UUID;

import com.base.api.entities.UserPlans;
import com.base.api.enums.PlanType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PlanDTO extends FilterDTO {

	private static final long serialVersionUID = 4390202976844404737L;

	@JsonProperty(value = "subscriptionId")
	private UUID planId;
	
	@JsonProperty(value = "name")
	private String name;

	@JsonProperty(value = "validity")
	private String validity;
	
	@JsonProperty(value = "description")
	private String description;
	
	private String price;
	
	@JsonProperty(value = "discount")
	private String discount;

	@JsonProperty(value = "is_trial_plan")
	private Boolean isTrial = false;

	private PlanType planType;
	
	//private PlanStatus status = PlanStatus.ACTIVE;
	private String status = "Active";
	
	@JsonProperty(value = "start_date")
	private String startDate;

	@JsonProperty(value = "end_date")
	private String endDate;
	
	//private String planAction;

	public PlanDTO(UserPlans plan) {
		this.planId = plan.getId();
		this.description = plan.getDescription();
		this.name = plan.getName();
		this.endDate = plan.getEndDate();
		this.startDate = plan.getStartDate();
		this.price = plan.getPrice();
		this.validity = plan.getValidity();
		this.discount = plan.getDiscount();
		this.planType = plan.getPlanType();
	}
}
