//package com.base.api.controller;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.security.Principal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import org.junit.Before;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.json.JacksonJsonParser;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.provider.ClientDetails;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.TokenRequest;
//import org.springframework.security.oauth2.provider.client.BaseClientDetails;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.web.FilterChainProxy;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.context.WebApplicationContext;
//
//import com.base.api.BaseApplication;
//import com.base.api.entities.User;
//import com.base.api.enums.UserStatus;
//import com.base.api.mock.UserMockData;
//import com.base.api.request.dto.UserSignupDTO;
//import com.base.api.security.UserPrincipal;
//import com.base.api.service.RoleService;
//import com.base.api.service.UserService;
//import com.base.api.service.impl.UserServiceImpl;
//import com.base.api.util.JsonUtils;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = BaseApplication.class)
////@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class })
//@AutoConfigureMockMvc()
//@WithMockUser
//public class UserControllerTest {
//
//	@Autowired
//	private MockMvc mockMvc;
//
////	@Autowired
////	private ErrorMessageUtils errorMessageUtils;
//
//	@MockBean
//	UserService userService;
//
////	@MockBean
////	CommonUserService commonUserService;
//
//	@MockBean
//	UserServiceImpl userServiceImpl;
//
//	@MockBean
//	RoleService roleService;
//
//	@Autowired
//	private WebApplicationContext wac;
//
//	@Autowired
//	private FilterChainProxy springSecurityFilterChain;
//
//	@Mock
//	private SecurityContext securityContext;
//
//	@Autowired
//	TokenStore tokenStore;
//
//	@Mock
//	Principal userPrincipal;
//
//	private static final String BASE_URL = "/api/user";
//
//	private OAuth2AccessToken token;
//	private OAuth2Authentication authentication;
//
//	public static User user = new User("Pinky Ramnani", "Pinky Ramnani", "pinky.ramnani.user@brainvire,com", "female",
//			null, "9826773833", UserStatus.ACTIVE, 1L, "ROLE_SUPERADMIN");
//
//	@Before
//	public void setUp() {
//		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(springSecurityFilterChain).build();
//		token = new DefaultOAuth2AccessToken("FOO");
//		ClientDetails client = new BaseClientDetails("client", null, "read", "client_credentials",
//				"ROLE_READER,ROLE_CLIENT,ROLE_SUPERADMIN");
//		// Authorities
//		List<GrantedAuthority> authority = new ArrayList<GrantedAuthority>();
//		authority.add(new SimpleGrantedAuthority("ROLE_SUPERADMIN"));
//		UserPrincipal principal = new UserPrincipal(this.user, false, false, false, true, UUID.fromString("test"));
//		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal,
//				principal, authority);
//		authentication = new OAuth2Authentication(
//				new TokenRequest(null, "client", null, "client_credentials").createOAuth2Request(client),
//				authenticationToken);
//		tokenStore.storeAccessToken(token, authentication);
//		Mockito.when(securityContext.getAuthentication()).thenReturn(authenticationToken);
////		when(userService.findByUserId(Mockito.anyLong())).thenAnswer(new Answer<UserEntity>() {
////			@Override
////			public UserEntity answer(InvocationOnMock invocation) throws Throwable {
////				List<UserEntity> users = UserMockData.userList.stream()
////						.filter(e -> e.getUserId().equals(invocation.getArgument(0))).collect(Collectors.toList());
////				if (users.isEmpty())
////					return null;
////				return users.get(0);
////			}
////		});
//
////		when(userService.signupByAdmin(Mockito.any(AdminAddUserDTO.class), Mockito.anyString())).thenReturn("OK");
////
////		when(userService.search(Mockito.any(UserFilter.class), Mockito.anyString()))
////				.thenReturn(UserMockData.searchData());
//
////		Mockito.when(userService.deleteUser(Mockito.anyLong())).thenReturn("OK");
//	}
//
////	@Test
//	public void obtainAccessToken() throws Exception {
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("grant_type", "client_credentials");
//		params.add("client_id", "client");
//		params.add("username", "writer");
//		params.add("password", "writer");
//		params.add("client_secret", "Brain@2020");
//		ResultActions result = mockMvc
//				.perform(post("/oauth/token").params(params).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
//				.andExpect(status().isOk());
//		String resultString = result.andReturn().getResponse().getContentAsString();
//		JacksonJsonParser jsonParser = new JacksonJsonParser();
//		System.out.println(jsonParser.parseMap(resultString).get("access_token").toString());
//	}
//
////	@Test
//	public void existentUserCanGetTokenAndAuthentication() throws Exception {
//		String username = "pinky.ramnani.user@brainvire,com";
//		String password = "Brain@2021";
//		String body = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"," + " \"grant_type\":\""
//				+ "password" + "\", \"client_id\":\"" + "common" + "\", \"client_secret\":\"" + "Brain@2020" + "\"}";
//		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
//		parameters.add("username", username);
//		parameters.add("password", password);
//		parameters.add("client_id", "common");
//		parameters.add("client_secret", "Brain@2020");
//		parameters.add("grant_type", "password");
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//		MvcResult result = this.mockMvc.perform(post("/oauth/token")
////				.headers(headers)
//				.contentType(MediaType.APPLICATION_FORM_URLENCODED).params(parameters)).andExpect(status().isOk())
//				.andReturn();
//		String response = result.getResponse().getContentAsString();
//		response = response.replace("{\"access_token\": \"", "");
//		String token = response.replace("\"}", "");
//		System.out.println("token" + token);
////		mockMvc.perform(
////				MockMvcRequestBuilders.get("/users/" + 1 + "/rounds").header("Authorization", "Bearer " + token))
////				.andExpect(status().isOk());
//	}
//
//	/**
//	 * User List
//	 * 
//	 * @throws Exception
//	 */
////	@Test
////	public void testGetAllUsers() throws Exception {
////		UserFilter request = UserMockData.getRquest();
////		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(BASE_URL + "/search/query")
////				.contentType(MediaType.APPLICATION_JSON).header("Version", "V1")
////				.header(HttpHeaders.AUTHORIZATION, "Bearer FOO").content(JsonUtils.json(request));
////		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
//////				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
////		List<User> users = new ArrayList<>();
////		users = JsonUtils.parseAndGetResponseData(result.getResponse().getContentAsString(), UserEntity.class);
////		assertEquals(UserMockData.userList.size(), users.size());
////	}
//
////	/**
////	 * Get User By Id
////	 * 
////	 * @throws Exception
////	 */
////	@Test
////	public void testGetUserById() throws Exception {
////		Long id = 1L;
////		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URL + "/" + id)
////				.contentType(MediaType.APPLICATION_JSON).header("Version", "V1").header("Authorization", "Bearer FOO");
////
////		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk())
////				// .andExpect(jsonPath("$.status", is("true")))
////				.andReturn();
////	}
//
//	/**
//	 * save Test New User
//	 * 
//	 * @throws Exception
//	 */
//	@Test
//	public void testSaveTestNewUser_400() throws Exception {
//		UserSignupDTO request = UserMockData.addUser_400();
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
//				.header("Version", "V1").header("Authorization", "Bearer FOO").content(JsonUtils.json(request));
//		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
//				.andExpect(jsonPath("$.errors", hasItem("User name can't be null or empty"))).andReturn();
////				.andExpect(jsonPath("$.errors", hasItem("Last name can't be null or empty")))
////				.andExpect(jsonPath("$.errors", hasItem("First name can't be null or empty"))).andReturn();
//	}
//
//	/**
//	 * save Test New User
//	 * 
//	 * @throws Exception
//	 * 
//	 */
////	@Test
////	public void testSaveTestNewUser_200() throws Exception {
////		AdminAddUserDTO request = UserMockData.addUser_200();
////		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
////				.header("Version", "V1").header("Authorization", "Bearer FOO").content(JsonUtils.json(request));
////		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isCreated()).andReturn();
////	}
//
////	@Test
////	public void testDeleteTestUser() throws Exception {
////		Long id = UserMockData.userList.get(0).getUserId();
////		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(BASE_URL + "/delete/" + id)
////				.contentType(MediaType.APPLICATION_JSON).header("Version", "V1").header("Authorization", "Bearer FOO");
////		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
//////				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
////	}
//
//}