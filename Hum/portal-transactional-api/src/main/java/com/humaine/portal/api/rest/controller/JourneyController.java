package com.humaine.portal.api.rest.controller;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.humaine.portal.api.enums.JourneyElementMasterIds;
import com.humaine.portal.api.exception.APIException;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.JourneyElementMaster;
import com.humaine.portal.api.model.JourneyElementValue;
import com.humaine.portal.api.model.TestJourney;
import com.humaine.portal.api.model.TestJourneyDeleted;
import com.humaine.portal.api.model.UserGroup;
import com.humaine.portal.api.request.dto.CreateTestJourneyRequest;
import com.humaine.portal.api.request.dto.MultipleDeleteRequests;
import com.humaine.portal.api.response.dto.JournyResponse;
import com.humaine.portal.api.response.dto.ShortGroupDetails;
import com.humaine.portal.api.response.dto.TestJourneyResponse;
import com.humaine.portal.api.rest.olap.service.impl.OLAPManualJourneyServiceImpl;
import com.humaine.portal.api.rest.repository.JourneyElementMasterRepository;
import com.humaine.portal.api.rest.repository.JourneyElementValueRepository;
import com.humaine.portal.api.rest.repository.TestJourneyDeletedRepository;
import com.humaine.portal.api.rest.repository.TestJourneyRepository;
import com.humaine.portal.api.rest.repository.UserEventRepository;
import com.humaine.portal.api.rest.repository.UserGroupRepository;
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
@Api(tags = "Test Journey", description = "Test Journey")
@RequestMapping("/test-journey")
public class JourneyController {

	private static final Logger log = LogManager.getLogger(JourneyController.class);

	@Autowired
	private JourneyElementMasterRepository journeyElementMasterRepository;

	@Autowired
	private JourneyElementValueRepository journeyElementValueRepository;

	@Autowired
	private UserGroupRepository userGroupRepository;

	@Autowired
	private AuthService authService;

	@Autowired
	private TestJourneyRepository testJourneyRepository;

	@Autowired
	private TestJourneyDeletedRepository testJourneyDeletedRepository;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Autowired
	UserEventRepository userEventRepository;

	@Autowired
	OLAPManualJourneyServiceImpl olapManualJourneyService;

	@GetMapping(value = "elements", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Journey Elements", notes = "Get Journey Elements List", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getElementsList(HttpServletRequest httpRequest) {
		log.info("Get Journey Elements: start");
		Iterable<JourneyElementMaster> elements = journeyElementMasterRepository.findAll();
		List<JourneyElementMaster> elementList = Lists.newLinkedList(elements);
		Account account = authService.getLoginUserAccount(true, httpRequest);
		List<String> menuItems = userEventRepository.findMenuNames(account.getId());
		mapMenuItems(elementList, menuItems);
		log.info("Get Journey Elements: end");
		return ResponseBuilder.buildResponse(elementList);
	}

	private void mapMenuItems(List<JourneyElementMaster> elementList, List<String> menuItems) {
		Long decisionId = JourneyElementMasterIds.DECISION.value();
		Long maxId = journeyElementValueRepository.findJourneyElementMaxId();

		int index = elementList.stream().map(e -> e.getId()).collect(Collectors.toList()).indexOf(decisionId);

		if (index == -1)
			return;

		elementList.get(index).getValues().removeIf(item -> "Menu Item".equals(item.getValue()));

		for (String item : menuItems) {
			maxId++;
			Long id = maxId;
			JourneyElementValue value = new JourneyElementValue(id, item);
			elementList.get(index).getValues().add(value);
		}
	}

	@GetMapping(value = "groups", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get User Group List", notes = "Get User Group List", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getGroups(HttpServletRequest httpRequest) {
		log.info("Get Groups List: start");
		Account account = authService.getLoginUserAccount(true, httpRequest);
		List<ShortGroupDetails> groupList = userGroupRepository.groupShortDetailsListByAccount(account.getId());
		log.info("Get Groups List: end");
		return ResponseBuilder.buildResponse(groupList);
	}

	@GetMapping(value = "", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Test Journey List", notes = "Get Test Journey List", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getJourneyList(HttpServletRequest httpRequest) {
		log.info("Get Test Journey List: start");
		Account account = authService.getLoginUserAccount(true, httpRequest);
		Long totalSteps = journeyElementMasterRepository.count();
		List<JournyResponse> journeyList = testJourneyRepository.getJourneyList(account.getId());
		olapManualJourneyService.fillSuccessMatchForList(journeyList, httpRequest);
		log.info("Get Test Journey List: end");
		return ResponseBuilder.buildResponse(
				journeyList.stream().map(e -> new TestJourneyResponse(e, totalSteps)).collect(Collectors.toList()));
	}

	@GetMapping(value = "{journeyId}", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Journey Details By Id", notes = "Get Journey Details By Id", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getJourneyDetails(@PathVariable(name = "journeyId") Long journeyId, HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("Get Test Journey Details By Id: Journy Id({}) : AccountId: ({}): start", journeyId, account.getId());
		Long totalSteps = journeyElementMasterRepository.count();
		JournyResponse journey = testJourneyRepository.getJourneyFullDetailsById(account.getId(), journeyId);
		if (journey == null) {
			log.error("No Journey Found With Id: {}", journeyId);
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.journey.not.found",
					new Object[] { journeyId }, "api.error.journey.not.found.code"));
		}
		olapManualJourneyService.fillSuccessMatch(journey, httpRequest);
		log.info("Get Test Journey Details By Id: Journy Id({}) : AccountId: ({}): end", journeyId, account.getId());
		return ResponseBuilder.buildResponse(new TestJourneyResponse(journey, totalSteps));
	}

	@PostMapping(value = "", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Test New Journey", notes = "Test New Journey", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> createNewTestJourney(@Valid @RequestBody CreateTestJourneyRequest request, HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("Test New Journey: start => Account Id ({}), Request Body => {}", account.getId(), request);
		UserGroup group = userGroupRepository.findGroupByAccountIdAndGroupId(account.getId(), request.getGroupId());
		if (group == null) {
			log.error("No Grop Found With Id: {}", request.getGroupId());
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user-group.not.found",
					new Object[] { request.getGroupId() }, "api.error.user-group.not.found.code"));
		}
		Iterable<JourneyElementMaster> elements = journeyElementMasterRepository.findAll();
		List<JourneyElementMaster> elementList = Lists.newArrayList(elements);
		Long filledSteps = validateRequest(request, elementList);
		TestJourney journey = new TestJourney(request, filledSteps, account, DateUtils.getCurrentTimestemp());
		testJourneyRepository.save(journey);
		log.info("Test New Journey: end => Account Id ({}), Request Body => {}", account.getId(), request);
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.success.ai.journey.saved", null, "api.success.ai.journey.saved.code"));
	}

	@PutMapping(value = "{journeyId}", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update Test New Journey", notes = "Update Test New Journey", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> updateTestJourney(@Valid @RequestBody CreateTestJourneyRequest request,
			@PathVariable(name = "journeyId") Long journeyId, HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("Update Test New Journey: start => Account Id ({}), Request Body => {}", account.getId(), request);

		TestJourney journey = testJourneyRepository.getJourneyById(account.getId(), journeyId);
		if (journey == null) {
			log.error("No Journey Found With Id: {}", journeyId);
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.journey.not.found",
					new Object[] { journeyId }, "api.error.journey.not.found.code"));
		}
		UserGroup group = userGroupRepository.findGroupByAccountIdAndGroupId(account.getId(), request.getGroupId());
		if (group == null) {
			log.error("No Grop Found With Id: {}", request.getGroupId());
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user-group.not.found",
					new Object[] { request.getGroupId() }, "api.error.user-group.not.found.code"));
		}
		Iterable<JourneyElementMaster> elements = journeyElementMasterRepository.findAll();
		List<JourneyElementMaster> elementList = Lists.newArrayList(elements);
		Long filledSteps = validateRequest(request, elementList);
		OffsetDateTime timestemp = DateUtils.getCurrentTimestemp();
		journey.updateFileds(request, filledSteps, account, timestemp);
		testJourneyRepository.save(journey);
		log.info("Update Test New Journey: end => Account Id ({}), Request Body => {}", account.getId(), request);
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.success.ai.journey.updated", null, "api.success.ai.journey.updated.code"));
	}

	@DeleteMapping(value = "{journeyId}", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Delete Test New Journey", notes = "Delete Test New Journey", authorizations = {
			@Authorization(value = AWSConfig.header) })
	ResponseEntity<TransactionInfo> deleteTestJourney(@PathVariable(name = "journeyId") Long journeyId, HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("Delete Test Journey By Id: Journy Id({}) : AccountId: ({}): start", journeyId, account.getId());
		TestJourney journey = testJourneyRepository.getJourneyById(account.getId(), journeyId);
		if (journey == null) {
			log.error("No Journey Found With Id: {}", journeyId);
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.journey.not.found",
					new Object[] { journeyId }, "api.error.journey.not.found.code"));
		}

		TestJourneyDeleted deleted = new TestJourneyDeleted(journey, DateUtils.getCurrentTimestemp());
		testJourneyDeletedRepository.save(deleted);
		testJourneyRepository.delete(journey);
		log.info("Delete Test Journey By Id: Journy Id({}) : AccountId: ({}): end", journeyId, account.getId());
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.success.ai.journey.deleted", null, "api.success.ai.journey.deleted.code"));
	}

	@DeleteMapping(headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Delete Multiple Test New Journeys By Ids", notes = "Delete Multiple Test New Journeys By Ids", authorizations = {
			@Authorization(value = AWSConfig.header) })
	ResponseEntity<TransactionInfo> deleteTestJourney(@RequestBody MultipleDeleteRequests<Long> request, HttpServletRequest httpRequest) {
		if (request == null)
			request = new MultipleDeleteRequests<>();
		if (request.getIds() == null)
			request.setIds(new ArrayList<Long>());
		if (request.getIds().isEmpty()) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.usergroup.delete.ids.empty",
					new Object[] {}, "api.error.usergroup.delete.ids.empty"));
		}
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("Delete Test Journey By Ids: Journy Ids({}) : AccountId: ({}): start", request.getIds(),
				account.getId());
		List<TestJourney> journey = testJourneyRepository.getJourneyByIds(account.getId(), request.getIds());
		journey.stream().forEach(e -> {
			TestJourneyDeleted deleted = new TestJourneyDeleted(e, DateUtils.getCurrentTimestemp());
			testJourneyDeletedRepository.save(deleted);
			testJourneyRepository.delete(e);
		});
		log.info("Delete Test Journey By Ids: Journy Ids({}) : AccountId: ({}): end", request.getIds(),
				account.getId());
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.success.ai.journey.deleted", null, "api.success.ai.journey.deleted.code"));
	}

	private Long validateRequest(CreateTestJourneyRequest request, List<JourneyElementMaster> elementList) {
		HashMap<Long, List<String>> elementsMap = new HashMap<>();
		HashMap<Long, String> elementsNameMap = new HashMap<>();

		for (JourneyElementMaster element : elementList) {
			elementsMap.put(element.getId(),
					element.getValues().stream().map(e -> e.getValue()).collect(Collectors.toList()));
			elementsNameMap.put(element.getId(), element.getName());
		}
		if (!elementsMap.get(JourneyElementMasterIds.FIRST_INTEREST.value()).contains(request.getFirstInterest())) {
			throwErrorMessage(request.getFirstInterest(),
					elementsNameMap.get(JourneyElementMasterIds.FIRST_INTEREST.value()));
		}
//		if (!StringUtils.isEmpty(request.getDecison())) {
//				&& !elementsMap.get(JourneyElementMasterIds.DECISION.value()).contains(request.getDecison())) 
//			throwErrorMessage(request.getDecison(), elementsNameMap.get(JourneyElementMasterIds.DECISION.value()));
//		}

		if (!StringUtils.isEmpty(request.getPurchaseAddCart()) && !elementsMap
				.get(JourneyElementMasterIds.PURCHASE_ADD_TO_CART.value()).contains(request.getPurchaseAddCart())) {
			throwErrorMessage(request.getPurchaseAddCart(),
					elementsNameMap.get(JourneyElementMasterIds.PURCHASE_ADD_TO_CART.value()));
		}

		if (!StringUtils.isEmpty(request.getPurchaseBuy())
				&& !elementsMap.get(JourneyElementMasterIds.PURCHASE_BUY.value()).contains(request.getPurchaseBuy())) {
			throwErrorMessage(request.getPurchaseBuy(),
					elementsNameMap.get(JourneyElementMasterIds.PURCHASE_BUY.value()));
		}

		if (!StringUtils.isEmpty(request.getPurchaseOwnership()) && !elementsMap
				.get(JourneyElementMasterIds.PURCHASE_RATE_PRODUCT.value()).contains(request.getPurchaseOwnership())) {
			throwErrorMessage(request.getPurchaseOwnership(),
					elementsNameMap.get(JourneyElementMasterIds.PURCHASE_RATE_PRODUCT.value()));
		}

		Long count = 0L;

		if (!StringUtils.isEmpty(request.getFirstInterest()))
			count++;
		if (!StringUtils.isEmpty(request.getDecison()))
			count++;
		if (!StringUtils.isEmpty(request.getPurchaseAddCart()))
			count++;
		if (!StringUtils.isEmpty(request.getPurchaseBuy()))
			count++;
		if (!StringUtils.isEmpty(request.getPurchaseOwnership()))
			count++;

		return count;
	}

	private void throwErrorMessage(String value, String element) {
		log.error("{} value Not Found in Element {}", value, element);
		throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.test-journey.element.not.found",
				new Object[] { value, element }, "api.error.test-journey.element.not.found.code"));
	}

}
