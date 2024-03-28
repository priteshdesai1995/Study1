package com.base.api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.base.api.entities.Enquiry;
import com.base.api.request.dto.EnquiryDto;

@Service
public interface EnquiryService {
	
	public List<Enquiry> getAllEnquiryData(EnquiryDto enquiryDto) throws Exception;

	public Enquiry getEnquiryDataById(UUID enquiryId) throws Exception;

	public void createEnquiryDataById(EnquiryDto enquiryDto);

	public void updateEnquiryDataById(UUID enquiryId, EnquiryDto enquiryDto) throws Exception;

	public void deleteEnquiryDataById(UUID enquiryId) throws Exception;

}
