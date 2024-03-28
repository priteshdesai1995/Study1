package com.humaine.portal.api.rest.controller;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humaine.portal.api.enums.UserEvents;
import com.humaine.portal.api.exception.APIException;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.JourneySuccessSessionCount;
import com.humaine.portal.api.projection.model.MonthlyProductData;
import com.humaine.portal.api.projection.model.Product;
import com.humaine.portal.api.projection.model.StateWiseSoldData;
import com.humaine.portal.api.request.dto.DashboardJourneySuccessCount;
import com.humaine.portal.api.response.dto.DashboardData;
import com.humaine.portal.api.response.dto.DashboardJourneySuccessResponse;
import com.humaine.portal.api.response.dto.StateInsightResponse;
import com.humaine.portal.api.rest.repository.SaleRepository;
import com.humaine.portal.api.rest.repository.UserEventRepository;
import com.humaine.portal.api.rest.service.AuthService;
import com.humaine.portal.api.security.config.AWSConfig;
import com.humaine.portal.api.util.DateUtils;
import com.humaine.portal.api.util.ErrorMessageUtils;
import com.humaine.portal.api.util.ResponseBuilder;
import com.humaine.portal.api.util.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/dashboard")
@Api(tags = "Dashboard", description = "Dashboard", authorizations = {})
public class DashboardController {

	@Autowired
	private SaleRepository saleRepository;
	
	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Autowired
	private AuthService authService;

	@Autowired
	private UserEventRepository userEventRepository;

	private static final Logger log = LogManager.getLogger(DashboardController.class);

	@GetMapping(value = "", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Dashboard Data", notes = "Get Dashboard Data", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getDemographicAttributes(HttpServletRequest httpRequest) {
		log.info("Get Dashboard Data: start");
		Account account = authService.getLoginUserAccount(true, httpRequest);
		
		List<Product> popularProducts = saleRepository.getPopularProductsList(account.getId(), 3,
				DateUtils.getFirstDayOfCurrentMonth(), DateUtils.getLastDayOfCurrentMonth());
		
		List<Product> leastPopularProducts = saleRepository.getLeastPopularProductsList(account.getId(), 3,
				DateUtils.getFirstDayOfCurrentMonth(), DateUtils.getLastDayOfCurrentMonth());
		
		List<StateWiseSoldData> stateSoldData = saleRepository.getStateWiseSoldAmount(account.getId(), 5, 
				DateUtils.getFirstDayOfCurrentMonth(), DateUtils.getLastDayOfCurrentMonth());
		
		List<StateWiseSoldData> leastStateSoldData = saleRepository.getLeasePerformingStateWiseSoldAmount(account.getId(), 5,
				DateUtils.getFirstDayOfCurrentMonth(), DateUtils.getLastDayOfCurrentMonth());
		
		MonthlyProductData monthlyProductData = saleRepository.getMonthlyProductData(account.getId(),
				DateUtils.getFirstDayOfCurrentMonth(), DateUtils.getLastDayOfCurrentMonth());
		
		DashboardData response = new DashboardData(popularProducts, leastPopularProducts, stateSoldData, leastStateSoldData, monthlyProductData);
		
		log.info("Get Dashboard Data: end");
		return ResponseBuilder.buildResponse(response);
	}

	@PostMapping(value = "journey", headers = {
			"version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Journy Success Statistics", notes = "Journy Success Statistics", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getJourneySuccessData(
			@Valid @RequestBody DashboardJourneySuccessCount request, HttpServletRequest httpRequest) {
		log.info("get Journy Success Statistics: start");
		
		Account account = authService.getLoginUserAccount(true, httpRequest);
		
		JourneySuccessSessionCount successCountForToday = userEventRepository.getSessionCountForToday(account.getId(),
				DateUtils.getCurrentTimestemp());
		
		Float todayPercentage = calculatePercentage(successCountForToday.getBuyCount(),
				successCountForToday.getTotalSessionCount());
		
		JourneySuccessSessionCount monthlySuccessCount = userEventRepository.getMontlySessionCount(account.getId(),
				UserEvents.BUY.value(), DateUtils.getFirstDayOfCurrentMonth(), DateUtils.getLastDayOfCurrentMonth());
		
		Float monthlyPercentage = calculatePercentage(monthlySuccessCount.getBuyCount(),
				monthlySuccessCount.getTotalSessionCount());
		
		DashboardJourneySuccessResponse response = new DashboardJourneySuccessResponse(monthlyPercentage,
				todayPercentage);
		
		log.info("get Journy Success Statistics: end");
		return ResponseBuilder.buildResponse(response);
	}

	/**
	 * @param stateName
	 * @return
	 * 
	 * This is a API method that will give the insights of the state.
	 *         HMNAI-322
	 */
	@GetMapping(value = "/state/{stateName}", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get insigths of the state of USA", notes = "Get insigths of the state of USA", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getStateInsights(@PathVariable(name = "stateName") String stateName, HttpServletRequest httpRequest) {

		if (stateName.isEmpty() || stateName == null) {
			log.error("State Name is null, Pass state name");
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.state.name.can.not.be.null",
					null, "api.error.state.name.can.not.be.null.code"));
		}

		Account account = authService.getLoginUserAccount(true, httpRequest);

		// code to get most popular and least popular product of that state 
		List<Product> popularProductsStateWise = saleRepository.getPopularProductsWithStateFilter(account.getId(), 3,
				DateUtils.getFirstDayOfCurrentMonth(), DateUtils.getLastDayOfCurrentMonth(), stateName);

		
		List<Product> leatsPopularProductsStateWise = saleRepository.getLeastPopularProductsWithStateFilter(
				account.getId(), 3, DateUtils.getFirstDayOfCurrentMonth(), DateUtils.getLastDayOfCurrentMonth(),
				stateName);
		
		// code to get product data 
		MonthlyProductData monthlyDataByState = saleRepository.getMonthlyProductDataWithStateFilter(account.getId(),
				DateUtils.getFirstDayOfCurrentMonth(), DateUtils.getLastDayOfCurrentMonth(), stateName);
		
		// Code to get the today and monthly sales persentage  
		JourneySuccessSessionCount successCountForTodayWithStateName = userEventRepository.getSessionCountForTodayWithStateFilter(account.getId(),
				DateUtils.getCurrentTimestemp(), stateName);

		Float todayPercentage = calculatePercentage(successCountForTodayWithStateName.getBuyCount(),
				successCountForTodayWithStateName.getTotalSessionCount());
		
		JourneySuccessSessionCount monthlySuccessCountWithStateName = userEventRepository
				.getMonthlySessionCountForCurrentMonthWithStateFilter(account.getId(),
						DateUtils.getFirstDayOfCurrentMonth(), DateUtils.getLastDayOfCurrentMonth(), stateName);

		Float monthlyPercentage = calculatePercentage(monthlySuccessCountWithStateName.getBuyCount(),
				monthlySuccessCountWithStateName.getTotalSessionCount());
		
		if(popularProductsStateWise.size() == 0 && leatsPopularProductsStateWise.size() == 0 &&
				monthlyDataByState == null && todayPercentage.isNaN() && monthlyPercentage.isNaN()) {
			log.error("Few of state insight data is not available");
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.state.insights.fields.not.available",
					null, "api.error.state.insights.fields.not.available.code"));
		}
				
		StateInsightResponse stateInsightResponse = new StateInsightResponse(popularProductsStateWise,
				leatsPopularProductsStateWise, monthlyPercentage, todayPercentage, monthlyDataByState);

		return ResponseBuilder.buildResponse(stateInsightResponse);
	}
	
	private static Float generateRandomNumber(Float min, Float max) {
		Random r = new Random();
		float random = min + r.nextFloat() * (max - min);
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		return Float.valueOf(df.format(random));
	}

	private static Float calculatePercentage(Float obtained, Float total) {
		return obtained * 100 / total;
	}

}
