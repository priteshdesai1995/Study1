package com.base.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.api.entities.Contact;
import com.base.api.filter.ContactFilter;
import com.base.api.filter.FilterBase;
import com.base.api.repository.ContactRepository;
import com.base.api.response.dto.ContactDTO;
import com.base.api.service.ContactService;
import com.base.api.utils.Util;

@Service(value = "contactService")
public class ContactServiceImpl implements ContactService {

	@Autowired
	EntityManager entityManager;
	
	@Autowired
	private ContactRepository contactRepository;
	@Override
	public List<ContactDTO> getAllContacts(ContactFilter filter) {
		
		
		if((filter.getStatus() == null || filter.getStatus().isEmpty())
				&& (filter.getName() == null || filter.getName().isEmpty())
				&& (filter.getEnquiryDetail() == null || filter.getEnquiryDetail().isEmpty())) {

			List<Contact> contactList = contactRepository.findAll();
			
			List<ContactDTO> list = new ArrayList<ContactDTO>();
			
			for (Contact contact : contactList) {
				ContactDTO contactDTO = new ContactDTO();
				contactDTO = this.mapEntityToDTO(contact);
				list.add(contactDTO);
			}
			
			return list;
		 
		}
		
		
		StringBuilder query = new StringBuilder();
		query = createCommonQuery();
		
		if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
			query.append(" and u.status LIKE '" + filter.getStatus() + "' ");
		}
		if (filter.getName() != null && !filter.getName().isEmpty()) {
			query.append(" and UPPER(co.userName) LIKE UPPER('%" + filter.getName() + "%')");
		}
		if (filter.getEnquiryDetail() != null && !filter.getEnquiryDetail().isEmpty()) {
			query.append(" and UPPER(u.contactDetails) LIKE UPPER('%" + filter.getEnquiryDetail() + "%')");
		}
		
		FilterBase filterBase = new FilterBase();
		filterBase.setColumnName(filter.getColumnName());
		filterBase.setStartRec(filter.getStartRec());
		filterBase.setEndRec(filter.getEndRec());
		filterBase.setSortingOrder(filter.getSortingOrder());
		filterBase.setOrder(filter.getOrder());
		
		String queryParam = Util.getFilterQuery(filterBase, query.toString());
		
		List<Contact> results = new ArrayList<Contact>();
		
		results = entityManager.createQuery(queryParam).setFirstResult(Integer.valueOf(filter.getStartRec()))
				.setMaxResults(Integer.valueOf(filter.getEndRec())).getResultList();
		
		List<ContactDTO> list = new ArrayList<ContactDTO>();
		for (Contact contact : results) {
			ContactDTO contactDTO = new ContactDTO();
			contactDTO = this.mapEntityToDTO(contact);
			list.add(contactDTO);
		}
		return list;
		
	}
	
	private ContactDTO mapEntityToDTO(Contact entity) {
		ContactDTO contactDTO = new ContactDTO();
		contactDTO.setContact_details(entity.getContactDetails());
		contactDTO.setCreated_at(entity.getCreatedDate());
		contactDTO.setCreated_by(entity.getUser().getId().toString());
		contactDTO.setDeleted(entity.getDeleted());
		contactDTO.setMessage(entity.getMessage());
//		contactDTO.setName(entity.getName());
		contactDTO.setStatus(entity.getStatus());
		contactDTO.setSubject(entity.getSubject());
		contactDTO.setUpdated_at(entity.getUpdatedDate());
		contactDTO.setName(entity.getName());
		contactDTO.setUuid(entity.getUuid());
		return contactDTO;
	}

	@Override
	public String changeStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteContact() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private StringBuilder createCommonQuery() {
		StringBuilder query = new StringBuilder();
		query.append("select u from ContactEntity u join u.user co where u.deleted = false");
		return query;
	}

}
