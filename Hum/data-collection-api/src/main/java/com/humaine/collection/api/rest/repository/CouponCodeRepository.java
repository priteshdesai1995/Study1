package com.humaine.collection.api.rest.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.collection.api.model.CouponCode;

@Repository
public interface CouponCodeRepository extends JpaRepository<CouponCode, Long> {

	@Query("SELECT c FROM CouponCode c WHERE couponApplyDate =:currentDate")
	CouponCode findCouponCodeByCurrentDate(@Param("currentDate") LocalDate currentDate);

}
