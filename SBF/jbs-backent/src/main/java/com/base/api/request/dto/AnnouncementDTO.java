/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.request.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.base.api.enums.Inclusion;
import com.base.api.enums.Platform;
import com.base.api.enums.Status;
import com.base.api.enums.TemplateType;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This class is a request DTO for Announcement.
 * 
 * @author minesh_prajapati
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AnnouncementDTO extends OrderAndPaginationDTO {

	@NotEmpty(message = "Title can't be null or empty")
	private String title;

//	@ApiModelProperty(notes = "type", example = "EMAIL | PUSH | SMS")
//    @NotNull(message = "Type can't be null or empty")
	private TemplateType type = TemplateType.SMS;

	//@ApiModelProperty(notes = "type", example = "ALL | ANDROID | IOS | WEB")
	@NotNull(message = "Platform can't be null or empty")
	private Platform platform;

	private Status status;

	@Future(message = "Start date can't be null or empty")
	@JsonProperty(value = "start_date")
	private LocalDate startDate;

	@Future(message = "End date can't be null or empty")
	@JsonProperty(value = "end_date")
	private LocalDate endDate;

	@NotEmpty(message = "Description can't be null or empty")
	private String description;
	// public String userType;
	// public String userRole;
	
	//@ApiModelProperty(notes = "type", example = "ALL | EXCLUDE | INCLUDE")
	@NotNull(message = "Inclusion can't be null or empty")
	public Inclusion inclusion;

	@NotEmpty(message = "Please Select user")
	public Set<UUID> usersId;
	

	@JsonProperty(value = "email_attachment")
	private MultipartFile attachmentFile;
	
	

}
