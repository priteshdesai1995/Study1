package com.base.api.entities;

import java.text.ParseException;
import java.time.LocalDate;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.base.api.enums.UserStatus;
import com.base.api.request.dto.UserSignupDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
@NamedQueries({ @NamedQuery(name = "user.select.by.id", query = "select u from User u where u.id = :id"),
		@NamedQuery(name = "user.select.by.role", query = "select u from User u inner join UserRole r on r.id = u.userRole.id where r.roleName = :roleName") })
public class User extends BaseEntity {

	private static final long serialVersionUID = -1205140138381353647L;

	@Column(name = "user_name", unique = true, nullable = false)
	private String userName;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@Column(name = "password", nullable = false, length = 1000)
	private String password;

	@Column(name = "isAccountNonLocked", nullable = false, columnDefinition = "boolean default true")
	private Boolean accountNonLocked = true;

	@Column(name = "isAccountNonExpired", nullable = false, columnDefinition = "boolean default true")
	private Boolean accountNonExpired = true;

	@Column(name = "isCredentialsNonExpired", nullable = false, columnDefinition = "boolean default true")
	private Boolean credentialsNonExpired = true;

	@Column(name = "isEnabled", columnDefinition = "boolean default true")
	private Boolean enabled = true;

	@OneToOne()
	@JoinColumn(name = "user_role_id")
	private UserRole userRole;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_profile_id")
	private UserProfile userProfile;

	public User(String fullName, String userName, String email, String gender, LocalDate dateOfBirth, String cellPhone,
			UserStatus status) throws ParseException {
		UserProfile userProfileEntity = new UserProfile();
		String[] name = fullName.split("\\s");
		userProfileEntity.setFirstName(name[0]);
		userProfileEntity.setLastName(name[1]);
		userProfileEntity.setEmail(email);
		userProfileEntity.setGender(gender);
		userProfileEntity.setDateOfBirth(dateOfBirth);
		userProfileEntity.setCellPhone(cellPhone);
		userProfile = userProfileEntity;
		this.userName = userName;
		this.status = status;
	}

	/**
	 * @param userSignupDTO
	 * 
	 *                      This is the constructor that will create User from the
	 *                      userSignupDTO
	 */
	public User(UserSignupDTO userSignupDTO) {
		this.userName = userSignupDTO.getUserName();
		this.status = UserStatus.ACTIVE;
		this.accountNonExpired = true;
		this.accountNonLocked = true;
		this.enabled = true;
		this.credentialsNonExpired = true;
	}

	/**
	 * @param userName
	 * @param status
	 * @param accountNonLocked
	 * @param accountNonExpired
	 * @param credentialsNonExpired
	 * @param enabled
	 * 
	 *                              This constructor is for seeding purpose
	 */
	public User(String userName, UserStatus status, Boolean accountNonLocked, Boolean accountNonExpired,
			Boolean credentialsNonExpired, Boolean enabled, String pass) {
		super();
		this.userName = userName;
		this.status = status;
		this.accountNonLocked = accountNonLocked;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
		this.password = pass;
	}

	public User(String fullName, String userName, String email, String gender, LocalDate date, String cellPhone,
			String status) {
		UserProfile entity = new UserProfile();
		String[] name = fullName.split("\\s");
		entity.setFirstName(name[0]);
		entity.setLastName(name[1]);
		entity.setEmail(email);
		entity.setGender(gender);
		entity.setDateOfBirth(date);
		entity.setCellPhone(cellPhone);
		userProfile = entity;
		this.userName = userName;
		this.status = UserStatus.valueOf(status);
	}

}