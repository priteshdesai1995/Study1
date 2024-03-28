package com.humaine.portal.api.rest.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

import com.humaine.portal.api.enums.JourneyElementMasterIds;
import com.humaine.portal.api.model.JourneyElementMaster;
import com.humaine.portal.api.model.TestJourney;
import com.humaine.portal.api.model.TestJourneyDeleted;
import com.humaine.portal.api.model.UserGroup;
import com.humaine.portal.api.olap.model.OLAPUserGroup;
import com.humaine.portal.api.request.dto.CreateTestJourneyRequest;
import com.humaine.portal.api.response.dto.JournyResponse;
import com.humaine.portal.api.response.dto.ShortGroupDetails;
import com.humaine.portal.api.response.dto.TestJourneyResponse;
import com.humaine.portal.api.rest.controller.mock.data.AuthenticationMockData;
import com.humaine.portal.api.rest.controller.mock.data.JournyMockData;
import com.humaine.portal.api.rest.controller.mock.data.UserGroupMockData;
import com.humaine.portal.api.rest.repository.AccountRepository;
import com.humaine.portal.api.rest.repository.JourneyElementMasterRepository;
import com.humaine.portal.api.rest.repository.TestJourneyDeletedRepository;
import com.humaine.portal.api.rest.repository.TestJourneyRepository;
import com.humaine.portal.api.rest.repository.UserGroupRepository;
import com.humaine.portal.api.rest.service.UserGroupService;
import com.humaine.portal.api.security.config.CognitoJwtAuthentication;
import com.humaine.portal.api.util.ErrorMessageUtils;
import com.humaine.portal.api.util.ErrorStatus;
import com.humaine.portal.api.util.JsonUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class JourneyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Mock
	private SecurityContext securityContext;

	@Autowired
	private UserGroupService userGroupService;

	@MockBean
	private AccountRepository accountRepository;

	@MockBean
	private UserGroupRepository userGroupRepository;

	@MockBean
	private JourneyElementMasterRepository journeyElementMasterRepository;

	@MockBean
	private TestJourneyRepository testJourneyRepository;

	@MockBean
	private TestJourneyDeletedRepository testJourneyDeletedRepository;

	@Before
	public void setUp() {
		User user = new User(AuthenticationMockData.account.getEmail(), "", new HashSet<GrantedAuthority>());
		Authentication auth = new CognitoJwtAuthentication(user, null, user.getAuthorities());
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(securityContext.getAuthentication()).thenReturn(auth);

		when(accountRepository.findAccountByEmail(Mockito.anyString())).thenReturn(AuthenticationMockData.account);

		when(journeyElementMasterRepository.findAll()).thenReturn(new Iterable<JourneyElementMaster>() {

			@Override
			public Iterator<JourneyElementMaster> iterator() {
				return JournyMockData.elementMaster.iterator();
			}
		});

		when(userGroupRepository.groupShortDetailsListByAccount(Mockito.anyLong()))
				.thenAnswer(new Answer<List<ShortGroupDetails>>() {
					@Override
					public List<ShortGroupDetails> answer(InvocationOnMock invocation) throws Throwable {
						return JournyMockData.groups.stream()
								.filter(e -> e.getAccount().getId().equals(invocation.getArgument(0)))
								.map(e -> new ShortGroupDetails(e.getId(), e.getName())).collect(Collectors.toList());
					}
				});

		when(testJourneyRepository.getJourneyList(Mockito.anyLong())).thenAnswer(new Answer<List<JournyResponse>>() {

			@Override
			public List<JournyResponse> answer(InvocationOnMock invocation) throws Throwable {

				Map<Long, UserGroup> groupMap = JournyMockData.groups.stream()
						.collect(Collectors.toMap(UserGroup::getId, Function.identity()));
				return JournyMockData.journeys.stream()
						.filter(e -> e.getAccount().getId().equals(invocation.getArgument(0)))
						.map(e -> new JournyResponse(e, groupMap.get(e.getGroupId()).getName(),
								groupMap.get(e.getGroupId()).getBigFive().getValue()))
						.collect(Collectors.toList());
			}
		});

		when(testJourneyRepository.getJourneyById(Mockito.anyLong(), Mockito.anyLong()))
				.thenAnswer(new Answer<TestJourney>() {

					@Override
					public TestJourney answer(InvocationOnMock invocation) throws Throwable {
						List<TestJourney> filterJourneys = JournyMockData.journeys.stream()
								.filter(e -> e.getAccount().getId().equals(invocation.getArgument(0))
										&& e.getId().equals(invocation.getArgument(1)))
								.collect(Collectors.toList());
						if (filterJourneys.isEmpty())
							return null;
						return filterJourneys.get(0);
					}
				});

		when(testJourneyRepository.getJourneyFullDetailsById(Mockito.anyLong(), Mockito.anyLong()))
				.thenAnswer(new Answer<JournyResponse>() {
					@Override
					public JournyResponse answer(InvocationOnMock invocation) throws Throwable {
						Map<Long, UserGroup> groupMap = JournyMockData.groups.stream()
								.collect(Collectors.toMap(UserGroup::getId, Function.identity()));
						List<TestJourney> filterJourneys = JournyMockData.journeys.stream()
								.filter(e -> e.getAccount().getId().equals(invocation.getArgument(0))
										&& e.getId().equals(invocation.getArgument(1)))
								.collect(Collectors.toList());
						if (filterJourneys.isEmpty())
							return null;

						return new JournyResponse(filterJourneys.get(0),
								groupMap.get(filterJourneys.get(0).getGroupId()).getName(),
								groupMap.get(filterJourneys.get(0).getGroupId()).getBigFive().getValue());
					}
				});
		when(journeyElementMasterRepository.count())
				.thenReturn(Long.parseLong(String.valueOf(JournyMockData.elementMaster.size())));

		when(testJourneyRepository.save(Mockito.any(TestJourney.class))).thenAnswer(new Answer<TestJourney>() {

			@Override
			public TestJourney answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArgument(0);
			}
		});

		when(testJourneyDeletedRepository.save(Mockito.any(TestJourneyDeleted.class)))
				.thenAnswer(new Answer<TestJourneyDeleted>() {

					@Override
					public TestJourneyDeleted answer(InvocationOnMock invocation) throws Throwable {
						return invocation.getArgument(0);
					}
				});

		Mockito.doNothing().when(testJourneyRepository).delete(Mockito.any(TestJourney.class));

		when(userGroupRepository.findGroupByAccountIdAndGroupId(Mockito.any(Long.class), Mockito.any(Long.class)))
				.thenAnswer(new Answer<UserGroup>() {
					@Override
					public UserGroup answer(InvocationOnMock invocation) throws Throwable {
						List<UserGroup> filterGroup = JournyMockData.groups.stream().filter(g -> {
							return g.getId().equals(invocation.getArgument(1))
									&& g.getAccount().getId().equals(invocation.getArgument(0));
						}).collect(Collectors.toList());
						if (filterGroup.isEmpty()) {
							return null;
						}
						return filterGroup.get(0);
					}
				});
	}

	private static final String BASE_URL = "/test-journey";

	/**
	 * Test Journey Element List
	 * 
	 * @throws Exception
	 */
	@Test
	public void testJourneyElementList() throws Exception {

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URL + "/elements")
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1");

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
		List<JourneyElementMaster> elements = new ArrayList<>();
		elements = JsonUtils.parseAndGetResponseData(result.getResponse().getContentAsString(),
				JourneyElementMaster.class);
		assertEquals(JournyMockData.elementMaster.size(), elements.size());
	}

	/**
	 * Test UserGroup List
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupList() throws Exception {

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URL + "/groups")
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1");

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();

		List<ShortGroupDetails> details = JsonUtils.parseAndGetResponseData(result.getResponse().getContentAsString(),
				ShortGroupDetails.class);
		List<Long> ids = details.stream().map(e -> e.getGroupId()).collect(Collectors.toList());
		assertTrue(!ids.contains(JournyMockData.groups.get(1).getId()));
	}

	/**
	 * Test Journey List
	 * 
	 * @throws Exception
	 */
	@Test
	public void testJourneyList() throws Exception {

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();

		List<TestJourneyResponse> list = JsonUtils.parseAndGetResponseData(result.getResponse().getContentAsString(),
				TestJourneyResponse.class);
		Set<Long> ids = list.stream().map(e -> e.getGroupId()).collect(Collectors.toSet());
		assertTrue(!ids.contains(JournyMockData.groups.get(1).getId()));
	}

	/**
	 * Get Journey By Id
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetJourneyById() throws Exception {
		Long id = 1L;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URL + "/" + id)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1");

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
	}

	/**
	 * Get Journey By Id of Different Account
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetJourneyById_of_different_Account() throws Exception {
		Long id = JournyMockData.journeys.get(1).getId();
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URL + "/" + id)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1");

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.journey.not.found", new Object[] { id }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.journey.not.found.code"))))
				.andReturn();
	}

	/**
	 * Get Journey By Id with non exist id
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetJourneyById_with_non_exist_id() throws Exception {
		Long id = 25L;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URL + "/" + id)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1");

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.journey.not.found", new Object[] { id }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.journey.not.found.code"))))
				.andReturn();
	}

	/**
	 * save Test New Journey
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSaveTestNewJourney() throws Exception {
		CreateTestJourneyRequest request = JournyMockData.getRquest();
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
	}

	/**
	 * save Test New Journey with Group Id of Different Account
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSaveTestNewJourney_with_Group_id_of_different_Account() throws Exception {
		CreateTestJourneyRequest request = JournyMockData.getRquest();
		request.setGroupId(JournyMockData.groups.get(1).getId());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found",
								new Object[] { request.getGroupId() }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found.code"))))
				.andReturn();
	}

	/**
	 * save Test New Journey with non Exist Group Id
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSaveTestNewJourney_with_non_exist_groupId() throws Exception {
		CreateTestJourneyRequest request = JournyMockData.getRquest();
		request.setGroupId(10L);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found",
								new Object[] { request.getGroupId() }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found.code"))))
				.andReturn();
	}

	/**
	 * save Test New Journey with improper step value. for example: for
	 * firstInterest: pass ADD_TO_CART value which is for (Purchase: Add to Cart
	 * step)
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSaveTestNewJourney_with_improper_step_value() throws Exception {
		CreateTestJourneyRequest request = JournyMockData.getRquest();
		request.setFirstInterest(JournyMockData.ADD_TO_CART);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.test-journey.element.not.found",
								new Object[] { JournyMockData.ADD_TO_CART, "FIRST_INTEREST" }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.test-journey.element.not.found.code"))))
				.andReturn();
	}

	/**
	 * update Test New Journey
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateTestNewJourney() throws Exception {
		Long id = JournyMockData.journeys.get(0).getId();
		CreateTestJourneyRequest request = JournyMockData.getRquest();
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(BASE_URL + '/' + id)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
	}

	/**
	 * update Test New Journey with Group Id of Different Account
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateTestNewJourney_with_Group_id_of_different_Account() throws Exception {
		Long id = JournyMockData.journeys.get(0).getId();
		CreateTestJourneyRequest request = JournyMockData.getRquest();
		request.setGroupId(JournyMockData.groups.get(1).getId());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(BASE_URL + '/' + id)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found",
								new Object[] { request.getGroupId() }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found.code"))))
				.andReturn();
	}

	/**
	 * update Test New Journey with JourneyId of different Account
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateTestNewJourney_with_Journey_id_of_different_Account() throws Exception {
		Long id = JournyMockData.journeys.get(1).getId();
		CreateTestJourneyRequest request = JournyMockData.getRquest();
		request.setGroupId(JournyMockData.groups.get(1).getId());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(BASE_URL + '/' + id)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.journey.not.found", new Object[] { id }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.journey.not.found.code"))))
				.andReturn();
	}

	/**
	 * Update Test New Journey with non Exist Journey Id
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateTestNewJourney_with_Non_Exit_JourneyId() throws Exception {
		Long id = 19L;
		CreateTestJourneyRequest request = JournyMockData.getRquest();
		request.setGroupId(JournyMockData.groups.get(1).getId());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(BASE_URL + '/' + id)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.journey.not.found", new Object[] { id }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.journey.not.found.code"))))
				.andReturn();
	}

	/**
	 * Update Test New Journey with non Exist Group Id
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateTestNewJourney_with_non_exist_groupId() throws Exception {
		Long id = JournyMockData.journeys.get(0).getId();
		CreateTestJourneyRequest request = JournyMockData.getRquest();
		request.setGroupId(10L);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found",
								new Object[] { request.getGroupId() }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found.code"))))
				.andReturn();
	}

	/**
	 * Update Test New Journey with improper step value. for example: for
	 * firstInterest: pass ADD_TO_CART value which is for (Purchase: Add to Cart
	 * step)
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateTestNewJourney_with_improper_step_value() throws Exception {
		Long id = JournyMockData.journeys.get(0).getId();
		CreateTestJourneyRequest request = JournyMockData.getRquest();
		request.setFirstInterest(JournyMockData.ADD_TO_CART);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(BASE_URL + '/' + id)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.test-journey.element.not.found",
								new Object[] { JournyMockData.ADD_TO_CART, "FIRST_INTEREST" }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.test-journey.element.not.found.code"))))
				.andReturn();
	}

	/**
	 * Delete Test Journey
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeleteTestJourney() throws Exception {
		Long id = JournyMockData.journeys.get(0).getId();
		CreateTestJourneyRequest request = JournyMockData.getRquest();
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(BASE_URL + '/' + id)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
	}

	/**
	 * Delete Test Journey with JourneyId of different Account
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeleteTestNewJourney_with_Journey_id_of_different_Account() throws Exception {
		Long id = JournyMockData.journeys.get(1).getId();
		CreateTestJourneyRequest request = JournyMockData.getRquest();
		request.setGroupId(JournyMockData.groups.get(1).getId());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(BASE_URL + '/' + id)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.journey.not.found", new Object[] { id }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.journey.not.found.code"))))
				.andReturn();
	}

	/**
	 * Delete Test Journey with non Exist Journey Id
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeleteTestNewJourney_with_Non_Exit_JourneyId() throws Exception {
		Long id = 19L;
		CreateTestJourneyRequest request = JournyMockData.getRquest();
		request.setGroupId(JournyMockData.groups.get(1).getId());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(BASE_URL + '/' + id)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.journey.not.found", new Object[] { id }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.journey.not.found.code"))))
				.andReturn();
	}
}