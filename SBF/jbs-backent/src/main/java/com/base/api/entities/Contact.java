package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "contacts")
@AttributeOverride(name = "id", column = @Column(name = "contact_id"))
public class Contact extends BaseEntity {
	private static final long serialVersionUID = -3348371671622292129L;

	@Column(name = "status")
	private String status;
	
	@Column(name = "name")
	private String name;
	
	@OneToOne()
	private User user;

	@Column(name = "contact_details")
	private String contactDetails;

	@Column(name = "message")
	private String message;

	@Column(name = "subject")
	private String subject;
	
	@Column(name = "deleted", columnDefinition = "boolean default false")
	private Boolean deleted = false;
	
	@Column(name = "uuid")
	@NotEmpty(message = "uuid must not be null or empty")
	private String uuid;
	
}
