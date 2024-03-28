package com.base.api.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.base.api.dto.filter.OfferFilter;
import com.base.api.entities.Offer;
import com.base.api.request.dto.OfferDTO;


/**
 * @author kavitha_deshagani
 *
 */
public interface OfferService {
	/*
	 * This services are used to offer module.
	 * 
	 */
	public String createOffer(OfferFilter offerFilter);

	public List<Offer> getAllOffers(OfferFilter offerFilter) throws ParseException;

	public OfferDTO getOffer(UUID offerId);

	public String updateOffer(UUID offerId, OfferFilter offerFilter);

	public String deleteOffer(UUID offerId);

	public void changeStatus(Map<String, String> statusReq);
	
	ByteArrayInputStream writeExcel(List<Offer> offers, String filename);

	InputStream load(List<Offer> offerEntities);

	 
}
