package com.base.api.request.dto;

import com.base.api.enums.EnquiryStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnquiryDto extends FilterDTO {


	private static final long serialVersionUID = 1L;

	private String enquiryDetails;

	private String name;

	private String message;

	private EnquiryStatus status;

	private String subject;

	private boolean deleted;

}
