package com.base.api.request.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateUserDto implements Serializable {

	private static final long serialVersionUID = 914611473997112719L;

	@NotEmpty(message = "firstName should not be null.")
	private String firstName;
	
	@NotEmpty(message = "middleName should not be null.")
	private String middleName;
	
	@NotEmpty(message = "lastName should not be null.")
	private String lastName;
	
	@NotEmpty(message = "userName should not be null.")
	private String userName;
	
	@Past(message = "dateOfBirth should be past date.")
	private LocalDate dateOfBirth;
	
	@NotEmpty(message = "gender should not be null.")
	private String gender;
	
	@NotEmpty(message = "email should not be null.")
	private String email;
	
	@NotEmpty(message = "cellPhone should not be null.")
	private String cellPhone;
	
	@NotEmpty(message = "homePhone should not be null.")
	private String homePhone;
	
	@NotEmpty(message = "workPhone should not be null.")
	private String workPhone;
	
	@NotEmpty(message = "occupation should not be null.")
	private String occupation;
	
	@NotEmpty(message = "employer should not be null.")
	private String employer;
	
	@NotEmpty(message = "address should not be null.")
	private List<AddressDTO> address;
}
