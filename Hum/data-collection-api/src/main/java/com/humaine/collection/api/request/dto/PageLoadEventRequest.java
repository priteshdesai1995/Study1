package com.humaine.collection.api.request.dto;

import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.humaine.collection.api.annotation.FieldValueExists;
import com.humaine.collection.api.rest.repository.AccountRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageLoadEventRequest {

	@FieldValueExists(repository = AccountRepository.class, requiredMsg = "api.error.accountID.null", requiredMsgErorCode = "api.error.accountID.null.code", message = "api.error.account.not.found", messageCode = "api.error.account.not.found.code")
	private Long accountID;
	
	@NotBlank(message = "{api.error.usereventrequest.userID.null}{error.code.separator}{api.error.usereventrequest.userID.null.code}")
	private String userID;
	
	@NotBlank(message = "{api.error.usereventrequest.sessionID.null}{error.code.separator}{api.error.usereventrequest.sessionID.null.code}")
	private String sessionID;
	
	@NotBlank(message = "{api.error.pageURL.null}{error.code.separator}{api.error.pageURL.null.code}")
	String pageURL;
	
	@NotNull(message = "{api.error.pageLoadTime.null}{error.code.separator}{api.error.pageLoadTime.null.code}")
	Long pageLoadTime;
	
	Map<String, Object> performanceData;
	
	String pageSource;
}
