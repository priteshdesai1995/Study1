package com.base.api.request.dto;

import java.util.List;

import com.base.api.entities.Offer;
import com.base.api.entities.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferDTO extends FiltertDTO{
	
	private Offer offer;
	private List<User> users;
	
//	private String offerName;
//	private String offerCode;
//	private OfferType type;
//	private UserStatus status;
//	private String totalUsage;
//	private OfferUsage usage;
}
