package com.base.api.request.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class AddressDTO implements Serializable {

	
	private static final long serialVersionUID = -8591433393025601184L;

	private String addressType;

	private String addressLineOne;

	private String addressLineTwo;

	private String city;

	private String province;

	private String postalCode;
}
