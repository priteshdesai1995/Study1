package com.humaine.portal.api.rest.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.humaine.portal.api.model.BigFive;
import com.humaine.portal.api.model.Buying;
import com.humaine.portal.api.model.PersonaDetailsMaster;
import com.humaine.portal.api.model.Persuasive;
import com.humaine.portal.api.model.Values;
import com.humaine.portal.api.request.dto.BigFiveEntryRequest;
import com.humaine.portal.api.rest.repository.BigFiveRepository;
import com.humaine.portal.api.rest.repository.BuyingRepository;
import com.humaine.portal.api.rest.repository.PersonaDetailsMasterRepository;
import com.humaine.portal.api.rest.repository.PersuasiveRepository;
import com.humaine.portal.api.rest.repository.ValuesRepository;
import com.humaine.portal.api.util.CommonUtils;
import com.humaine.portal.api.util.TransactionInfo;

@RestController
@RequestMapping("test")
public class TestController {

	@Autowired
	BigFiveRepository bigFiveRepository;

	@Autowired
	BuyingRepository buyingRepository;

	@Autowired
	ValuesRepository valuesRepository;

	@Autowired
	PersuasiveRepository persuasiveRepository;

	@Autowired
	PersonaDetailsMasterRepository personaDetailsMasterRepository;

	@PostMapping(value = "", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TransactionInfo> entry(@RequestBody List<BigFiveEntryRequest> request) {
		List<BigFive> bigFiveList = Lists.newArrayList(bigFiveRepository.findAll());
		List<Buying> buyingList = Lists.newArrayList(buyingRepository.findAll());
		List<Values> valuesList = Lists.newArrayList(valuesRepository.findAll());
		List<Persuasive> strategyList = Lists.newArrayList(persuasiveRepository.findAll());

		HashMap<String, BigFive> bigFiveMap = new HashMap<>();
		HashMap<String, Buying> buyingMap = new HashMap<>();
		HashMap<String, Values> valuesMap = new HashMap<>();
		HashMap<String, Persuasive> strategyMap = new HashMap<>();

		bigFiveList.forEach(e -> {
			bigFiveMap.put(CommonUtils.generateUnique(e.getValue()), e);
		});
		buyingList.forEach(e -> {
			buyingMap.put(CommonUtils.generateUnique(e.getValue()), e);
		});
		valuesList.forEach(e -> {
			valuesMap.put(CommonUtils.generateUnique(e.getValue()), e);
		});
		strategyList.forEach(e -> {
			strategyMap.put(CommonUtils.generateUnique(e.getValue()), e);
		});
		request.forEach(e -> {
			PersonaDetailsMaster pd = new PersonaDetailsMaster();
			pd.setBigFive(bigFiveMap.get(CommonUtils.generateUnique(e.getBigFive())));
			pd.setBuy(buyingMap.get(CommonUtils.generateUnique(e.getMotivation())));
			pd.setValues(valuesMap.get(CommonUtils.generateUnique(e.getValues())));
			pd.setStrategies(strategyMap.get(CommonUtils.generateUnique(e.getStrategy())));
			pd.setFrustrations(e.getFrustrations());
			pd.setGoals(e.getGoals());
			pd.setPersonalities(e.getPersonality());
			personaDetailsMasterRepository.save(pd);
		});
		return null;
	}
}
