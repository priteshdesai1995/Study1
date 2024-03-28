package com.humaine.collection.api.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "coupon_code_info")
@NoArgsConstructor
@Getter
@Setter
public class CouponCode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial")
	Long id;

	@Column(name = "coupon_code")
	String couponCode;

	@Column(name = "apply_date")
	LocalDate couponApplyDate;

}
