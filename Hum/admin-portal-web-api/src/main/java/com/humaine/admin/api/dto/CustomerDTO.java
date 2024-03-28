package com.humaine.admin.api.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class CustomerDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3942948714242066069L;

	private String fullName;

	private String email;

	private String url;

	private String address;

	private String city;

	private String state;

	private List<String> industry;

	private Date registeredOn;

	private Long accountId;

	private String status;
	
	public CustomerDTO(CustomerListing customer) {
		super();
		this.fullName = customer.getFullName();
		this.email = customer.getEmail();
		this.url = customer.getUrl();
		this.address = customer.getAddress();
		this.city = customer.getCity();
		this.state = customer.getState();
		this.registeredOn = customer.getRegisteredOn();
		this.accountId = customer.getAccountId();
		this.status = customer.getStatus();
		String industries = customer.getIndustry();
		if (industries == null) {
			industries = "";
		}
		this.industry = Arrays.asList(industries.split(",")).stream().map(e -> {
			if (e == null)
				return "";
			return e.trim();
		}).filter(e -> {
			return e.length() > 0;
		}).collect(Collectors.toList());
	}

}
