package com.base.api.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.api.entities.Enquiry;
import com.base.api.exception.APIException;
import com.base.api.exception.DataNotFoundException;
import com.base.api.repository.EnquiryCustomRepository;
import com.base.api.repository.EnquiryRepository;
import com.base.api.request.dto.EnquiryDto;
import com.base.api.service.EnquiryService;
import com.base.api.utils.Util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author kavitha_deshagani
 * This  class is used to implement the EnquiryServiceImplementaion.
 *
 */
@Slf4j
@Service
public class EnquiryServiceImpl implements EnquiryService{
	
	@Autowired
	private EnquiryRepository enquiryRepo;

	@Autowired
	private EnquiryCustomRepository customRepo;
	
	@Override
	public List<Enquiry> getAllEnquiryData(EnquiryDto enquiryDto) throws Exception {
		List<Enquiry> enquiryList = customRepo.getAllEnquiriesWithFilters(enquiryDto);
		if (Util.isEmpty(enquiryList.get(0))) {
			log.error("getAllEnquirys data not found");
			throw new DataNotFoundException("enquiry.data.not.found");
		}
		return enquiryList;
	}
	

	@Override
	public Enquiry getEnquiryDataById(UUID enquiryId) throws Exception {

		log.info("EnquiryServiceImpl : Get-Data-ById");
		Enquiry enquiryData = enquiryRepo.getById(enquiryId);

		if (null != enquiryData) {
			log.info("Getting the Data based on Id..!");
			return enquiryData;
		} else {
			log.error("No such data is found by id :" + enquiryId);
			throw new DataNotFoundException("enquiry.data.not.found");
		}
	}
	
	@Transactional
	@Override
	public void createEnquiryDataById(EnquiryDto enquiryDto) {

		log.info("EnquiryServiceImpl : create-or-save-data");

		Enquiry createData = new Enquiry(enquiryDto);
		if (null != createData.getUuid()) {
			enquiryRepo.save(createData);
			log.info("create data successfully...!");
		} else {
			log.error("some thing went wrong while creating data");
			throw new APIException("failed.to.create.data");
		}

	}
	
	@Transactional
	@Override
	public void updateEnquiryDataById(UUID enquiryId, EnquiryDto enquiryDto) throws Exception {

		log.info("EnquiryServiceImpl : Update-Enquiry-Data");

		Optional<Enquiry> dataOptional = enquiryRepo.findById(enquiryId);
		if (dataOptional.isPresent() && null != dataOptional.get()) {
			Enquiry entity = dataOptional.get();
			enquiryRepo.save(entity);
			log.info("data updated successfully...");
		} else {
			throw new DataNotFoundException("invalid.id.was.provided..!");
		}

	}

	@Override
	public void deleteEnquiryDataById(UUID enquiryId) throws Exception {

		log.info("EnquiryServiceImpl : Delete-data");
		Optional<Enquiry> data = enquiryRepo.findById(enquiryId);

		if (null != data) {
			enquiryRepo.deleteById(enquiryId);
			log.info("data deleted successfully..!");
		} else {
			log.error("No such data is found with id :" + enquiryId);
			throw new DataNotFoundException("Based.on.id.data.is.not.found");
		}

	}
}
