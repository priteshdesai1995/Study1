package com.humaine.admin.api.model;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "userprofile", uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }) })
public class UserProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4435387511899406040L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_profile_id", unique = true)
	private Long userId;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "date_of_birth")
	private OffsetDateTime dateOfBirth;

	@Column(name = "gender")
	private String gender;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "cell_phone")
	private String cellPhone;

	@Column(name = "home_phone")
	private String homePhone;

	@Column(name = "work_phone")
	private String workPhone;

	@Column(name = "occupation") // nullable = false
	private String occupation;

	@Column(name = "employer") // nullable=false
	private String employer;

	@Column(name = "profile_photo_link")
	private String profilePhotoLink;

	@Column(name = "profile_photo")
	private byte[] profilePhoto;

	@Column(name = "photoType")
	private String photoType;

	@Column(name = "create_date")
	private OffsetDateTime createDate;

}
