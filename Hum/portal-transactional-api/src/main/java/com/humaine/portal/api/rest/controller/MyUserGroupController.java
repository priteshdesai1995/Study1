package com.humaine.portal.api.rest.controller;

import java.text.NumberFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.humaine.portal.api.enums.GroupFlag;
import com.humaine.portal.api.es.repository.impl.ESUserEventRepository;
import com.humaine.portal.api.exception.APIException;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.BigFive;
import com.humaine.portal.api.model.MyUserGroupStatastics;
import com.humaine.portal.api.model.OLAPUserGroupDeleted;
import com.humaine.portal.api.model.PersonaDetailsMaster;
import com.humaine.portal.api.olap.model.OLAPUserGroup;
import com.humaine.portal.api.projection.model.OLAPUserGroupRank;
import com.humaine.portal.api.response.dto.CollapsedMyUserGroup;
import com.humaine.portal.api.response.dto.ConversionChartResultResponse;
import com.humaine.portal.api.response.dto.Persona;
import com.humaine.portal.api.response.dto.UserGroupMinMaxData;
import com.humaine.portal.api.response.dto.UserGroupRank;
import com.humaine.portal.api.rest.olap.repository.OLAPUserGroupMappingRepository;
import com.humaine.portal.api.rest.olap.repository.OLAPUserGroupRepository;
import com.humaine.portal.api.rest.repository.BigFiveRepository;
import com.humaine.portal.api.rest.repository.OLAPUserGroupDeletedRepository;
import com.humaine.portal.api.rest.repository.PersonaDetailsMasterRepository;
import com.humaine.portal.api.rest.service.AuthService;
import com.humaine.portal.api.security.config.AWSConfig;
import com.humaine.portal.api.util.CommonUtils;
import com.humaine.portal.api.util.DateUtils;
import com.humaine.portal.api.util.ErrorMessageUtils;
import com.humaine.portal.api.util.ResponseBuilder;
import com.humaine.portal.api.util.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "My User Group", description = "My User Group")
@RequestMapping("/my-usergroups")
public class MyUserGroupController {

	private static final Logger log = LogManager.getLogger(MyUserGroupController.class);

	@Autowired
	private AuthService authService;

	@Autowired
	private OLAPUserGroupRepository userGroupRepository;

	@Autowired
	private OLAPUserGroupDeletedRepository userGroupDeletedRepository;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Autowired
	private OLAPUserGroupMappingRepository userGroupMappingRepository;

	@Autowired
	private BigFiveRepository bigFiveRepository;

	@Autowired
	private ESUserEventRepository esUserEventRepository;

	@Autowired
	private PersonaDetailsMasterRepository personaDetailsMasterRepository;

	@Value("${attributes.empty.value}")
	private String emptyValue;

	@GetMapping(value = "", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get My User Group list", notes = "Get My User Group list", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getMyUserGroupList(HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("get My UserGroup List Request: start | Requested By(AccountId: {})", account.getId());
		List<OLAPUserGroup> groups = userGroupRepository.groupListByAccount(account.getId(),
				GroupFlag.MY_USER_GROUP.value());
		HashMap<String, List<OLAPUserGroup>> groupsMap = new HashMap<>();
		for (OLAPUserGroup group : groups) {
			if (!groupsMap.containsKey(group.getBigFive())) {
				groupsMap.put(group.getBigFive(), new ArrayList<OLAPUserGroup>());
			}
			group.setName(group.getGroupId());
			groupsMap.get(group.getBigFive()).add(group);
		}
		Iterable<BigFive> bigFives = bigFiveRepository.findAll();
		List<BigFive> bigFiveList = Lists.newArrayList(bigFives);
		Map<String, Long> bigFiveMap = new HashMap<>();
		for (BigFive bigFive : bigFiveList) {
			bigFiveMap.put(bigFive.getValue().toLowerCase(), bigFive.getId());
		}
		List<CollapsedMyUserGroup> result = new ArrayList<>();
		for (Map.Entry<String, List<OLAPUserGroup>> entry : groupsMap.entrySet()) {
			CollapsedMyUserGroup group = new CollapsedMyUserGroup(entry.getKey(),
					bigFiveMap.get(entry.getKey().toLowerCase()), new ArrayList<>(), new ArrayList<>(), null,
					entry.getValue(), 0L);
			List<Float> successMatch = entry.getValue().stream().filter(e -> {
				return e.getSuccessMatch() != null && e.getSuccessMatch() > 0;
			}).map(e -> e.getSuccessMatch()).collect(Collectors.toList());
			Double avarageValue = 0d;

			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(2);

			if (successMatch != null && successMatch.size() > 0) {
				OptionalDouble avarage = successMatch.stream().mapToDouble(e -> Double.valueOf(String.valueOf(e)))
						.average();
				avarageValue = Double.valueOf(nf.format(avarage.getAsDouble()));
			}
			group.setSuccessMatch(Float.valueOf(String.valueOf(avarageValue)));
			group.setTotalUserCount(entry.getValue().stream().filter(o -> o.getNoOfUser() != null)
					.mapToLong(OLAPUserGroup::getNoOfUser).sum());
			result.add(group);
		}
		log.info("get My UserGroup List Request: end | Requested By(AccountId: {})", account.getId());
		return ResponseBuilder.buildResponse(result);
	}

	@DeleteMapping(value = "{groupId}", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Delete Specific My User Group", notes = "Delete Specific My User Group", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> deleteMyUserGroup(@PathVariable(name = "groupId") String groupId, HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("delete UserGroup By Id Request: Group Id {} : start | Requested By(AccountId: {})", groupId,
				account.getId());
		OLAPUserGroup group = userGroupRepository.findGroupByAccountIdAndGroupId(account.getId(), groupId,
				GroupFlag.MY_USER_GROUP.value());
		if (group == null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user-group.not.found",
					new Object[] { groupId }, "api.error.user-group.not.found.code"));
		}

		OLAPUserGroupDeleted groupDeleted = new OLAPUserGroupDeleted(group);
		userGroupDeletedRepository.save(groupDeleted);
		userGroupRepository.delete(group);
		log.info("delete UserGroup By Id Request: Group Id {} : end | Requested By(AccountId: {})", groupId,
				account.getId());
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.success.user-group.delete", null, "api.success.user-group.delete.code"));
	}

	@GetMapping(value = "persona/{groupId}", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get User Group Persona Details", notes = "Get User Group Persona Details", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getUserGroupPersonaDetails(@PathVariable(name = "groupId") String groupId, HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("get My UserGroupBy Persona Details Request {} : start | Requested By(AccountId: {})", groupId,
				account.getId());
		OLAPUserGroup group = userGroupRepository.findGroupByAccountIdAndGroupId(account.getId(), groupId,
				GroupFlag.MY_USER_GROUP.value());
		if (group == null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user-group.not.found",
					new Object[] { groupId }, "api.error.user-group.not.found.code"));
		}
		BigFive bigFive = bigFiveRepository.getBigFiveIdByName(group.getBigFive());
		Optional<BigFive> res = bigFiveRepository.findById(bigFive.getId());
		if (!res.isEmpty())
			bigFive = res.get();
		UserGroupRank groupRank = userGroupRepository.getGroupsTopRankGroupByAccountAndBigFive(account.getId(),
				group.getBigFive(), GroupFlag.MY_USER_GROUP.value(), group.getGroupId());
//		Long userCount = 0L;
//		Long userCount = userGroupMappingRepository.getUserCount(account.getId(), groupId,
//				GroupFlag.MY_USER_GROUP.value());
		PersonaDetailsMaster pm = personaDetailsMasterRepository.getPersonaDetailsByValue(group.getBigFive(),
				group.getMotivationToBuy(), group.getPersuasiveStratergies(), group.getValues());
		Persona persona = new Persona(group.getNoOfUser(), null, group, bigFive, pm);
		if (groupRank != null) {
			persona.getGroup().setRank(groupRank.getRank());
		}
		persona.getDetails().setPersuasiveStrategies(persona.getDetails().getPersuasiveStrategies().stream().filter(e -> !emptyValue.equals(e)).collect(Collectors.toList()));
		persona.getDetails().setValues(persona.getDetails().getValues().stream().filter(e -> !emptyValue.equals(e)).collect(Collectors.toList()));
		persona.getDetails().setMotivationToBuy(persona.getDetails().getMotivationToBuy().stream().filter(e -> !emptyValue.equals(e)).collect(Collectors.toList()));
		log.info("get My UserGroupBy Persona Details Request {} : end | Requested By(AccountId: {})", groupId,
				account.getId());
		return ResponseBuilder.buildResponse(persona);
	}

	@GetMapping(value = "/groups/persona/{bigFiveId}", headers = {
			"version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get User Groups Persona Details By bigFiveId", notes = "Get User Groups Persona Details By bigFiveId", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getUserGroupPersonaDetailsByBigFiveId(
			@PathVariable(name = "bigFiveId") Long bigFiveId, HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("get My UserGroupBy Persona Details By BigFive Id Request {} : start | Requested By(AccountId: {})",
				bigFiveId, account.getId());
		Optional<BigFive> bigFive = bigFiveRepository.findById(bigFiveId);
		if (bigFive.isEmpty()) {
			throw new APIException(
					this.errorMessageUtils.getMessageWithCode("api.error.user-groups.not.found.with.bigfive-id",
							new Object[] { bigFiveId }, "api.error.user-groups.not.found.with.bigfive-id.code"));
		}
		List<OLAPUserGroup> userGroups = userGroupRepository.getGroupListByAccountAndBigFive(account.getId(),
				bigFive.get().getValue(), GroupFlag.MY_USER_GROUP.value());

//		Long userCount = userGroupMappingRepository.getUserCountByBigFive(account.getId(), bigFive.get().getValue(),
//				GroupFlag.MY_USER_GROUP.value());

		OLAPUserGroupRank rank = userGroupRepository.getGroupsTopRankGroupByAccountAndBigFive(account.getId(),
				bigFive.get().getValue(), GroupFlag.MY_USER_GROUP.value());
		OLAPUserGroup topGrp = userGroupRepository.findGroupByAccountIdAndGroupId(account.getId(), rank.getGroupId(), GroupFlag.MY_USER_GROUP.value());

		if (topGrp == null) {
			throw new APIException(
					this.errorMessageUtils.getMessageWithCode("api.error.user-groups.not.found.with.bigfive-id",
							new Object[] { bigFiveId }, "api.error.user-groups.not.found.with.bigfive-id.code"));
		}

		if (userGroups == null || userGroups != null && userGroups.isEmpty()) {
			throw new APIException(
					this.errorMessageUtils.getMessageWithCode("api.error.user-groups.not.found.with.bigfive-id",
							new Object[] { bigFiveId }, "api.error.user-groups.not.found.with.bigfive-id.code"));
		}
		List<String> ids = userGroups.stream().map(e -> e.getGroupId()).collect(Collectors.toList());
		UserGroupMinMaxData minMax = userGroupRepository.getMinMaxDataByIds(account.getId(),
				GroupFlag.MY_USER_GROUP.value(), ids);
		Map<String, PersonaDetailsMaster> personaDetailsMap = new HashMap<>();
		List<PersonaDetailsMaster> personaDetailsList = Lists.newArrayList(personaDetailsMasterRepository.findAll());
		personaDetailsList.stream().forEach(e -> {
			String key = CommonUtils.generateUnique(e.getBigFive().getValue()) + "_"
					+ CommonUtils.generateUnique(e.getBuy().getValue()) + "_"
					+ CommonUtils.generateUnique(e.getStrategies().getValue()) + "_"
					+ CommonUtils.generateUnique(e.getValues().getValue());
			personaDetailsMap.put(key, e);
		});
		Persona persona = new Persona(rank.getTotalUsers(), topGrp, rank.getRank(), userGroups, bigFive.get(),
				minMax, personaDetailsMap);
		persona.getDetails().setPersuasiveStrategies(persona.getDetails().getPersuasiveStrategies().stream().filter(e -> !emptyValue.equalsIgnoreCase(e)).collect(Collectors.toList()));
		persona.getDetails().setValues(persona.getDetails().getValues().stream().filter(e -> !emptyValue.equalsIgnoreCase(e)).collect(Collectors.toList()));
		persona.getDetails().setMotivationToBuy(persona.getDetails().getMotivationToBuy().stream().filter(e -> !emptyValue.equalsIgnoreCase(e)).collect(Collectors.toList()));
		return ResponseBuilder.buildResponse(persona);
	}

	@GetMapping(value = "/statistics", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get My User Group statastics count", notes = "Get My User Group statastics count", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getStatastics(HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		List<String> indexs = new ArrayList<>();
		OffsetDateTime current = DateUtils.getCurrentTimestemp();
		OffsetDateTime hoursDiff = DateUtils.getCurrentTimestemp().minusHours(23);
		indexs.add(esUserEventRepository.getIndexName(current));
		if (DateUtils.getFromatedDate(current) != DateUtils.getFromatedDate(hoursDiff)) {
			indexs.add(esUserEventRepository.getIndexName(hoursDiff));
		}
		ConversionChartResultResponse<Long> overallRate = esUserEventRepository.getOverallConversionRate(indexs,
				account.getId(), false);
		ConversionChartResultResponse<Double> userSpent = esUserEventRepository.getAvarageUserSpend(indexs,
				account.getId());
		return ResponseBuilder.buildResponse(new MyUserGroupStatastics(overallRate, userSpent));
	}
}
