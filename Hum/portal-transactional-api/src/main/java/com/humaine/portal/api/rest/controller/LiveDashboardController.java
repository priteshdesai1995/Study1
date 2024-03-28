package com.humaine.portal.api.rest.controller;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.humaine.portal.api.enums.GraphDuration;
import com.humaine.portal.api.es.repository.impl.ESUserEventRepository;
import com.humaine.portal.api.exception.APIException;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.LivePageRefreshHistory;
import com.humaine.portal.api.response.dto.ActiveDeviceUsedResponse;
import com.humaine.portal.api.response.dto.ChartDataWithRange;
import com.humaine.portal.api.response.dto.ConversionChartResultResponse;
import com.humaine.portal.api.response.dto.LiveDashboardChartData;
import com.humaine.portal.api.response.dto.LiveDashboardPageResponse;
import com.humaine.portal.api.response.dto.LivePageChartData;
import com.humaine.portal.api.rest.repository.LivePageRepository;
import com.humaine.portal.api.rest.service.AuthService;
import com.humaine.portal.api.security.config.AWSConfig;
import com.humaine.portal.api.util.DateUtils;
import com.humaine.portal.api.util.ErrorMessageUtils;
import com.humaine.portal.api.util.ResponseBuilder;
import com.humaine.portal.api.util.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;

/**
 * This controller have the APIs that is used in live page of the portal 
 *
 */
@RestController
@RequestMapping("/live/dashboard")
@Api(tags = "Live Dashboard", description = "Live Dashboard", authorizations = {})
@Slf4j
public class LiveDashboardController {

	@Autowired
	private ESUserEventRepository esUserEventRepository;
	
	@Autowired
	private LivePageRepository livePageRepository;
	
	@Autowired
	private AuthService authService;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@GetMapping(value = "active/countries", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Top Countries Active Data", notes = "Get Top Countries Active Data", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public LiveDashboardPageResponse getDashboardData(HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		List<String> indexs = new ArrayList<>();
		OffsetDateTime current = DateUtils.getCurrentTimestemp();
//		OffsetDateTime hoursDiff = DateUtils.getCurrentTimestemp().minusHours(23);
		indexs.add(esUserEventRepository.getIndexName(current));

//		if (DateUtils.getFromatedDate(current) != DateUtils.getFromatedDate(hoursDiff)) {
//			indexs.add(esUserEventRepository.getIndexName(hoursDiff));
//		}
		LiveDashboardPageResponse result = esUserEventRepository.getStateWiseProductSale(indexs, account.getId(), 5);
		return result;
	}

	@GetMapping(value = "users/hourly", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Dashboard Data", notes = "Get Dashboard Data", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ChartDataWithRange<Long> getLastTwentyFourHoursUsers(HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		List<String> indexs = new ArrayList<>();
		OffsetDateTime current = DateUtils.getCurrentTimestemp();
		OffsetDateTime hoursDiff = DateUtils.getCurrentTimestemp().minusHours(23);
		indexs.add(esUserEventRepository.getIndexName(current));

		if (DateUtils.getFromatedDate(current) != DateUtils.getFromatedDate(hoursDiff)) {
			indexs.add(esUserEventRepository.getIndexName(hoursDiff));
		}
		return esUserEventRepository.getUserInLastTwentyFourHours(indexs, account.getId());
	}

	@GetMapping(value = "active/device", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Active Devices Used Data", notes = "Get Active Devices Used Data", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public List<ActiveDeviceUsedResponse> getActiveDevicedUsed(HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		return esUserEventRepository.getActiveDevicesUsedData(account.getId());
	}

	@GetMapping(value = "statastics", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Active Devices Used Data", notes = "Get Active Devices Used Data", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public LiveDashboardChartData getChartData(HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		List<String> indexs = new ArrayList<>();
		OffsetDateTime current = DateUtils.getCurrentTimestemp();
		OffsetDateTime hoursDiff = DateUtils.getCurrentTimestemp().minusHours(23);
		indexs.add(esUserEventRepository.getIndexName(current));

		if (DateUtils.getFromatedDate(current) != DateUtils.getFromatedDate(hoursDiff)) {
			indexs.add(esUserEventRepository.getIndexName(hoursDiff));
		}
		LivePageChartData sessionData = esUserEventRepository.getActiveUsersChartData(indexs, account.getId());
		ConversionChartResultResponse<Long> overallRate = esUserEventRepository.getOverallConversionRate(indexs,
				account.getId());
		ConversionChartResultResponse<Double> totalRate = esUserEventRepository.getTotalConversions(indexs,
				account.getId());
		ConversionChartResultResponse<Double> activeUsers = esUserEventRepository.getActiveUsers(indexs,
				account.getId());
		return new LiveDashboardChartData(sessionData, overallRate, totalRate, activeUsers);
	}

	@GetMapping(value = "session/duration", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Session Duration Data", notes = "Get Session Duration Data", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public HashMap<String, Double> getApdexScore(@RequestParam(required = true) GraphDuration duration, HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		return esUserEventRepository.getSessionDurationData(account.getId(), duration);
	}

	@GetMapping(value = "apdex-score", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Apdex Data", notes = "Get Apdex Data", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ChartDataWithRange<Double> getSessionDurationData(@RequestParam(required = true) GraphDuration duration, HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		if (GraphDuration.ONE_WEEK.equals(duration)) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.duration.invalid",
					new Object[] { GraphDuration.ONE_WEEK.value() }, "api.error.duration.invalid.code"));
		}
		return esUserEventRepository.getApdexScore(account.getId(), duration);
	}

	@GetMapping(value = "page-loadtime", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Apdex Data", notes = "Get Apdex Data", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ChartDataWithRange<Double> getPageLoadTimeData(@RequestParam(required = true) GraphDuration duration, HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		if (GraphDuration.ONE_WEEK.equals(duration)) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.duration.invalid",
					new Object[] { GraphDuration.ONE_WEEK.value() }, "api.error.duration.invalid.code"));
		}
		return esUserEventRepository.getPageLoadTime(account.getId(), duration);
	}

	@GetMapping(value = "bounce-rate", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Bounce Rate Data", notes = "Get Bounce Rate Data", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ConversionChartResultResponse<Double> getBounceRate(HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		List<String> indexs = new ArrayList<>();
		indexs.add(esUserEventRepository.getIndexName(DateUtils.getCurrentTimestemp()));
		if (!indexs.contains(esUserEventRepository.getIndexName(DateUtils.getCurrentTimestemp().minusHours(23)))) {
			indexs.add(esUserEventRepository.getIndexName(DateUtils.getCurrentTimestemp().minusHours(23)));
		}
		return esUserEventRepository.getBounceRate(indexs, account.getId());
	}

	
	/**
	 * This method will post entries in data table live_page_refresh_history when
	 * page is refreshed
	 */
	@PostMapping(value = "page-refresh", headers = { "version=v1" })
	@ApiOperation(value = "post last refreshed date and time in table live_page_refresh_history", notes = "post last refreshed date and time in table live_page_refresh_history", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> postEntryOfLastRefresh(HttpServletRequest httpRequest) throws SQLException {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		try {
			livePageRepository.updateRefreshDateAndTimeForAccountId(account.getId(),
					DateUtils.getCurrentTimestemp());
			
		}catch(Exception e) {
			log.error("Something going wrong with sql query");
		}
		log.info("New entry logged in database");

		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.last.refreshed.time.logged", null, "api.last.refreshed.time.logged.code"));
	}

	/**
	 * This API returns last date and time of refresh
	 */
	@GetMapping(value = "last-refresh-time", headers = { "version=v1" })
	@ApiOperation(value = "get the last refreshed time ", notes = "get the last refreshed time", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getLastRefreshedTime(HttpServletRequest httpRequest){
		
		Account account = authService.getLoginUserAccount(true, httpRequest);
		OffsetDateTime lastRefresh = DateUtils.getCurrentTimestemp();
		Timestamp refreshTimeStamp = livePageRepository.getLastRefreshedDateAndTime(account.getId());
		if (refreshTimeStamp != null) {
			lastRefresh = DateUtils.toUTCOffsetDateTime(refreshTimeStamp);
		}
		return ResponseBuilder.buildResponse(lastRefresh, HttpStatus.OK);
	}
	
}
