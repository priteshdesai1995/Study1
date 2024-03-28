package com.humaine.admin.api.model;

import java.io.Serializable;
import java.text.ParseException;
import java.time.OffsetDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6658802746083474165L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", unique = true)
	private Long userId;

	@Column(name = "user_name", unique = true, nullable = false)
	private String userName;

	@Column(name = "status", nullable = false)
	private String status;

	@Column(name = "password", nullable = false, length = 1000)
	private String password;

	@Column(name = "create_date")
	private OffsetDateTime createDate;

	@Column(name = "isAccountNonLocked", nullable = false, columnDefinition = "boolean default true")
	private Boolean accountNonLocked = true;

	@Column(name = "isAccountNonExpired", nullable = false, columnDefinition = "boolean default true")
	private Boolean accountNonExpired = true;

	@Column(name = "isCredentialsNonExpired", nullable = false, columnDefinition = "boolean default true")
	private Boolean credentialsNonExpired = true;

	@Column(name = "isEnabled", nullable = false, columnDefinition = "boolean default true")
	private Boolean enabled = true;

	@OneToOne()
	private UserRole userRole;

	@OneToOne(cascade = CascadeType.ALL)
	private UserProfile userProfile;

	public User(String fullName, String userName, String email, String gender, OffsetDateTime dateOfBirth,
			String cellPhone, String status) throws ParseException {
		UserProfile entity = new UserProfile();
		String[] name = fullName.split("\\s");
		entity.setFirstName(name[0]);
		entity.setLastName(name[1]);
		entity.setEmail(email);
		entity.setGender(gender);
		entity.setDateOfBirth(dateOfBirth);
		entity.setCellPhone(cellPhone);
		userProfile = entity;
		this.userName = userName;
		this.status = status;
	}

}