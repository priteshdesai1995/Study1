package com.humaine.portal.api.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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

import com.humaine.portal.api.model.AIUserGroup;
import com.humaine.portal.api.model.AIUserGroupDeleted;
import com.humaine.portal.api.model.OLAPUserGroupDeleted;
import com.humaine.portal.api.model.UserGroup;
import com.humaine.portal.api.olap.model.OLAPUserGroup;
import com.humaine.portal.api.request.dto.SaveAIUserGroupRequest;
import com.humaine.portal.api.rest.controller.mock.data.AuthenticationMockData;
import com.humaine.portal.api.rest.controller.mock.data.UserGroupMockData;
import com.humaine.portal.api.rest.olap.repository.OLAPUserGroupRepository;
import com.humaine.portal.api.rest.repository.AIUserGroupDeletedRepository;
import com.humaine.portal.api.rest.repository.AIUserGroupRepository;
import com.humaine.portal.api.rest.repository.AccountRepository;
import com.humaine.portal.api.rest.repository.OLAPUserGroupDeletedRepository;
import com.humaine.portal.api.security.config.CognitoJwtAuthentication;
import com.humaine.portal.api.util.DateUtils;
import com.humaine.portal.api.util.ErrorMessageUtils;
import com.humaine.portal.api.util.ErrorStatus;
import com.humaine.portal.api.util.JsonUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AIUserGroupControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	private final String attributeEmptyValue = "N/A";

	@Mock
	private SecurityContext securityContext;

	@MockBean
	private AccountRepository accountRepository;

	@MockBean
	private AIUserGroupRepository aiUserGroupRepository;

	@MockBean
	private OLAPUserGroupRepository userGroupRepository;

	@MockBean
	private AIUserGroupDeletedRepository userGroupDeletedRepository;

	@MockBean
	private OLAPUserGroupDeletedRepository olapUserGroupDeletedRepository;

	private OffsetDateTime timestemp = DateUtils.getCurrentTimestemp();

	private List<OLAPUserGroup> olapUserGroups = UserGroupMockData.olapUserGroups;

	private AIUserGroup getObject(OLAPUserGroup group, Long id) {
		AIUserGroup aiGroup = new AIUserGroup(group);
		aiGroup.setId(id);
		return aiGroup;
	}

	private List<AIUserGroup> aiUserGroups = new ArrayList<>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8995229550259913619L;
		{
			add(getObject(olapUserGroups.get(4), 1L));
			add(getObject(olapUserGroups.get(6), 2L));
		}
	};

	@Before
	public void setUp() {
		User user = new User(AuthenticationMockData.account.getEmail(), "", new HashSet<GrantedAuthority>());
		Authentication auth = new CognitoJwtAuthentication(user, null, user.getAuthorities());
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(securityContext.getAuthentication()).thenReturn(auth);

		when(accountRepository.findAccountByEmail(Mockito.anyString())).thenReturn(AuthenticationMockData.account);
		when(userGroupRepository.groupListByAccount(Mockito.any(Long.class), Mockito.any(Integer.class)))
				.thenAnswer(new Answer<List<OLAPUserGroup>>() {

					@Override
					public List<OLAPUserGroup> answer(InvocationOnMock invocation) throws Throwable {
						return olapUserGroups.stream().filter(e -> e.getAccount().equals(invocation.getArgument(0))
								&& e.getFlag().equals(invocation.getArgument(1))).collect(Collectors.toList());
					}
				});

		when(userGroupRepository.getGroupListByAccount(Mockito.any(Long.class), Mockito.any(List.class),
				Mockito.any(Integer.class))).thenAnswer(new Answer<List<OLAPUserGroup>>() {
					@Override
					public List<OLAPUserGroup> answer(InvocationOnMock invocation) throws Throwable {
						List<Long> ids = invocation.getArgument(1);
						return olapUserGroups.stream()
								.filter(e -> e.getAccount().equals(invocation.getArgument(0))
										&& !ids.contains(e.getGroupId())
										&& e.getFlag().equals(invocation.getArgument(2)))
								.collect(Collectors.toList());
					}
				});

		when(aiUserGroupRepository.getGroupIdsByAccount(Mockito.any(Long.class))).thenAnswer(new Answer<List<Long>>() {

			@Override
			public List<Long> answer(InvocationOnMock invocation) throws Throwable {
				return aiUserGroups.stream().filter(e -> e.getAccount().equals(invocation.getArgument(0)))
						.map(e -> e.getId()).collect(Collectors.toList());
			}
		});

		when(userGroupRepository.getGroupListByIDsAndAccount(Mockito.any(Long.class), Mockito.any(List.class),
				Mockito.any(Integer.class))).thenAnswer(new Answer<List<OLAPUserGroup>>() {

					@Override
					public List<OLAPUserGroup> answer(InvocationOnMock invocation) throws Throwable {
						List<Long> ids = invocation.getArgument(1);

						return olapUserGroups.stream()
								.filter(e -> e.getAccount().equals(invocation.getArgument(0))
										&& ids.contains(e.getGroupId())
										&& e.getFlag().equals(invocation.getArgument(2)))
								.collect(Collectors.toList());
					}
				});

		when(aiUserGroupRepository.findGroupsByAccountAndIds(Mockito.any(Long.class), Mockito.any(List.class)))
				.thenAnswer(new Answer<List<String>>() {

					@Override
					public List<String> answer(InvocationOnMock invocation) throws Throwable {
						List<String> ids = invocation.getArgument(1);

						return aiUserGroups.stream()
								.filter(e -> e.getAccount().equals(invocation.getArgument(0))
										&& ids.contains(e.getUserGroupId()))
								.map(e -> e.getUserGroupId()).collect(Collectors.toList());
					}
				});
		when(aiUserGroupRepository.saveAll(Mockito.any(aiUserGroups.getClass())))
				.thenAnswer(new Answer<List<AIUserGroup>>() {
					@Override
					public List<AIUserGroup> answer(InvocationOnMock invocation) throws Throwable {
						List<AIUserGroup> groups = invocation.getArgument(0);
						return groups;
					}
				});
		when(userGroupDeletedRepository.save(Mockito.any(AIUserGroupDeleted.class)))
				.thenAnswer(new Answer<AIUserGroupDeleted>() {

					@Override
					public AIUserGroupDeleted answer(InvocationOnMock invocation) throws Throwable {
						return invocation.getArgument(0);
					}
				});

		when(olapUserGroupDeletedRepository.save(Mockito.any(OLAPUserGroupDeleted.class)))
				.thenAnswer(new Answer<OLAPUserGroupDeleted>() {

					@Override
					public OLAPUserGroupDeleted answer(InvocationOnMock invocation) throws Throwable {
						return invocation.getArgument(0);
					}
				});
		when(aiUserGroupRepository.findGroupByAccountIdAndGroupId(Mockito.anyLong(), Mockito.anyLong()))
				.thenAnswer(new Answer<AIUserGroup>() {
					@Override
					public AIUserGroup answer(InvocationOnMock invocation) throws Throwable {
						List<AIUserGroup> groups = aiUserGroups.stream()
								.filter(e -> e.getId().equals(invocation.getArgument(1))
										&& e.getAccount().equals(invocation.getArgument(0)))
								.collect(Collectors.toList());
						if (groups.isEmpty())
							return null;
						return groups.get(0);
					}
				});
		when(userGroupRepository.findGroupByAccountIdAndGroupId(Mockito.anyLong(), Mockito.anyString(),
				Mockito.any(Integer.class))).thenAnswer(new Answer<OLAPUserGroup>() {
					@Override
					public OLAPUserGroup answer(InvocationOnMock invocation) throws Throwable {
						System.out.println();
						List<OLAPUserGroup> filterGroups = olapUserGroups.stream()
								.filter(e -> e.getGroupId().equals(invocation.getArgument(1))
										&& e.getAccount().equals(invocation.getArgument(0))
										&& e.getFlag().equals(invocation.getArgument(2)))
								.collect(Collectors.toList());
						System.out.println(filterGroups);
						if (filterGroups.isEmpty())
							return null;
						return filterGroups.get(0);
					}
				});

		Mockito.doNothing().when(userGroupRepository).delete(Mockito.any(OLAPUserGroup.class));
		Mockito.doNothing().when(aiUserGroupRepository).delete(Mockito.any(AIUserGroup.class));
	}

	private final String BASE_URL = "/ai-usergroups";

	/*
	 * Test AI User Group List: should not contains the deleted Group
	 */
	@Test
	public void testAIUserGroupList() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
		List<OLAPUserGroup> groups = new ArrayList<>();
		groups = JsonUtils.parseAndGetResponseData(result.getResponse().getContentAsString(), OLAPUserGroup.class);
		List<String> ids = groups.stream().map(e -> e.getGroupId()).collect(Collectors.toList());
		assertThat(ids).doesNotContain(olapUserGroups.get(6).getGroupId());
		assertThat(ids).doesNotContain(aiUserGroups.get(1).getUserGroupId());
	}

	/*
	 * Test Saved AI User Group List
	 */
	@Test
	public void testSavedAIUserGroupList() throws Exception {
		String URL = BASE_URL + "/saved";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
		List<AIUserGroup> groups = new ArrayList<>();
		groups = JsonUtils.parseAndGetResponseData(result.getResponse().getContentAsString(), AIUserGroup.class);
		List<String> ids = groups.stream().map(e -> e.getId().toString()).collect(Collectors.toList());
		assertThat(ids).doesNotContain(olapUserGroups.get(6).getGroupId());
	}

	/*
	 * Test Save AI User Groups
	 */
	@Test
	public void testSaveAIGeneratedUserGroups() throws Exception {
		String URL = BASE_URL + "/save";
		List<String> groupIds = new ArrayList<>() {
			private static final long serialVersionUID = 1L;
			{
				add(olapUserGroups.get(7).getGroupId());
				add(olapUserGroups.get(5).getGroupId());
			}
		};
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtils.json(new SaveAIUserGroupRequest(groupIds))).header("version", "v1");

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
	}

	/*
	 * Test Save AI User Groups: With Id does not exist in database
	 */
	@Test
	public void testSaveAIGeneratedUserGroups_with_id_does_not_exist_in_db() throws Exception {
		String URL = BASE_URL + "/save";
		List<String> groupIds = new ArrayList<>() {
			private static final long serialVersionUID = 1L;
			{
				add(olapUserGroups.get(4).getGroupId());
				add(olapUserGroups.get(5).getGroupId());
				add("10");
			}
		};
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtils.json(new SaveAIUserGroupRequest(groupIds))).header("version", "v1");

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found",
								new Object[] { Arrays.asList(10L) }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found.code"))))
				.andReturn();
	}

	/*
	 * Test Save AI User Groups: With Ids that are already saved
	 */
	@Test
	public void testSaveAIGeneratedUserGroups_with_id_that_are_already_saved() throws Exception {
		String URL = BASE_URL + "/save";
		List<String> groupIds = new ArrayList<>() {
			private static final long serialVersionUID = 1L;
			{
				add(olapUserGroups.get(0).getGroupId());
			}
		};
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtils.json(new SaveAIUserGroupRequest(groupIds))).header("version", "v1");

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found",
								new Object[] { Arrays.asList(groupIds.get(0)) }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found.code"))))
				.andReturn();
	}

	/*
	 * Test Save AI User Groups: With Id of Different Account
	 */
	@Test
	public void testSaveAIGeneratedUserGroups_with_id_of_different_Account() throws Exception {
		String URL = BASE_URL + "/save";
		List<String> groupIds = new ArrayList<>() {
			private static final long serialVersionUID = 1L;
			{
				add(olapUserGroups.get(1).getGroupId());
			}
		};
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtils.json(new SaveAIUserGroupRequest(groupIds))).header("version", "v1");

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found",
								new Object[] { Arrays.asList(groupIds.get(0)) }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.user-group.not.found.code"))))
				.andReturn();
	}

	/*
	 * Test Delete Saved AI Generated User Group
	 * 
	 */
	@Test
	public void testDeleteSavedAIGeneratedUserGroups() throws Exception {
		Long id = 1L;
		final String URL = BASE_URL + "/saved/" + id;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
	}

	/*
	 * Test Delete Saved AI Generated User Group that does not exist in Database
	 * 
	 */
	@Test
	public void testDeleteSavedAIGeneratedUser_with_unknown_id() throws Exception {
		Long id = 10L;
		final String URL = BASE_URL + "/saved/" + id;
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

	/*
	 * Test Delete Saved AI Generated User Group of Different Account
	 * 
	 */
	@Test
	public void testDeleteSavedAIGeneratedUser_of_different_account() throws Exception {
		Long id = 2L;
		final String URL = BASE_URL + "/saved/" + id;
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

	/*
	 * Test Delete AI Generated User Group
	 * 
	 */
	@Test
	public void testDeleteAIGeneratedUserGroups() throws Exception {
		String id = olapUserGroups.get(4).getGroupId();
		final String URL = BASE_URL + "/" + id;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
	}

	/*
	 * Test Delete Saved AI Generated User Group that does not exist in Database
	 * 
	 */
	@Test
	public void testDeleteAIGeneratedUser_with_unknown_id() throws Exception {
		Long id = 10L;
		final String URL = BASE_URL + "/" + id;
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

	/*
	 * Test Delete Saved AI Generated User Group of Different Account
	 * 
	 */
	@Test
	public void testDeleteAIGeneratedUser_of_different_account() throws Exception {
		Long id = 2L;
		final String URL = BASE_URL + "/" + id;
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
}
