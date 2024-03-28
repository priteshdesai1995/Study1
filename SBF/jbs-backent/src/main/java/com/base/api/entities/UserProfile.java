package com.base.api.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.base.api.request.dto.AddressDTO;
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
@Table(name = "userprofile", uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }) })
@AttributeOverride(name = "id", column = @Column(name = "user_profile_id"))
public class UserProfile extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 4435387511899406040L;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "date_of_birth", columnDefinition = "DATE")
	private LocalDate dateOfBirth;

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

	@Column(name = "occupation") 
	private String occupation;

	@Column(name = "employer") 
	private String employer;

	@Column(name = "profile_photo_link")
	private String profilePhotoLink;

	@Column(name = "profile_photo")
	private byte[] profilePhoto;

	@Column(name = "photoType")
	private String photoType;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<PersonAddress> userAddresses;
	
	public UserProfile(UserSignupDTO userSignupDTO) {
		this.firstName = userSignupDTO.getFirstName();
		this.middleName = userSignupDTO.getMiddleName();
		this.lastName = userSignupDTO.getLastName();
		this.dateOfBirth = userSignupDTO.getDateOfBirth();
		this.gender = userSignupDTO.getGender();
		this.email = userSignupDTO.getEmail();
		this.cellPhone = userSignupDTO.getCellPhone();
		this.homePhone = userSignupDTO.getHomePhone();
		this.workPhone = userSignupDTO.getWorkPhone();
		this.occupation = userSignupDTO.getOccupation();
		this.employer = userSignupDTO.getEmployer();

		List<AddressDTO> userAddressList = userSignupDTO.getAddress();
		
		if (userAddressList != null) {
			Set<PersonAddress> userAddressSet = new HashSet<PersonAddress>();
			userAddressList.forEach(userAddress -> {
				userAddressSet.add(new PersonAddress(userAddress));
			});
			this.userAddresses = userAddressSet;
		}
	}
}
