package com.base.api.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.base.api.entities.Enquiry;
import com.base.api.request.dto.EnquiryDto;

@Repository
public interface EnquiryCustomRepository {
     
	List<Enquiry> getAllEnquiriesWithFilters(EnquiryDto enquiryDTO);

}
