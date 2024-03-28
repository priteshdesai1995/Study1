package com.humaine.portal.api.request.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Data
@ToString
public class ContactUsRequest {

	@NotBlank(message = "{api.error.contact.us.fullname.null}{error.code.separator}{api.error.contact.us.fullname.null.code}")
	String fullName;
	
	@NotBlank(message = "{api.error.contact.us.email.null}{error.code.separator}{api.error.contact.us.email.null.code}")
	String email;
	
	@NotBlank(message = "{api.error.contact.us.phone.null}{error.code.separator}{api.error.contact.us.phone.null.code}")
	String phoneNumber;
	
	String message;
}
