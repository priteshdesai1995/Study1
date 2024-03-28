package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.base.api.request.dto.AddressDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "useraddress")
@AttributeOverride(name = "id", column = @Column(name = "person_address_id"))
public class PersonAddress extends BaseEntity {

	private static final long serialVersionUID = -2560361076012158870L;

	@ManyToOne
	@JoinColumn(name = "user_profile_id")
	private UserProfile userProfile;

	@Column(name = "address_type")
	private String addressType;

	@Column(name = "addressline_one", nullable = false)
	private String addressLineOne;

	@Column(name = "addressline_two")
	private String addressLineTwo;

	@Column(name = "city", nullable = false)
	private String city;

	@Column(name = "province", nullable = false)
	private String province;

	@Column(name = "postal_code", nullable = false)
	private String postalCode;

	public PersonAddress(AddressDTO addressDTO) {
		this.addressType = addressDTO.getAddressType();
		this.addressLineOne = addressDTO.getAddressLineOne();
		this.addressLineTwo = addressDTO.getAddressLineTwo();
		this.city = addressDTO.getCity();
		this.province = addressDTO.getProvince();
		this.postalCode = addressDTO.getPostalCode();
	}
	
}
