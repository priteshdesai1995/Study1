package com.humaine.portal.api.rest.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.humaine.portal.api.model.AgeGroup;
import com.humaine.portal.api.model.BigFive;
import com.humaine.portal.api.model.Buying;
import com.humaine.portal.api.model.Education;
import com.humaine.portal.api.model.Ethnicity;
import com.humaine.portal.api.model.FamilySize;
import com.humaine.portal.api.model.Gender;
import com.humaine.portal.api.model.ManualUserGroupRank;
import com.humaine.portal.api.model.Persuasive;
import com.humaine.portal.api.model.UserGroup;
import com.humaine.portal.api.model.UserGroupDeleted;
import com.humaine.portal.api.model.Values;
import com.humaine.portal.api.olap.model.OLAPManualUserGroup;
import com.humaine.portal.api.olap.model.OLAPUserGroupMapping;
import com.humaine.portal.api.request.dto.UserGroupRequest;
import com.humaine.portal.api.request.dto.UserGroupUpdateRequest;
import com.humaine.portal.api.response.dto.Persona;
import com.humaine.portal.api.response.dto.UserGroupResponse;
import com.humaine.portal.api.rest.controller.mock.data.AuthenticationMockData;
import com.humaine.portal.api.rest.controller.mock.data.UserGroupMockData;
import com.humaine.portal.api.rest.olap.repository.OLAPManualUserGroupRepository;
import com.humaine.portal.api.rest.olap.repository.OLAPUserGroupMappingRepository;
import com.humaine.portal.api.rest.repository.AccountRepository;
import com.humaine.portal.api.rest.repository.AgeGroupRepository;
import com.humaine.portal.api.rest.repository.BigFiveRepository;
import com.humaine.portal.api.rest.repository.BuyingRepository;
import com.humaine.portal.api.rest.repository.EducationRepository;
import com.humaine.portal.api.rest.repository.EthnicityRepository;
import com.humaine.portal.api.rest.repository.FamilySizeRepository;
import com.humaine.portal.api.rest.repository.GenderRepository;
import com.humaine.portal.api.rest.repository.PersuasiveRepository;
import com.humaine.portal.api.rest.repository.UserGroupDeletedRepository;
import com.humaine.portal.api.rest.repository.UserGroupRepository;
import com.humaine.portal.api.rest.repository.ValuesRepository;
import com.humaine.portal.api.rest.service.UserGroupService;
import com.humaine.portal.api.security.config.CognitoJwtAuthentication;
import com.humaine.portal.api.util.ErrorMessageUtils;
import com.humaine.portal.api.util.ErrorStatus;
import com.humaine.portal.api.util.JsonUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserGroupControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserGroupService userGroupService;

	@MockBean
	private GenderRepository genderRepository;

	@MockBean
	private AgeGroupRepository ageGroupRepository;

	@MockBean
	private EthnicityRepository ethnicityRepository;

	@MockBean
	private FamilySizeRepository familySizeRepository;

	@MockBean
	private BigFiveRepository bigFiveRepository;

	@MockBean
	private EducationRepository educationRepository;

	@MockBean
	private UserGroupRepository userGroupRepository;

	@MockBean
	private UserGroupDeletedRepository userGroupDeletedRepository;

	@MockBean
	private ValuesRepository valuesRepository;

	@MockBean
	private PersuasiveRepository persuasiveRepository;

	@MockBean
	private BuyingRepository buyingRepository;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	private final String attributeEmptyValue = "N/A";

	@Mock
	private SecurityContext securityContext;

	@MockBean
	private AccountRepository accountRepository;

	@MockBean
	private OLAPUserGroupMappingRepository userGroupMappingRepository;

	@MockBean
	private OLAPManualUserGroupRepository manualUserGroupRepository;

	@Before
	public void setUp() {
		User user = new User(AuthenticationMockData.account.getEmail(), "", new HashSet<GrantedAuthority>());
		Authentication auth = new CognitoJwtAuthentication(user, null, user.getAuthorities());
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(securityContext.getAuthentication()).thenReturn(auth);

		when(accountRepository.findAccountByEmail(Mockito.anyString())).thenReturn(AuthenticationMockData.account);

		when(genderRepository.findAll()).thenReturn(new Iterable<Gender>() {

			@Override
			public Iterator<Gender> iterator() {
				return UserGroupMockData.genders.iterator();
			}
		});

		when(ageGroupRepository.findAll()).thenReturn(new Iterable<AgeGroup>() {

			@Override
			public Iterator<AgeGroup> iterator() {
				return UserGroupMockData.ageGroups.iterator();
			}
		});

		when(familySizeRepository.findAll()).thenReturn(new Iterable<FamilySize>() {

			@Override
			public Iterator<FamilySize> iterator() {
				return UserGroupMockData.familySizes.iterator();
			}
		});

		when(ethnicityRepository.findAll()).thenReturn(new Iterable<Ethnicity>() {

			@Override
			public Iterator<Ethnicity> iterator() {
				return UserGroupMockData.ethnicity.iterator();
			}
		});

		when(ethnicityRepository.getEthnicityList(Mockito.anyString())).thenAnswer(new Answer<List<Ethnicity>>() {

			@Override
			public List<Ethnicity> answer(InvocationOnMock invocation) throws Throwable {
				return UserGroupMockData.ethnicity.stream()
						.filter(e -> !attributeEmptyValue.equalsIgnoreCase(e.getValue())).collect(Collectors.toList());
			}
		});

		when(ethnicityRepository.getEthnicityEmptyValue(Mockito.anyString())).thenAnswer(new Answer<Ethnicity>() {

			@Override
			public Ethnicity answer(InvocationOnMock invocation) throws Throwable {
				List<Ethnicity> ethnicityList = UserGroupMockData.ethnicity.stream()
						.filter(e -> attributeEmptyValue.equalsIgnoreCase(e.getValue())).collect(Collectors.toList());
				if (ethnicityList == null)
					return null;
				return ethnicityList.get(0);
			}
		});

		when(genderRepository.getGenderList(Mockito.anyString())).thenAnswer(new Answer<List<Gender>>() {

			@Override
			public List<Gender> answer(InvocationOnMock invocation) throws Throwable {
				return UserGroupMockData.genders.stream()
						.filter(e -> !attributeEmptyValue.equalsIgnoreCase(e.getValue())).collect(Collectors.toList());
			}
		});

		when(genderRepository.getGenderEmptyValue(Mockito.anyString())).thenAnswer(new Answer<Gender>() {

			@Override
			public Gender answer(InvocationOnMock invocation) throws Throwable {
				List<Gender> genderList = UserGroupMockData.genders.stream()
						.filter(e -> attributeEmptyValue.equalsIgnoreCase(e.getValue())).collect(Collectors.toList());
				if (genderList == null)
					return null;
				return genderList.get(0);
			}
		});

		when(bigFiveRepository.findAll()).thenReturn(new Iterable<BigFive>() {

			@Override
			public Iterator<BigFive> iterator() {
				return UserGroupMockData.bigFive.iterator();
			}
		});

		when(educationRepository.findAll()).thenReturn(new Iterable<Education>() {

			@Override
			public Iterator<Education> iterator() {
				return UserGroupMockData.educations.iterator();
			}
		});

		when(buyingRepository.getAttributeByName(Mockito.anyString())).thenAnswer(new Answer<Buying>() {

			@Override
			public Buying answer(InvocationOnMock invocation) throws Throwable {
				List<Buying> buyingFilter = UserGroupMockData.buying.stream()
						.filter(e -> attributeEmptyValue.equals(e.getValue())).collect(Collectors.toList());
				if (buyingFilter.size() == 0)
					return null;
				return buyingFilter.get(0);
			}
		});

		when(bigFiveRepository.findById(Mockito.any(Long.class))).thenAnswer(new Answer<Optional<BigFive>>() {

			@Override
			public Optional<BigFive> answer(InvocationOnMock invocation) throws Throwable {
				List<BigFive> bigFiveList = UserGroupMockData.bigFive.stream()
						.filter(e -> e.getId().equals(invocation.getArgument(0))).collect(Collectors.toList());
				if (bigFiveList.isEmpty())
					return Optional.empty();
				return Optional.of(bigFiveList.get(0));
			}
		});

		when(userGroupRepository.findGroupByAccountIdAndGroupId(Mockito.any(Long.class), Mockito.any(Long.class)))
				.thenAnswer(new Answer<UserGroup>() {
					@Override
					public UserGroup answer(InvocationOnMock invocation) throws Throwable {
						List<UserGroup> filterGroup = UserGroupMockData.groups.stream().filter(g -> {
							return g.getId().equals(invocation.getArgument(1))
									&& g.getAccount().getId().equals(invocation.getArgument(0));
						}).collect(Collectors.toList());
						if (filterGroup.isEmpty()) {
							return null;
						}
						return filterGroup.get(0);
					}
				});
		when(userGroupRepository.groupListByAccount(Mockito.any(Long.class))).thenAnswer(new Answer<List<UserGroup>>() {

			@Override
			public List<UserGroup> answer(InvocationOnMock invocation) throws Throwable {
				return UserGroupMockData.groups.stream()
						.filter(g -> g.getAccount().getId().equals(invocation.getArgument(0)))
						.collect(Collectors.toList());
			}
		});
		when(userGroupDeletedRepository.save(Mockito.any(UserGroupDeleted.class)))
				.thenAnswer(new Answer<UserGroupDeleted>() {

					@Override
					public UserGroupDeleted answer(InvocationOnMock invocation) throws Throwable {
						UserGroupDeleted delete = invocation.getArgument(0);
						return delete;
					}

				});
		when(userGroupRepository.findGroupByAccountIdAndName(Mockito.any(Long.class), Mockito.any(Long.class),
				Mockito.any(String.class))).thenAnswer(new Answer<UserGroup>() {

					@Override
					public UserGroup answer(InvocationOnMock invocation) throws Throwable {

						List<UserGroup> userGroups = UserGroupMockData.groups.stream().filter(g -> {
							return g.getAccount().getId().equals(invocation.getArgument(0))
									&& !g.getId().equals(invocation.getArgument(1))
									&& g.getName().equalsIgnoreCase(invocation.getArgument(2));
						}).collect(Collectors.toList());

						if (userGroups.isEmpty())
							return null;

						return userGroups.get(0);
					}
				});
		when(userGroupRepository.findGroupByAccountIdAndName(Mockito.any(Long.class), Mockito.anyString()))
				.thenAnswer(new Answer<UserGroup>() {

					@Override
					public UserGroup answer(InvocationOnMock invocation) throws Throwable {
						List<UserGroup> userGroups = UserGroupMockData.groups.stream().filter(g -> {
							return g.getAccount().getId().equals(invocation.getArgument(0))
									&& g.getName().equalsIgnoreCase(invocation.getArgument(1));
						}).collect(Collectors.toList());
						if (userGroups.isEmpty())
							return null;
						return userGroups.get(0);
					}
				});

		when(userGroupRepository.save(Mockito.any(UserGroup.class))).thenAnswer(new Answer<UserGroup>() {

			@Override
			public UserGroup answer(InvocationOnMock invocation) throws Throwable {
				UserGroup result = invocation.getArgument(0);
				return result;
			}
		});
		when(genderRepository.findById(Mockito.any(Long.class))).thenAnswer(new Answer<Optional<Gender>>() {

			@Override
			public Optional<Gender> answer(InvocationOnMock invocation) throws Throwable {
				List<Gender> genderList = UserGroupMockData.genders.stream().filter(g -> {
					return g.getId().equals(invocation.getArgument(0));
				}).collect(Collectors.toList());
				if (genderList.isEmpty())
					return Optional.empty();
				return Optional.of(genderList.get(0));
			}
		});

		when(ageGroupRepository.findById(Mockito.any(Long.class))).thenAnswer(new Answer<Optional<AgeGroup>>() {

			@Override
			public Optional<AgeGroup> answer(InvocationOnMock invocation) throws Throwable {
				List<AgeGroup> list = UserGroupMockData.ageGroups.stream().filter(g -> {
					return g.getId().equals(invocation.getArgument(0));
				}).collect(Collectors.toList());
				if (list.isEmpty())
					return Optional.empty();
				return Optional.of(list.get(0));
			}
		});
		when(ethnicityRepository.findById(Mockito.any(Long.class))).thenAnswer(new Answer<Optional<Ethnicity>>() {

			@Override
			public Optional<Ethnicity> answer(InvocationOnMock invocation) throws Throwable {
				List<Ethnicity> list = UserGroupMockData.ethnicity.stream().filter(g -> {
					return g.getId().equals(invocation.getArgument(0));
				}).collect(Collectors.toList());
				if (list.isEmpty())
					return Optional.empty();
				return Optional.of(list.get(0));
			}
		});

		when(familySizeRepository.findById(Mockito.any(Long.class))).thenAnswer(new Answer<Optional<FamilySize>>() {

			@Override
			public Optional<FamilySize> answer(InvocationOnMock invocation) throws Throwable {
				List<FamilySize> list = UserGroupMockData.familySizes.stream().filter(g -> {
					return g.getId().equals(invocation.getArgument(0));
				}).collect(Collectors.toList());
				if (list.isEmpty())
					return Optional.empty();
				return Optional.of(list.get(0));
			}
		});

		when(bigFiveRepository.findById(Mockito.any(Long.class))).thenAnswer(new Answer<Optional<BigFive>>() {

			@Override
			public Optional<BigFive> answer(InvocationOnMock invocation) throws Throwable {
				List<BigFive> list = UserGroupMockData.bigFive.stream().filter(g -> {
					return g.getId().equals(invocation.getArgument(0));
				}).collect(Collectors.toList());
				if (list.isEmpty())
					return Optional.empty();
				return Optional.of(list.get(0));
			}
		});

		when(valuesRepository.findById(Mockito.any(Long.class))).thenAnswer(new Answer<Optional<Values>>() {

			@Override
			public Optional<Values> answer(InvocationOnMock invocation) throws Throwable {
				List<Values> list = UserGroupMockData.values.stream().filter(g -> {
					return g.getId().equals(invocation.getArgument(0));
				}).collect(Collectors.toList());
				if (list.isEmpty())
					return Optional.empty();
				return Optional.of(list.get(0));
			}
		});

		when(persuasiveRepository.findById(Mockito.any(Long.class))).thenAnswer(new Answer<Optional<Persuasive>>() {

			@Override
			public Optional<Persuasive> answer(InvocationOnMock invocation) throws Throwable {
				List<Persuasive> list = UserGroupMockData.persuasive.stream().filter(g -> {
					return g.getId().equals(invocation.getArgument(0));
				}).collect(Collectors.toList());
				if (list.isEmpty())
					return Optional.empty();
				return Optional.of(list.get(0));
			}
		});

		when(buyingRepository.findById(Mockito.any(Long.class))).thenAnswer(new Answer<Optional<Buying>>() {

			@Override
			public Optional<Buying> answer(InvocationOnMock invocation) throws Throwable {
				List<Buying> list = UserGroupMockData.buying.stream().filter(g -> {
					return g.getId().equals(invocation.getArgument(0));
				}).collect(Collectors.toList());
				if (list.isEmpty())
					return Optional.empty();
				return Optional.of(list.get(0));
			}
		});

		when(educationRepository.findById(Mockito.any(Long.class))).thenAnswer(new Answer<Optional<Education>>() {

			@Override
			public Optional<Education> answer(InvocationOnMock invocation) throws Throwable {
				List<Education> list = UserGroupMockData.educations.stream().filter(g -> {
					return g.getId().equals(invocation.getArgument(0));
				}).collect(Collectors.toList());
				if (list.isEmpty())
					return Optional.empty();
				return Optional.of(list.get(0));
			}
		});

		when(manualUserGroupRepository.getGroupListByAccountAndIds(Mockito.any(Long.class), Mockito.any(List.class)))
				.then(new Answer<List<OLAPManualUserGroup>>() {

					@Override
					public List<OLAPManualUserGroup> answer(InvocationOnMock invocation) throws Throwable {
						List<Long> ids = invocation.getArgument(1);
						return UserGroupMockData.manualUserGroups.stream().filter(
								e -> e.getAccount().equals(invocation.getArgument(0)) && ids.contains(e.getGroupId()))
								.collect(Collectors.toList());
					}
				});

		when(manualUserGroupRepository.getGroupsRankByIds(Mockito.any(Long.class), Mockito.any(List.class)))
				.then(new Answer<List<ManualUserGroupRank>>() {

					@Override
					public List<ManualUserGroupRank> answer(InvocationOnMock invocation) throws Throwable {
						List<Long> ids = invocation.getArgument(1);
						List<OLAPManualUserGroup> data = UserGroupMockData.manualUserGroups.stream()
								.filter(e -> e.getAccount().equals(invocation.getArgument(0))
										&& ids.contains(e.getGroupId()) && e.getSuccessMatch() != null
										&& e.getSuccessMatch() > 0)
								.collect(Collectors.toList());
						data.sort(new Comparator<OLAPManualUserGroup>() {

							@Override
							public int compare(OLAPManualUserGroup arg0, OLAPManualUserGroup arg1) {
								if (arg1.getSuccessMatch() < arg0.getSuccessMatch())
									return -1;
								if (arg0.getSuccessMatch() < arg1.getSuccessMatch())
									return 1;
								return 0;
							}
						});
						List<ManualUserGroupRankObject> filterData = data.stream()
								.map(e -> new ManualUserGroupRankObject(e)).collect(Collectors.toList());
						for (int i = 0; i < filterData.size(); i++) {
							filterData.get(i).rank = Long.parseLong(String.valueOf(i + 1));
						}
						return filterData.stream().map(e -> e).collect(Collectors.toList());
					}
				});
		when(manualUserGroupRepository.getGroupRankByIds(Mockito.any(Long.class), Mockito.any(List.class),
				Mockito.any(Long.class))).then(new Answer<ManualUserGroupRank>() {

					@Override
					public ManualUserGroupRank answer(InvocationOnMock invocation) throws Throwable {
						List<Long> ids = invocation.getArgument(1);
						List<OLAPManualUserGroup> data = UserGroupMockData.manualUserGroups.stream()
								.filter(e -> e.getAccount().equals(invocation.getArgument(0))
										&& ids.contains(e.getGroupId()) && e.getSuccessMatch() != null
										&& e.getSuccessMatch() > 0)
								.collect(Collectors.toList());
						data.sort(new Comparator<OLAPManualUserGroup>() {
							@Override
							public int compare(OLAPManualUserGroup arg0, OLAPManualUserGroup arg1) {
								if (arg1.getSuccessMatch() < arg0.getSuccessMatch())
									return -1;
								if (arg0.getSuccessMatch() < arg1.getSuccessMatch())
									return 1;
								return 0;
							}
						});

						List<ManualUserGroupRankObject> filterData = data.stream()
								.map(e -> new ManualUserGroupRankObject(e)).collect(Collectors.toList());
						for (int i = 0; i < filterData.size(); i++) {
							filterData.get(i).rank = Long.parseLong(String.valueOf(i + 1));
						}
						filterData = filterData.stream().map(e -> e).collect(Collectors.toList());
						List<ManualUserGroupRankObject> rank = filterData.stream().filter(e -> {
							return e.getGroupId().equals(invocation.getArgument(2));
						}).collect(Collectors.toList());
						if (rank.isEmpty())
							return null;
						return rank.get(0);
					}
				});

		when(userGroupRepository.groupIdListByAccount(Mockito.anyLong())).then(new Answer<List<Long>>() {
			@Override
			public List<Long> answer(InvocationOnMock invocation) throws Throwable {
				List<Long> ids = UserGroupMockData.groups.stream()
						.filter(e -> e.getAccount().getId().equals(invocation.getArgument(0))).map(e -> e.getId())
						.collect(Collectors.toList());
				return ids;
			}
		});

		when(userGroupMappingRepository.getUserCount(Mockito.anyLong(), Mockito.anyString(),
				Mockito.any(Integer.class))).then(new Answer<Long>() {
					@Override
					public Long answer(InvocationOnMock invocation) throws Throwable {
						List<OLAPUserGroupMapping> filterData = UserGroupMockData.groupMapping.stream().filter(e -> {
							return e.getAccount().equals(invocation.getArgument(0))
									&& e.getUserGroupId().equals(invocation.getArgument(1))
									&& e.getFlag().equals(invocation.getArgument(2));
						}).collect(Collectors.toList());
						return Long.parseLong(String.valueOf(filterData.size()));
					}
				});
		Mockito.doNothing().when(userGroupRepository).delete(Mockito.any(UserGroup.class));
	}

	/**
	 * 
	 * Test Get Demographic Attributes
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetDemographicAttributes() throws Exception {
		final String URL = "/demographic";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
	}

	/**
	 * 
	 * Test Get Cognitive Attributes
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCognitiveAttribute() throws Exception {

		final String URL = "/cognitive/2";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
	}

	/**
	 * 
	 * Test Get Cognitive Attributes: For Unknown BigFive Id
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCognitiveAttribute_For_Unknown_BigFive() throws Exception {

		Long id = 10L;
		final String URL = "/cognitive/" + id;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.bigfive.not.found", new Object[] { id }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.bigfive.not.found.code"))))
				.andReturn();
	}

	/**
	 * 
	 * Test Get Cognitive Attributes: Response should return Attributes that have
	 * N/A (if present) in case no attributes Mapped to BigFive value
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCognitiveAttribute_NA_Values_For_Empty_BigFive_Mapping() throws Exception {

		Long id = 1L;
		final String URL = "/cognitive/" + id;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value())))
				.andExpect(jsonPath("$.responseData.motivationToBuy", is(Matchers.empty())))
				.andExpect(jsonPath("$.responseData.motivationToBuyEmpty.value", is(attributeEmptyValue))).andReturn();
	}

	/**
	 * 
	 * Test Get UserGroup By Id for login Account
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetUserGroup() throws Exception {
		Long groupId = 1L;
		final String URL = "/usergroups/" + groupId;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
	}

	/**
	 * 
	 * Test Get Group By Id: which is not exist in database.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetUserGroup_With_Unknow_GroupID() throws Exception {
		Long groupId = 5L;
		final String URL = "/usergroups/" + groupId;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found",
								new Object[] { groupId }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found.code"))))
				.andReturn();
	}

	/**
	 * 
	 * Test Get Group of Other Account By Id
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetUserGroup_Of_Other_Account() throws Exception {
		Long groupId = 2L;
		final String URL = "/usergroups/" + groupId;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found",
								new Object[] { groupId }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found.code"))))
				.andReturn();
	}

	/**
	 * 
	 * Test Get User GroupList
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetUserGroup_List() throws Exception {
		final String URL = "/usergroups";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();

		List<UserGroupResponse> details = JsonUtils.parseAndGetResponseData(result.getResponse().getContentAsString(),
				UserGroupResponse.class);
	}

	/**
	 * 
	 * Test Delete UserGroup
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeleteUserGroup() throws Exception {
		Long id = 1L;
		final String URL = "/usergroup/" + id;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
	}

	/**
	 * 
	 * Test Delete UserGroup that does not exist in database
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeleteUserGroup_that_not_exist_in_database() throws Exception {
		Long id = 10L;
		final String URL = "/usergroup/" + id;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found", new Object[] { id }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found.code"))))
				.andReturn();
	}

	/**
	 * 
	 * Test Delete UserGroup of Other Account
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeleteUserGroup_of_other_Account() throws Exception {
		Long id = 2L;
		final String URL = "/usergroup/" + id;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found", new Object[] { id }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found.code"))))
				.andReturn();
	}

	/**
	 * 
	 * Test Create User Group
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCreateUserGroup() throws Exception {
		UserGroupRequest request = UserGroupMockData.getRequest();
		final String URL = "/usergroup";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
	}

	/**
	 * 
	 * Test Create User Group: Without passing required fields
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCreateUserGroup_without_passing_required_fields() throws Exception {
		UserGroupRequest request = UserGroupMockData.getRequest();
		request.setAgeGroup(null);
		request.setBigFive(null);
		request.setFamilySize(null);
		final String URL = "/usergroup";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value()))).andReturn();
	}

	/**
	 * 
	 * Test Create User Group: For Irrelevant bigFive Mapping Attribute
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCreateUserGroup_with_irrelevant_bigfive_mapping_attribute() throws Exception {
		UserGroupRequest request = UserGroupMockData.getRequest();
		request.setPersuasiveStratergies(UserGroupMockData.persuasive.get(0).getId());
		final String URL = "/usergroup";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.persuasive-stratergies.not.found",
								new Object[] { request.getPersuasiveStratergies() }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.persuasive-stratergies.not.found.code"))))
				.andReturn();
	}

	/**
	 * 
	 * Test Create User Group: For bigFive Mapping Attribute with NA Value
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCreateUserGroup_with_NA_bigfive_mapping_attribute() throws Exception {
		UserGroupRequest request = UserGroupMockData.getRequest();
		request.setMotivationToBuy(UserGroupMockData.buying.get(1).getId());
		final String URL = "/usergroup";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.buying.not.found",
								new Object[] { request.getPersuasiveStratergies() }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.buying.not.found.code"))))
				.andReturn();
	}

	/**
	 * 
	 * Test Create User Group: With Duplicate GroupName for same Login Account
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCreateUserGroup_with_Dupplicate_GroupName_In_Same_Account() throws Exception {
		UserGroupRequest request = UserGroupMockData.getRequest();
		request.setName(UserGroupMockData.groups.get(0).getName());
		final String URL = "/usergroup";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.user-group.already.exist",
								new Object[] { request.getName() }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.user-group.already.exist.code"))))
				.andReturn();
	}

	/**
	 * 
	 * Test Create User Group: With Duplicate GroupName for different Account
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCreateUserGroup_with_Dupplicate_GroupName_In_different_Account() throws Exception {
		UserGroupRequest request = UserGroupMockData.getRequest();
		request.setName(UserGroupMockData.groups.get(1).getName());
		final String URL = "/usergroup";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
	}

	/**
	 * 
	 * Update Manual User Group Name
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateUserGroupName() throws Exception {
		UserGroupRequest userRequest = UserGroupMockData.getRequest();
		UserGroupUpdateRequest request = new UserGroupUpdateRequest();
		request.setName(userRequest.getName() + "test");
		final String URL = "/usergroup/" + UserGroupMockData.groups.get(0).getId();
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
	}

	/**
	 * 
	 * Update Manual User Group Name of different Account
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateUserGroupName_of_different_Account() throws Exception {
		UserGroupRequest userRequest = UserGroupMockData.getRequest();
		UserGroupUpdateRequest request = new UserGroupUpdateRequest();
		request.setName(userRequest.getName() + "test");
		final String URL = "/usergroup/" + UserGroupMockData.groups.get(1).getId();
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found",
								new Object[] { UserGroupMockData.groups.get(1).getId() }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found.code"))))
				.andReturn();
	}

	/**
	 * 
	 * Update Manual User Group Name That is Already Exist
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateUserGroupName_with_GroupName_that_alreay_exist() throws Exception {
		UserGroupUpdateRequest request = new UserGroupUpdateRequest();
		request.setName(UserGroupMockData.groups.get(2).getName());
		final String URL = "/usergroup/" + UserGroupMockData.groups.get(0).getId();
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.user-group.already.exist",
								new Object[] { UserGroupMockData.groups.get(2).getName() }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.user-group.already.exist.code"))))
				.andReturn();
	}

	/**
	 * 
	 * Update Manual User Group Name without changing group name
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateUserGroupName_without_changing_groupname() throws Exception {
		UserGroupRequest userRequest = UserGroupMockData.getRequest();
		UserGroupUpdateRequest request = new UserGroupUpdateRequest();
		request.setName(UserGroupMockData.groups.get(0).getName());
		final String URL = "/usergroup/" + UserGroupMockData.groups.get(0).getId();
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
	}

	/**
	 * 
	 * Test get Persona Details
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_get_Persona_details() throws Exception {

		final String URL = "/usergroups/persona/" + UserGroupMockData.groups.get(0).getId();
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
		Persona persona = JsonUtils.parseResponse(result.getResponse().getContentAsString(), Persona.class);
		assertEquals(2L, persona.getGroup().getRank());
	}

	/**
	 * 
	 * Test get Persona Details of Different Account
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_get_Persona_details_of_different_Account() throws Exception {

		final String URL = "/usergroups/persona/" + UserGroupMockData.groups.get(1).getId();
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found",
								new Object[] { UserGroupMockData.groups.get(1).getId() }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found.code"))))
				.andReturn();
	}

	/**
	 * 
	 * Test get Persona Details of for Group wihtout Success Match
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_get_Persona_details_for_group_without_success_match() throws Exception {

		final String URL = "/usergroups/persona/" + UserGroupMockData.groups.get(3).getId();
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
		Persona persona = JsonUtils.parseResponse(result.getResponse().getContentAsString(), Persona.class);
		assertEquals(null, persona.getGroup().getRank());
	}

}

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
class ManualUserGroupRankObject implements ManualUserGroupRank {

	Long groupId;

	Long rank;

	Float successMatch;

	Long noOfUsers;
	
	public ManualUserGroupRankObject(OLAPManualUserGroup group) {
		this.groupId = group.getGroupId();
		this.successMatch = group.getSuccessMatch();
		this.noOfUsers = group.getNoOfUsers();
	}
}
