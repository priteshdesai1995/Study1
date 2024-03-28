package com.base.api.repository.Impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.base.api.entities.Enquiry;
import com.base.api.repository.EnquiryCustomRepository;
import com.base.api.request.dto.EnquiryDto;
import com.base.api.utils.Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class EnquityCustomRepositoryImpl implements  EnquiryCustomRepository{
	
	@Autowired 
	EntityManager entityManager;

	@Override
	public List<Enquiry> getAllEnquiriesWithFilters(EnquiryDto enquiryDTO) {
		
		log.info("EnquityCustomRepositoryImpl: Start getCustomAllEnquiries");
		String stringQuery = createEnquireClauseQuery(enquiryDTO).toString();
		List<Enquiry> enquiries = entityManager.createQuery(stringQuery)
				.setFirstResult(enquiryDTO.getStartRec())
				.setMaxResults(enquiryDTO.getEndRec()).getResultList();
		log.info("EnquityCustomRepositoryImpl: End getCustomAllEnquires");
		return enquiries;
	}


	private StringBuffer createEnquireClauseQuery(EnquiryDto enquiryDTO) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT e FROM EnquiryEntity AS e WHERE 1=1");
		if (!Util.isEmpty(enquiryDTO.getEnquiryDetails())) {
			query.append(" AND e.enquiryDetails like '%" + enquiryDTO.getEnquiryDetails() + "%'");
		}
		if (!Util.isEmpty(enquiryDTO.getMessage())) {
			query.append(" AND e.message like '%" + enquiryDTO.getMessage() + "%'");
		}
		if (!Util.isEmpty(enquiryDTO.getName())) {
			query.append(" AND e.name like '%" + enquiryDTO.getName() + "%'");
		}
		if (!Util.isEmpty(enquiryDTO.getSubject())) {
			query.append(" AND e.subject like '%" + enquiryDTO.getSubject() + "%'");
		}
	
		if (!Util.isEmpty(enquiryDTO.getStatus())) {
			query.append(" AND e.status = '" + enquiryDTO.getStatus() + "'");
		}

		if (!Util.isEmpty(enquiryDTO.getOrder())) {
			query.append(" ORDER BY e." + enquiryDTO.getOrder());
		}

		if (!Util.isEmpty(enquiryDTO.getOrder())) {
			query.append(" " + enquiryDTO.getSortingOrder());
		}

		return query;
	}
}
