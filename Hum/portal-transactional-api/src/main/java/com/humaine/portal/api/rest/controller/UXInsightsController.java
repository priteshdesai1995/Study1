package com.humaine.portal.api.rest.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humaine.portal.api.enums.UserEvents;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.projection.model.DailyEventDistributionCount;
import com.humaine.portal.api.projection.model.DailyMostPurchasedProductCount;
import com.humaine.portal.api.projection.model.DailyMostViewedCategoryCount;
import com.humaine.portal.api.projection.model.DailyMostViewedProductCount;
import com.humaine.portal.api.projection.model.DailyProductAvg;
import com.humaine.portal.api.response.dto.DailyProductsDataResponse;
import com.humaine.portal.api.response.dto.UXInsightsStatasticsCountResponse;
import com.humaine.portal.api.rest.repository.InventoryRepository;
import com.humaine.portal.api.rest.repository.SaleRepository;
import com.humaine.portal.api.rest.repository.UserEventRepository;
import com.humaine.portal.api.rest.repository.UserRepository;
import com.humaine.portal.api.rest.repository.UserSessionRepository;
import com.humaine.portal.api.rest.service.AuthService;
import com.humaine.portal.api.security.config.AWSConfig;
import com.humaine.portal.api.util.ResponseBuilder;
import com.humaine.portal.api.util.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ux-insights")
@Api(tags = "UX Insights", description = "UX Insights", authorizations = {})
public class UXInsightsController {

	private static final Logger log = LogManager.getLogger(UXInsightsController.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserSessionRepository userSessionRepository;

	@Autowired
	private SaleRepository saleRepository;

	@Autowired
	private AuthService authService;

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private UserEventRepository userEventRepository;

	@GetMapping(value = "", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Statastics Data", notes = "Get Statastics Data", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getStatasticsData(HttpServletRequest httpRequest) {
		log.info("Get Statastics Data: start");
		Account account = authService.getLoginUserAccount(true, httpRequest);
		Long totalUsers = userRepository.getTotalUsersByAccount(account.getId());
		Long dailyAvgSessions = userSessionRepository.getAvgUserSessionsPerDay(account.getId());
		DailyProductAvg productAvg = saleRepository.getDailyPrductAvg(account.getId());
		log.info("Get Statastics Data: end");
		return ResponseBuilder
				.buildResponse(new UXInsightsStatasticsCountResponse(totalUsers, dailyAvgSessions, productAvg));
	}

	@GetMapping(value = "products/data", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Products Daily Statastics Data", notes = "Get Products Daily Statastics Data", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getProductsData(HttpServletRequest httpRequest) {
		log.info("Get Products Daily Statastics Data: start");
		Account account = authService.getLoginUserAccount(true, httpRequest);
		List<DailyMostViewedProductCount> prodData = inventoryRepository
				.getDailyMostViewedProductCount(UserEvents.PRODVIEW.value(), account.getId(), 5);

		List<DailyMostPurchasedProductCount> mostPurchased = inventoryRepository
				.getDailyMostPurchasedProductCount(UserEvents.PRODVIEW.value(), account.getId(), 5);
		List<DailyMostViewedCategoryCount> mostVisistedCat = inventoryRepository
				.getDailyMostViewedCategoryCount(UserEvents.PRODVIEW.value(), account.getId(), 5);
		List<DailyEventDistributionCount> eventDistribution = userEventRepository
				.getDailyEventDistributionData(account.getId());
		log.info("Get Products Daily Statastics Data: end");
		return ResponseBuilder.buildResponse(
				new DailyProductsDataResponse(prodData, mostPurchased, mostVisistedCat, eventDistribution));
	}
}
