package com.humaine.portal.api.rest.controller;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.humaine.portal.api.exception.APIException;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.AgeGroup;
import com.humaine.portal.api.model.BigFive;
import com.humaine.portal.api.model.Buying;
import com.humaine.portal.api.model.Education;
import com.humaine.portal.api.model.FamilySize;
import com.humaine.portal.api.model.Persuasive;
import com.humaine.portal.api.model.UserGroup;
import com.humaine.portal.api.model.UserGroupDeleted;
import com.humaine.portal.api.model.Values;
import com.humaine.portal.api.request.dto.MultipleDeleteRequests;
import com.humaine.portal.api.request.dto.UserGroupRequest;
import com.humaine.portal.api.request.dto.UserGroupUpdateRequest;
import com.humaine.portal.api.response.dto.CognitiveAttributes;
import com.humaine.portal.api.response.dto.DemoGraphicAttributes;
import com.humaine.portal.api.response.dto.Persona;
import com.humaine.portal.api.response.dto.UserGroupDetails;
import com.humaine.portal.api.response.dto.UserGroupListResponse;
import com.humaine.portal.api.rest.olap.service.OLAPManualUserGroupService;
import com.humaine.portal.api.rest.repository.AgeGroupRepository;
import com.humaine.portal.api.rest.repository.BigFiveRepository;
import com.humaine.portal.api.rest.repository.BuyingRepository;
import com.humaine.portal.api.rest.repository.EducationRepository;
import com.humaine.portal.api.rest.repository.FamilySizeRepository;
import com.humaine.portal.api.rest.repository.PersuasiveRepository;
import com.humaine.portal.api.rest.repository.TestJourneyRepository;
import com.humaine.portal.api.rest.repository.UserGroupDeletedRepository;
import com.humaine.portal.api.rest.repository.UserGroupRepository;
import com.humaine.portal.api.rest.repository.ValuesRepository;
import com.humaine.portal.api.rest.service.AuthService;
import com.humaine.portal.api.rest.service.EthnicityService;
import com.humaine.portal.api.rest.service.GenderService;
import com.humaine.portal.api.rest.service.UserGroupService;
import com.humaine.portal.api.security.config.AWSConfig;
import com.humaine.portal.api.util.ErrorMessageUtils;
import com.humaine.portal.api.util.ResponseBuilder;
import com.humaine.portal.api.util.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "User Group", description = "User Group")
public class UserGroupController {

	private static final Logger log = LogManager.getLogger(UserGroupController.class);

	@Autowired
	private AgeGroupRepository ageGroupRepository;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Autowired
	private EthnicityService ethnicityService;

	@Autowired
	private FamilySizeRepository familySizeRepository;

	@Autowired
	private BigFiveRepository bigFiveRepository;

	@Autowired
	private EducationRepository educationRepository;

	@Autowired
	private UserGroupService userGroupService;

	@Autowired
	private GenderService genderService;

	@Autowired
	private UserGroupRepository userGroupRepository;

	@Autowired
	private AuthService authService;

	@Autowired
	private UserGroupDeletedRepository userGroupDeletedRepository;

	@Autowired
	private ValuesRepository valuesRepository;

	@Autowired
	private PersuasiveRepository persuasiveRepository;

	@Autowired
	private BuyingRepository buyingRepository;

	@Autowired
	private OLAPManualUserGroupService manualUserGroupService;

	@Autowired
	private TestJourneyRepository testJourneyRepository;

	@Value("${attributes.empty.value}")
	private String attributeEmptyValue;

	@GetMapping(value = "demographic", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get user group demographic's master data", notes = "Get user group demographic's master data", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getDemographicAttributes() {
		log.info("Get demographic Attribute Request: start");
		DemoGraphicAttributes response = new DemoGraphicAttributes();

		response.setGender(genderService.getGenderList());

		Iterable<AgeGroup> ageGroups = ageGroupRepository.findAll();
		response.setAgeGroup(Lists.newArrayList(ageGroups));

		Iterable<FamilySize> familySizes = familySizeRepository.findAll();
		response.setFamilySize(Lists.newArrayList(familySizes));

		response.setEthnicity(ethnicityService.getEthnicityList());

		Iterable<BigFive> bigFives = bigFiveRepository.findAll();
		response.setBigFive(Lists.newArrayList(bigFives));

		Iterable<Education> educations = educationRepository.findAll();
		response.setEducation(Lists.newArrayList(educations));
		log.info("Get demographic Attribute Request: end");
		return ResponseBuilder.buildResponse(response);
	}

	@GetMapping(value = "cognitive/{bigFive}", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get user group cognitive profile master data", notes = "Get user group cognitive profile master data", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getCognitiveAttributes(@PathVariable(name = "bigFive") Long id) {
		log.info("Get cognitive Attribute Request: for BigFive Id => {}: start");
		CognitiveAttributes response = new CognitiveAttributes();
		if (id == null) {
			log.error("BigFive Id must not be null or empty: {}", id);
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.bigfive.null", new Object[] {},
					"api.error.bigfive.null.code"));
		}
		Optional<BigFive> bigFive = bigFiveRepository.findById(id);
		if (bigFive.isEmpty()) {
			log.error("No BigFive find with Id: {}", id);
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.bigfive.not.found",
					new Object[] { id }, "api.error.bigfive.not.found.code"));
		}
		BigFive result = bigFive.get();
		response.setMotivationToBuy(sortSet(result.getBuying()));
		response.setPersuasiveStrategies(sortSet(result.getPersuasive()));
		response.setValues(sortSet(result.getValues()));
		if (response.getMotivationToBuy().isEmpty()) {
			Buying emptyAttribute = buyingRepository.getAttributeByName(attributeEmptyValue);
			if (emptyAttribute != null) {
				response.setMotivationToBuyEmpty(emptyAttribute);
			}
		}

		if (response.getValues().isEmpty()) {
			Values emptyAttribute = valuesRepository.getAttributeByName(attributeEmptyValue);
			if (emptyAttribute != null) {
				response.setValuesEmpty(emptyAttribute);
			}
		}

		if (response.getPersuasiveStrategies().isEmpty()) {
			Persuasive emptyAttribute = persuasiveRepository.getAttributeByName(attributeEmptyValue);
			if (emptyAttribute != null) {
				response.setPersuasiveStrategiesEmpty(emptyAttribute);
			}
		}
		log.info("Get cognitive Attribute Request: for BigFive Id => {}: end", id);
		return ResponseBuilder.buildResponse(response);
	}

	@PostMapping(value = "usergroup", headers = {
			"version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Manual User Group Create", notes = "Manual User Group Create", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> createUserGroup(@Valid @RequestBody UserGroupRequest request, HttpServletRequest httpRequest) {
		log.info("Create UserGroup Request: start");
		log.info("Request Body: {}", request);
		userGroupService.createUserGroup(request, httpRequest);
		log.info("Create UserGroup Request: for BigFive Id => {}: start");
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.success.user-group.create", null, "api.success.user-group.create.code"));
	}

	@GetMapping(value = "usergroups/{groupId}", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Specific Manual User Group", notes = "Get Specific Manual User Group", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getUserGroup(@PathVariable(name = "groupId") Long groupId, HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("get UserGroupBy Id Request: Group Id => {} : start | Requested By(AccountId: {})", groupId,
				account.getId());
		UserGroup group = userGroupRepository.findGroupByAccountIdAndGroupId(account.getId(), groupId);
		if (group == null) {
			log.error("No Grop Found With Id: {}", groupId);
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user-group.not.found",
					new Object[] { groupId }, "api.error.user-group.not.found.code"));
		}
		log.info("get UserGroupBy Id Request: Group Id => {} : end | Requested By(AccountId: {})", groupId,
				account.getId());
		if (group.getEthnicity() != null && attributeEmptyValue.equals(group.getEthnicity().getValue())) {
			group.setEthnicity(null);
		}
		return ResponseBuilder.buildResponse(new UserGroupDetails(group));
	}

	@GetMapping(value = "usergroups", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Manual User Group list", notes = "Get Manual User Group list", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getUserGroupList(HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("get UserGroup List Request: start | Requested By(AccountId: {})", account.getId());
		List<UserGroup> groups = userGroupRepository.groupListByAccount(account.getId());
		List<UserGroupListResponse> userGroupResp = groups.stream()
				.map(g -> new UserGroupListResponse(g, attributeEmptyValue)).collect(Collectors.toList());
		manualUserGroupService.fillSuccessMatchForList(userGroupResp, account.getId());
		log.info("get UserGroup List Request: end | Requested By(AccountId: {})", account.getId());
		return ResponseBuilder.buildResponse(userGroupResp);
	}

	@DeleteMapping(value = "/usergroup/{groupId}", headers = {
			"version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Delete Specific Manual User Group", notes = "Delete Specific Manual User Group", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> deleteUserGroup(@PathVariable(name = "groupId") Long groupId, HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("delete UserGroup By Id Request: Group Id {} : start | Requested By(AccountId: {})", groupId,
				account.getId());
		UserGroup group = userGroupRepository.findGroupByAccountIdAndGroupId(account.getId(), groupId);
		if (group == null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user-group.not.found",
					new Object[] { groupId }, "api.error.user-group.not.found.code"));
		}
		Long count = testJourneyRepository.getJourniesCountByGroupId(account.getId(), groupId);
		if (count > 0) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.group.deleted.journey.exist",
					new Object[] { group.getName(), count }, "api.error.group.deleted.journey.exist.code"));
		}
		UserGroupDeleted groupDeleted = new UserGroupDeleted(group);
		userGroupDeletedRepository.save(groupDeleted);
		userGroupRepository.delete(group);
		log.info("delete UserGroup By Id Request: Group Id {} : end | Requested By(AccountId: {})", groupId,
				account.getId());
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.success.user-group.delete", null, "api.success.user-group.delete.code"));
	}

	@DeleteMapping(value = "/usergroup", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Delete Multiple Manual User Group By Ids", notes = "Delete Multiple Manual User Group By Ids", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> deleteUserGroups(@RequestBody MultipleDeleteRequests<Long> request, HttpServletRequest httpRequest) {
		if (request == null)
			request = new MultipleDeleteRequests<>();
		if (request.getIds() == null)
			request.setIds(new ArrayList<Long>());
		if (request.getIds().isEmpty()) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.usergroup.delete.ids.empty",
					new Object[] {}, "api.error.usergroup.delete.ids.empty"));
		}
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("delete UserGroup By Id Request: Group Ids {} : start | Requested By(AccountId: {})", request.getIds(),
				account.getId());
		List<UserGroup> groups = userGroupRepository.findGroupByAccountIdAndGroupIds(account.getId(), request.getIds());

		groups.stream().forEach(e -> {
			UserGroupDeleted groupDeleted = new UserGroupDeleted(e);
			userGroupDeletedRepository.save(groupDeleted);
			userGroupRepository.delete(e);
		});
		log.info("delete UserGroup By Id Request: Group Ids {} : end | Requested By(AccountId: {})", request.getIds(),
				account.getId());
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.success.user-group.delete", null, "api.success.user-group.delete.code"));
	}

	@GetMapping(value = "usergroups/persona/{groupId}", headers = {
			"version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get User Group Persona Details", notes = "Get User Group Persona Details", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getUserGroupPersonaDetails(@PathVariable(name = "groupId") Long groupId, HttpServletRequest httpRequest) {
		log.info("get UserGroupBy Persona Details Request: Group Id => {} : start)", groupId);
		Persona persona = userGroupService.getPersonaDetails(groupId, httpRequest);
		log.info("get UserGroupBy Persona Details Request: Group Id => {} : end)", groupId);
		return ResponseBuilder.buildResponse(persona);
	}

	@PostMapping(value = "usergroup/{groupId}", headers = {
			"version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update Manual User Group Name", notes = "Update Manual User Group Name", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> updateGroupName(@Valid @RequestBody UserGroupUpdateRequest request,
			@PathVariable(name = "groupId") Long groupId, HttpServletRequest httpRequest) {
		log.info("Update UserGroup Request: start");
		log.info("Request Body: {}", request);
		try {
			userGroupService.updateUserGroup(request, groupId, httpRequest);
		} catch (Exception e) {
			log.error("User Group Name is invalid");
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user-group.name.invalid",
					new Object[] { "" }, "api.error.user-group.name.invalid.code"));
		}
		log.info("Update UserGroup Request: start");
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.success.user-group.update", null, "api.success.user-group.update.code"));
	}

	private <T> LinkedHashSet<T> sortSet(Set<T> setObject) {
		if (setObject == null)
			return new LinkedHashSet<T>();
		return setObject.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
	}
}
