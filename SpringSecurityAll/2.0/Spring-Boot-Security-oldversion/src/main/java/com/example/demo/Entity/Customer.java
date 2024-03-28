package com.example.demo.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "customer_table")
//@SequenceGenerator(name = "Generator1", sequenceName = "Gen1", initialValue = 1000)

public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long customerId;

	@NotEmpty
	@Size(min = 3, message = "Customer First Name Should Not be Empty")
	private String customerFirstName;

	@NotEmpty
	@Size(min = 3, message = "Customer Last Name Should Not be Empty")
	private String customerLastName;

	@NotEmpty
	@Size(min = 10, max = 10, message = "Enter Correct PhoneNumber (must be 10 digits)")
	private String phoneNumber;

	@NotEmpty
	@Size(min = 3, message = "Customer Address Should Not be Empty")
	private String address;

	@NotEmpty
	private String role;

	@NotEmpty
	@Column(unique = true, length = 30)
	@Email(message = "Customer Email is not valid")
	private String customerEmail;

//	@NotEmpty
//	@Column(length = 20)
//	@Size(min = 8, message = "Password Must be 8 characters long")
//	@Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}")
	private String password;

	public Customer(long customerId, String customerFirstName, String customerLastName, String phoneNumber,
			String address, String customerEmail, String password, String role) {

		super();
		this.customerId = customerId;
		this.customerFirstName = customerFirstName;
		this.customerLastName = customerLastName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.customerEmail = customerEmail;
		this.password = password;
	}

	public Customer() {

	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", customerFirstName=" + customerFirstName + ", customerLastName="
				+ customerLastName + ", phoneNumber=" + phoneNumber + ", address=" + address + ", role=" + role
				+ ", customerEmail=" + customerEmail + ", password=" + password + "]";
	}

}
