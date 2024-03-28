package com.humaine.portal.api.rest.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.humaine.portal.api.enums.GroupFlag;
import com.humaine.portal.api.exception.APIException;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.BigFive;
import com.humaine.portal.api.model.Buying;
import com.humaine.portal.api.model.Ethnicity;
import com.humaine.portal.api.model.Gender;
import com.humaine.portal.api.model.ManualUserGroupRank;
import com.humaine.portal.api.model.PersonaDetailsMaster;
import com.humaine.portal.api.model.Persuasive;
import com.humaine.portal.api.model.UserGroup;
import com.humaine.portal.api.model.Values;
import com.humaine.portal.api.request.dto.UserGroupRequest;
import com.humaine.portal.api.request.dto.UserGroupUpdateRequest;
import com.humaine.portal.api.response.dto.Persona;
import com.humaine.portal.api.rest.olap.repository.OLAPManualUserGroupRepository;
import com.humaine.portal.api.rest.olap.repository.OLAPUserGroupMappingRepository;
import com.humaine.portal.api.rest.repository.BigFiveRepository;
import com.humaine.portal.api.rest.repository.BuyingRepository;
import com.humaine.portal.api.rest.repository.PersonaDetailsMasterRepository;
import com.humaine.portal.api.rest.repository.PersuasiveRepository;
import com.humaine.portal.api.rest.repository.UserGroupRepository;
import com.humaine.portal.api.rest.repository.ValuesRepository;
import com.humaine.portal.api.rest.service.AuthService;
import com.humaine.portal.api.rest.service.EthnicityService;
import com.humaine.portal.api.rest.service.GenderService;
import com.humaine.portal.api.rest.service.UserGroupService;
import com.humaine.portal.api.util.AvatarImageUtils;
import com.humaine.portal.api.util.ErrorMessageUtils;
import javax.servlet.http.HttpServletRequest;
@Service
public class UserGroupServiceImpl implements UserGroupService {

	private static final Logger log = LogManager.getLogger(UserGroupServiceImpl.class);

	@Autowired
	private UserGroupRepository userGroupRepository;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Autowired
	private AuthService authService;

	@Autowired
	private BigFiveRepository bigFiveRepository;

	@Autowired
	private AvatarImageUtils avatarImageUtils;

	@Autowired
	private ValuesRepository valuesRepository;

	@Autowired
	private PersuasiveRepository persuasiveRepository;

	@Autowired
	private BuyingRepository buyingRepository;

	@Autowired
	private EthnicityService ethnicityService;

	@Autowired
	private GenderService genderService;

	@Autowired
	private OLAPUserGroupMappingRepository userGroupMappingRepository;

	@Autowired
	private OLAPManualUserGroupRepository manualUserGroupRepository;

	@Value("${attributes.empty.value}")
	private String attributeEmptyValue;

	@Autowired
	private PersonaDetailsMasterRepository personaDetailsMasterRepository;

	@Override
	public void checkUserGroupNameExist(Account account, String groupName) {
		log.info("Checking GroupName Exist with Name: {}, in Account: {} start", groupName, account.getId());
		UserGroup group = userGroupRepository.findGroupByAccountIdAndName(account.getId(), groupName);
		if (group != null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user-group.already.exist",
					new Object[] { groupName }, "api.error.user-group.already.exist.code"));
		}
		
		boolean isGroupNameValid = groupName.matches("^[a-zA-Z0-9 _]{1,64}$");
        if(!isGroupNameValid) {
            log.error("Group Name is not valid because It contains special chars");
            throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user-group.name.invalid",
                    new Object[] { groupName }, "api.error.user-group.name.invalid.code"));
        }
		
		log.info("Checking GroupName Exist with Name: {}, in Account: {} ends", groupName, account.getId());
	}

	@Override
		public UserGroup createUserGroup(UserGroupRequest request, HttpServletRequest httpRequest) {
		log.info("Create UserGroup start");
		Account account = authService.getLoginUserAccount(true, httpRequest);
		checkUserGroupNameExist(account, request.getName());
		if (request.getEthnicity() == null) {
			Ethnicity emptyEthnicity = ethnicityService.getEmptyEthnicityValue();
			if (emptyEthnicity != null) {
				request.setEthnicity(emptyEthnicity.getId());
			}
		}

		if (request.getGender() == null) {
			Gender emptyGender = genderService.getEmptyGenderValue();
			if (emptyGender != null) {
				request.setGender(emptyGender.getId());
			}
		}
		validateRequest(request);
		UserGroup userGroup = new UserGroup(request, account);
		userGroup.setIcon(avatarImageUtils.getRandomAvatar());
		userGroup = userGroupRepository.save(userGroup);
		log.info("Create UserGroup end");
		return userGroup;
	}

	@Override
	public void validateRequest(UserGroupRequest request) {
		log.info("Validate UserGroupCreate Request start");
		Optional<BigFive> bigFive = bigFiveRepository.findById(request.getBigFive());
		if (bigFive.isEmpty()) {
			log.info("BigFive value should not be null or empty");
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.big-five.not.found",
					new Object[] { request.getBigFive() }, "api.error.big-five.not.found.code"));
		}
		BigFive bigFiveResult = bigFive.get();
		List<Long> valueIds = bigFiveResult.getValues().stream().map(e -> e.getId()).collect(Collectors.toList());
		List<Long> persuasiveIds = bigFiveResult.getPersuasive().stream().map(e -> e.getId())
				.collect(Collectors.toList());
		List<Long> buyingIds = bigFiveResult.getBuying().stream().map(e -> e.getId()).collect(Collectors.toList());
		if (!valueIds.contains(request.getValues())) {
			boolean isError = true;
			if (valueIds.isEmpty()) {
				Values emptyAttribute = valuesRepository.getAttributeByName(attributeEmptyValue);
				if (emptyAttribute != null && emptyAttribute.getId().equals(request.getValues())) {
					isError = false;
				}
			}
			if (isError)
				throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.values.not.found",
						new Object[] { request.getValues() }, "api.error.values.not.found.code"));
		}

		if (!persuasiveIds.contains(request.getPersuasiveStratergies())) {
			boolean isError = true;
			if (persuasiveIds.isEmpty()) {
				Persuasive emptyAttribute = persuasiveRepository.getAttributeByName(attributeEmptyValue);
				if (emptyAttribute != null && emptyAttribute.getId().equals(request.getPersuasiveStratergies())) {
					isError = false;
				}
			}
			if (isError)
				throw new APIException(
						this.errorMessageUtils.getMessageWithCode("api.error.persuasive-stratergies.not.found",
								new Object[] { request.getPersuasiveStratergies() },
								"api.error.persuasive-stratergies.not.found.code"));
		}

		if (!buyingIds.contains(request.getMotivationToBuy())) {
			boolean isError = true;
			if (buyingIds.isEmpty()) {
				Buying emptyAttribute = buyingRepository.getAttributeByName(attributeEmptyValue);
				if (emptyAttribute != null && emptyAttribute.getId().equals(request.getMotivationToBuy())) {
					isError = false;
				}
			}
			if (isError)
				throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.buying.not.found",
						new Object[] { request.getMotivationToBuy() }, "api.error.buying.not.found.code"));
		}
		log.info("Validate UserGroupCreate Request end");
	}

	@Override
	public UserGroup updateUserGroup(UserGroupRequest request, Long groupId, HttpServletRequest httpRequest) {
		log.info("Update usergroup start");
		Account account = authService.getLoginUserAccount(true, httpRequest);
		UserGroup group = userGroupRepository.findGroupByAccountIdAndGroupId(account.getId(), groupId);
		if (group == null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user-group.not.found",
					new Object[] { groupId }, "api.error.user-group.not.found.code"));
		}
		checkUserGroupNameExist(account, groupId, request.getName());
		validateRequest(request);
		UserGroup userGroup = new UserGroup(request, account, groupId);
		userGroup = userGroupRepository.save(userGroup);
		log.info("Update usergroup end");
		return userGroup;
	}

	@Override
	public void checkUserGroupNameExist(Account account, Long groupId, String groupName) {
		UserGroup group = userGroupRepository.findGroupByAccountIdAndName(account.getId(), groupId, groupName);
		if (group != null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user-group.not.found",
					new Object[] { groupId }, "api.error.user-group.not.found.code"));
		}
	}

	@Override
	public Persona getPersonaDetails(Long groupId, HttpServletRequest httpRequest) {
		log.info("get Persona Details start");
		Account account = authService.getLoginUserAccount(true,  httpRequest);
		UserGroup group = userGroupRepository.findGroupByAccountIdAndGroupId(account.getId(), groupId);
		if (group == null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user-group.not.found",
					new Object[] { groupId }, "api.error.user-group.not.found.code"));
		}
		List<Long> ids = userGroupRepository.groupIdListByAccount(account.getId());

//		Long userCount = userGroupMappingRepository.getUserCount(account.getId(), group.getId(),
//				GroupFlag.MANUAL.value());
		Long userCount = 0L;

		Optional<BigFive> res = bigFiveRepository.findById(group.getBigFive().getId());
		if (!res.isEmpty()) {
			group.setBigFive(res.get());
		}

		PersonaDetailsMaster pm = personaDetailsMasterRepository.getPersonaDetailsByIds(group.getBigFive().getId(),
				group.getBuying().getId(), group.getPersuasive().getId(), group.getValues().getId());

		Persona persona = new Persona(userCount, null, group, pm);

		if (!ids.isEmpty()) {
			ManualUserGroupRank userGroupsRank = manualUserGroupRepository.getGroupRankByIds(account.getId(),
					ids.stream().map(e -> e).collect(Collectors.toList()), group.getId());
			if (userGroupsRank != null) {
				persona.getGroup().setRank(userGroupsRank.getRank());
				persona.setUserCount(userGroupsRank.getNoOfUsers());
			}
		}
		log.info("get Persona Details end");
		return persona;
	}

	@Override
	public void updateUserGroup(UserGroupUpdateRequest request, Long groupId, HttpServletRequest httpRequest) {
		log.info("Update User Group Details start");
		Account account = authService.getLoginUserAccount(true, httpRequest);
		UserGroup group = userGroupRepository.findGroupByAccountIdAndGroupId(account.getId(), groupId);
		if (group == null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user-group.not.found",
					new Object[] { groupId }, "api.error.user-group.not.found.code"));
		}
		UserGroup checkGroup = userGroupRepository.findGroupByAccountIdAndName(account.getId(), groupId,
				request.getName());
		if (checkGroup != null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user-group.already.exist",
					new Object[] { request.getName() }, "api.error.user-group.already.exist.code"));
		}
		group.setName(request.getName());
		userGroupRepository.save(group);
		log.info("Update User Group Details end");
	}

}
