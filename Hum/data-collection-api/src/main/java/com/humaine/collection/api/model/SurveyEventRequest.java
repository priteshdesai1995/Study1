package com.humaine.collection.api.model;

import javax.validation.constraints.NotBlank;

import com.humaine.collection.api.annotation.FieldValueExists;
import com.humaine.collection.api.rest.repository.GiftCardRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyEventRequest {

	@NotBlank(message = "{api.error.usereventrequest.userID.null}{error.code.separator}{api.error.usereventrequest.userID.null.code}")
	private String userID;

	@NotBlank(message = "{api.error.usereventrequest.sessionID.null}{error.code.separator}{api.error.usereventrequest.sessionID.null.code}")
	private String sessionID;

}
