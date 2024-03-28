package com.humaine.portal.api.rest.controller;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humaine.portal.api.enums.GroupFlag;
import com.humaine.portal.api.exception.APIException;
import com.humaine.portal.api.model.AIUserGroup;
import com.humaine.portal.api.model.AIUserGroupDeleted;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.BigFive;
import com.humaine.portal.api.model.OLAPUserGroupDeleted;
import com.humaine.portal.api.model.PersonaDetailsMaster;
import com.humaine.portal.api.olap.model.OLAPUserGroup;
import com.humaine.portal.api.projection.model.RankWithUserCount;
import com.humaine.portal.api.request.dto.SaveAIUserGroupRequest;
import com.humaine.portal.api.response.dto.Persona;
import com.humaine.portal.api.response.dto.UserGroupRank;
import com.humaine.portal.api.rest.olap.repository.OLAPUserGroupMappingRepository;
import com.humaine.portal.api.rest.olap.repository.OLAPUserGroupRepository;
import com.humaine.portal.api.rest.repository.AIUserGroupDeletedRepository;
import com.humaine.portal.api.rest.repository.AIUserGroupRepository;
import com.humaine.portal.api.rest.repository.BigFiveRepository;
import com.humaine.portal.api.rest.repository.OLAPUserGroupDeletedRepository;
import com.humaine.portal.api.rest.repository.PersonaDetailsMasterRepository;
import com.humaine.portal.api.rest.service.AuthService;
import com.humaine.portal.api.security.config.AWSConfig;
import com.humaine.portal.api.util.AvatarImageUtils;
import com.humaine.portal.api.util.DateUtils;
import com.humaine.portal.api.util.ErrorMessageUtils;
import com.humaine.portal.api.util.ResponseBuilder;
import com.humaine.portal.api.util.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "AI Generated Group", description = "AI Generated Group")
@RequestMapping("/ai-usergroups")
public class AIUserGroupController {

	private static final Logger log = LogManager.getLogger(AIUserGroupController.class);

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Autowired
	private AuthService authService;

	@Autowired
	private OLAPUserGroupRepository userGroupRepository;

	@Autowired
	private AIUserGroupRepository aiUserGroupRepository;

	@Autowired
	private AIUserGroupDeletedRepository aiUserGroupDeletedRepository;

	@Autowired
	private AIUserGroupDeletedRepository userGroupDeletedRepository;

	@Autowired
	private OLAPUserGroupDeletedRepository olapUserGroupDeletedRepository;

	@Autowired
	private AvatarImageUtils avatarUtils;

	@Autowired
	private BigFiveRepository bigFiveRepository;

	@Autowired
	private OLAPUserGroupMappingRepository userGroupMappingRepository;
	
	@Autowired
	private PersonaDetailsMasterRepository personaDetailsMasterRepository;

	@GetMapping(value = "", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get AI User Group list", notes = "Get AI User Group list", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getUserGroupList(HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("get AI Generated UserGroup List Request: start | Requested By(AccountId: {})", account.getId());
		List<String> ids = aiUserGroupRepository.getGroupIdsByAccount(account.getId());
		List<OLAPUserGroup> groups = new ArrayList<OLAPUserGroup>();
		if (ids.isEmpty()) {
			groups = userGroupRepository.groupListByAccount(account.getId(), GroupFlag.AI_GENERATED.value());
			groups.forEach(e -> {
				e.setName(e.getGroupId());
			});
		} else {
			groups = userGroupRepository.getGroupListByAccount(account.getId(), ids, GroupFlag.AI_GENERATED.value());
			groups.forEach(e -> {
				e.setName(e.getGroupId());
			});
		}
		log.info("get AI Generated UserGroup List Request: end | Requested By(AccountId: {})", account.getId());
		return ResponseBuilder.buildResponse(groups);
	}

	@GetMapping(value = "saved", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Saved AI User Group list", notes = "Get Saved AI User Group list", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getSavedUserGroupList(HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true,  httpRequest);
		log.info("get Saved AI Generated UserGroup List Request: start | Requested By(AccountId: {})", account.getId());
		List<AIUserGroup> groups = aiUserGroupRepository.getGroupListByAccount(account.getId());
		log.info("get Saved AI Generated UserGroup List Request: end | Requested By(AccountId: {})", account.getId());
		return ResponseBuilder.buildResponse(groups);
	}

	@PostMapping(value = "save", headers = {
			"version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Save AI User Group", notes = "Save AI User Group", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> saveAIGenerateGroup(@Valid @RequestBody SaveAIUserGroupRequest request, HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("Save AI Generated UserGroup Request: start | Requested By(AccountId: {})", account.getId());
		List<OLAPUserGroup> group = userGroupRepository.getGroupListByIDsAndAccount(account.getId(),
				request.getGroupIds(), GroupFlag.AI_GENERATED.value());
		List<String> foundIds = group.stream().map(e -> e.getGroupId()).collect(Collectors.toList());
		
		if (group == null || group != null && !foundIds.containsAll(request.getGroupIds())) {
			log.error("No Group Found With Ids: {}", request.getGroupIds());
			List<String> notFoundIds = request.getGroupIds().stream().filter(e -> !foundIds.contains(e))
					.collect(Collectors.toList());

			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user-group.not.found",
					new Object[] { notFoundIds }, "api.error.user-group.not.found.code"));
		}
		List<String> ids = aiUserGroupRepository.findGroupsByAccountAndIds(account.getId(), request.getGroupIds());

		if (ids != null && !ids.isEmpty()) {
			log.error("No Group Found With Ids: {}", ids);
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user-group.not.found",
					new Object[] { ids }, "api.error.user-group.not.found.code"));
		}
		OffsetDateTime timestamp = DateUtils.getCurrentTimestemp();
		List<AIUserGroup> aiUserGroups = group.stream().map(e -> {
			AIUserGroup saveGroup = new AIUserGroup(e);
			saveGroup.setIcon(avatarUtils.getRandomAvatar());
			saveGroup.setTimestamp(timestamp);
			return saveGroup;
		}).collect(Collectors.toList());
		aiUserGroupRepository.saveAll(aiUserGroups);

		log.info("Save AI Generated UserGroup Request: end | Requested By(AccountId: {})", account.getId());
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.success.ai.user-group.saved", null, "api.success.ai.user-group.saved.code"));
	}

	@DeleteMapping(value = "/saved/{groupId}", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Delete Saved AI User Group", notes = "Delete Saved AI User Group", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> deleteSavedAIUserGroup(@PathVariable(name = "groupId") Long groupId, HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("delete Saed AI UserGroup By Id Request: Group Id {} : start | Requested By(AccountId: {})", groupId,
				account.getId());
		AIUserGroup group = aiUserGroupRepository.findGroupByAccountIdAndGroupId(account.getId(), groupId);
		if (group == null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user-group.not.found",
					new Object[] { groupId }, "api.error.user-group.not.found.code"));
		}
		AIUserGroupDeleted groupDeleted = new AIUserGroupDeleted(group);
		userGroupDeletedRepository.save(groupDeleted);
		aiUserGroupRepository.delete(group);
		log.info("delete Saed AI UserGroup By Id Request: Group Id {} : end | Requested By(AccountId: {})", groupId,
				account.getId());
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.success.user-group.delete", null, "api.success.user-group.delete.code"));
	}

	@DeleteMapping(value = "{groupId}", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Delete AI User Group", notes = "Delete AI User Group", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> deleteAIUserGroup(@PathVariable(name = "groupId") String groupId, HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("delete AI UserGroup By Id Request: Group Id {} : start | Requested By(AccountId: {})", groupId,
				account.getId());
		OLAPUserGroup group = userGroupRepository.findGroupByAccountIdAndGroupId(account.getId(), groupId,
				GroupFlag.AI_GENERATED.value());
		if (group == null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user-group.not.found",
					new Object[] { groupId }, "api.error.user-group.not.found.code"));
		}
		OLAPUserGroupDeleted groupDeleted = new OLAPUserGroupDeleted(group);
		olapUserGroupDeletedRepository.save(groupDeleted);
		userGroupRepository.delete(group);
		log.info("delete AI UserGroup By Id Request: Group Id {} : end | Requested By(AccountId: {})", groupId,
				account.getId());
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.success.user-group.delete", null, "api.success.user-group.delete.code"));
	}

	@GetMapping(value = "/saved/persona/{groupId}", headers = {
			"version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get User Group Persona Details", notes = "Get User Group Persona Details", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getSavedUserGroupPersonaDetails(
			@PathVariable(name = "groupId") Long groupId, HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true,  httpRequest);
		log.info("get Saved AI UserGroupBy Persona Details Request {} : start | Requested By(AccountId: {})", groupId,
				account.getId());
		AIUserGroup group = aiUserGroupRepository.findGroupByAccountIdAndGroupId(account.getId(), groupId);
		if (group == null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user-group.not.found",
					new Object[] { groupId }, "api.error.user-group.not.found.code"));
		}
		Long userCount = userGroupMappingRepository.getUserCount(account.getId(), group.getUserGroupId(),
				GroupFlag.AI_GENERATED.value());
		BigFive bigFive = bigFiveRepository.getBigFiveIdByName(group.getBigFive());
		Optional<BigFive> res = bigFiveRepository.findById(bigFive.getId());
		if (!res.isEmpty())
			bigFive = res.get();
		PersonaDetailsMaster pm = personaDetailsMasterRepository.getPersonaDetailsByValue(group.getBigFive(),
				group.getMotivationToBuy(), group.getPersuasiveStratergies(), group.getValues());
		Persona persona = new Persona(userCount, null, group, bigFive, pm);
		UserGroupRank rank = aiUserGroupRepository.getGroupTopRankGroupByAccount(account.getId(),
				group.getUserGroupId());
		if (rank != null) {
			persona.getGroup().setRank(rank.getRank());
		}
		log.info("get Saved AI UserGroupBy Persona Details Request {} : end | Requested By(AccountId: {})", groupId,
				account.getId());
		return ResponseBuilder.buildResponse(persona);
	}

	@GetMapping(value = "/persona/{groupId}", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get User Group Persona Details", notes = "Get User Group Persona Details", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getUserGroupPersonaDetails(@PathVariable(name = "groupId") String groupId, HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("get AI UserGroupBy Persona Details Request {} : start | Requested By(AccountId: {})", groupId,
				account.getId());
		OLAPUserGroup group = userGroupRepository.findGroupByAccountIdAndGroupId(account.getId(), groupId,
				GroupFlag.AI_GENERATED.value());
		if (group == null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user-group.not.found",
					new Object[] { groupId }, "api.error.user-group.not.found.code"));
		}
//		Long userCount = userGroupMappingRepository.getUserCount(account.getId(), groupId,
//				GroupFlag.AI_GENERATED.value());
		BigFive bigFive = bigFiveRepository.getBigFiveIdByName(group.getBigFive());
		Optional<BigFive> res = bigFiveRepository.findById(bigFive.getId());
		if (!res.isEmpty())
			bigFive = res.get();
		
		RankWithUserCount groupRank = userGroupRepository.findGroupRankByAccountIdAndGroupId(account.getId(), groupId,
				GroupFlag.AI_GENERATED.value());
		PersonaDetailsMaster pm = personaDetailsMasterRepository.getPersonaDetailsByValue(group.getBigFive(),
				group.getMotivationToBuy(), group.getPersuasiveStratergies(), group.getValues());
		Persona persona = new Persona(groupRank.getUserCount(), groupRank.getRank(), group, bigFive, pm);
		log.info("get AI UserGroupBy Persona Details Request {} : end | Requested By(AccountId: {})", groupId,
				account.getId());
		return ResponseBuilder.buildResponse(persona);
	}
}
