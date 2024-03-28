package com.base.api.service;

import java.util.List;

import com.base.api.filter.ContactFilter;
import com.base.api.response.dto.ContactDTO;

public interface ContactService {
	
	List<ContactDTO> getAllContacts(ContactFilter contactFilter);
	
	String changeStatus();
	
	String deleteContact();
}
