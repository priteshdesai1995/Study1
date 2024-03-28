package com.base.api.entities;

import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.base.api.enums.EnquiryStatus;
import com.base.api.request.dto.EnquiryDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="enquiry_table")
@AttributeOverride(name = "UUID", column = @Column(name = "enquiry_id"))
public class Enquiry extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "status")
	private EnquiryStatus status;

	@Column(name = "name")
	private String name;

	@Column(name = "uuid")
	@NotEmpty(message = "uuid must not be null or empty")
	private UUID uuid;

	@OneToOne()
	private User user;

	@Column(name = "enquiry_details")
	private String enquiryDetails;

	@Column(name = "message")
	private String message;

	@Column(name = "subject")
	private String subject;

	@Column(name = "deleted", columnDefinition = "boolean default false")
	private Boolean deleted = false;

	public Enquiry(EnquiryDto enquiryDto) {

		this.enquiryDetails = enquiryDto.getEnquiryDetails();
		this.name = enquiryDto.getName();
		this.message = enquiryDto.getMessage();
		this.status = enquiryDto.getStatus();
		this.subject = enquiryDto.getSubject();
		this.deleted = enquiryDto.isDeleted();
	}

	public void update(EnquiryDto enquiryDto) {
		this.setEnquiryDetails(enquiryDto.getEnquiryDetails());
		this.setName(enquiryDto.getName());
		this.setMessage(enquiryDto.getMessage());
		this.setStatus(enquiryDto.getStatus());
		this.setSubject(enquiryDto.getSubject());
		this.setDeleted(enquiryDto.isDeleted());
	}

}
