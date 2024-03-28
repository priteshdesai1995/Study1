package com.humaine.portal.api.rest.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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

import com.humaine.portal.api.enums.GroupFlag;
import com.humaine.portal.api.model.BigFive;
import com.humaine.portal.api.model.OLAPUserGroupDeleted;
import com.humaine.portal.api.olap.model.OLAPUserGroup;
import com.humaine.portal.api.response.dto.CollapsedMyUserGroup;
import com.humaine.portal.api.rest.controller.mock.data.AuthenticationMockData;
import com.humaine.portal.api.rest.controller.mock.data.UserGroupMockData;
import com.humaine.portal.api.rest.olap.repository.OLAPUserGroupRepository;
import com.humaine.portal.api.rest.repository.AccountRepository;
import com.humaine.portal.api.rest.repository.BigFiveRepository;
import com.humaine.portal.api.rest.repository.OLAPUserGroupDeletedRepository;
import com.humaine.portal.api.security.config.CognitoJwtAuthentication;
import com.humaine.portal.api.util.DateUtils;
import com.humaine.portal.api.util.ErrorMessageUtils;
import com.humaine.portal.api.util.ErrorStatus;
import com.humaine.portal.api.util.JsonUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class MyUserGroupControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private SecurityContext securityContext;

	@MockBean
	private AccountRepository accountRepository;

	private final String attributeEmptyValue = "N/A";

	@MockBean
	private BigFiveRepository bigFiveRepository;

	private final String controllerURL = "/my-usergroups";

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@MockBean
	private OLAPUserGroupRepository userGroupRepository;

	@MockBean
	private OLAPUserGroupDeletedRepository userGroupDeletedRepository;

	private OffsetDateTime timestemp = DateUtils.getCurrentTimestemp();

	List<OLAPUserGroup> groups = UserGroupMockData.olapUserGroups;

	@Before
	public void setUp() {

		User user = new User(AuthenticationMockData.account.getEmail(), "", new HashSet<GrantedAuthority>());
		Authentication auth = new CognitoJwtAuthentication(user, null, user.getAuthorities());
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(securityContext.getAuthentication()).thenReturn(auth);

		when(accountRepository.findAccountByEmail(Mockito.anyString())).thenReturn(AuthenticationMockData.account);

		when(userGroupRepository.findGroupByAccountIdAndGroupId(Mockito.any(Long.class), Mockito.any(String.class),
				Mockito.any(Integer.class))).thenAnswer(new Answer<OLAPUserGroup>() {

					@Override
					public OLAPUserGroup answer(InvocationOnMock invocation) throws Throwable {
						List<OLAPUserGroup> filterGroup = groups.stream().filter(g -> {
							return invocation.getArgument(1).equals(g.getGroupId())
									&& invocation.getArgument(0).equals(g.getAccount());
						}).collect(Collectors.toList());
						if (filterGroup.isEmpty())
							return null;
						return filterGroup.get(0);
					}
				});
		when(userGroupRepository.groupListByAccount(Mockito.any(Long.class), Mockito.any(Integer.class)))
				.thenAnswer(new Answer<List<OLAPUserGroup>>() {

					@Override
					public List<OLAPUserGroup> answer(InvocationOnMock invocation) throws Throwable {
						return groups.stream().filter(g -> g.getAccount().equals(invocation.getArgument(0))
								&& g.getFlag().equals(invocation.getArgument(1))).collect(Collectors.toList());
					}
				});
		when(userGroupDeletedRepository.save(Mockito.any(OLAPUserGroupDeleted.class)))
				.thenAnswer(new Answer<OLAPUserGroupDeleted>() {

					@Override
					public OLAPUserGroupDeleted answer(InvocationOnMock invocation) throws Throwable {
						OLAPUserGroupDeleted delete = invocation.getArgument(0);
//						delete.setId(delete.getGroupId());
						return delete;
					}

				});

		Mockito.doNothing().when(userGroupRepository).delete(Mockito.any(OLAPUserGroup.class));

		when(bigFiveRepository.findAll()).thenReturn(new Iterable<BigFive>() {

			@Override
			public Iterator<BigFive> iterator() {
				return UserGroupMockData.bigFive.iterator();
			}
		});
	}

	/**
	 * 
	 * Test Get My User GroupList
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetUserGroup_List() throws Exception {
		final String URL = controllerURL;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();

		List<CollapsedMyUserGroup> data = JsonUtils.parseAndGetResponseData(result.getResponse().getContentAsString(),
				CollapsedMyUserGroup.class);

		List<String> bigFives = UserGroupMockData.bigFive.stream().map(e -> e.getValue().toLowerCase())
				.collect(Collectors.toList());
		Set<String> groupsBigFive = UserGroupMockData.olapUserGroups.stream().filter(e -> {
			return bigFives.contains(e.getBigFive().toLowerCase())
					&& e.getFlag().equals(GroupFlag.MY_USER_GROUP.value())
					&& e.getAccount().equals(AuthenticationMockData.account.getId());
		}).map(e -> e.getBigFive()).collect(Collectors.toSet());
		assertEquals(groupsBigFive.size(), data.size());
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
		final String URL = controllerURL + "/" + id;
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
		final String URL = controllerURL + "/" + id;
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
		Long id = 3L;
		final String URL = controllerURL + "/" + id;
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
