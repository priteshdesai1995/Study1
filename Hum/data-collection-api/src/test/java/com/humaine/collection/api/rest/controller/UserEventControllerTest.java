package com.humaine.collection.api.rest.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.humaine.collection.api.enums.UserEvents;
import com.humaine.collection.api.es.repository.impl.ESPageLoadDataRespository;
import com.humaine.collection.api.es.repository.impl.ESUserEventRepository;
import com.humaine.collection.api.model.Account;
import com.humaine.collection.api.model.InventoryMaster;
import com.humaine.collection.api.model.Product;
import com.humaine.collection.api.model.Sale;
import com.humaine.collection.api.model.User;
import com.humaine.collection.api.model.UserDemographics;
import com.humaine.collection.api.model.UserSession;
import com.humaine.collection.api.request.dto.UserEventRequest;
import com.humaine.collection.api.rest.repository.AccountRepository;
import com.humaine.collection.api.rest.repository.InventoryRepository;
import com.humaine.collection.api.rest.repository.ProductRepository;
import com.humaine.collection.api.rest.repository.SaleRepository;
import com.humaine.collection.api.rest.repository.UserDemographicsRepository;
import com.humaine.collection.api.rest.repository.UserEventRepository;
import com.humaine.collection.api.rest.repository.UserRepository;
import com.humaine.collection.api.rest.repository.UserSessionRepository;
import com.humaine.collection.api.security.config.Authentication;
import com.humaine.collection.api.security.filter.CustomAuthenticationFilter;
import com.humaine.collection.api.util.DateUtils;
import com.humaine.collection.api.util.ErrorMessageUtils;
import com.humaine.collection.api.util.ErrorStatus;
import com.humaine.collection.api.util.JsonUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserEventControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserSessionRepository userSessionRepository;

	@MockBean
	private AccountRepository accountRepository;

	@MockBean
	private UserEventRepository userEventRepository;

	@MockBean
	private InventoryRepository inventoryRepository;

	@MockBean
	private UserDemographicsRepository userDemographicsRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private SaleRepository saleRepository;
	
	@MockBean
	private ProductRepository productRepository;

	@Mock
	private SecurityContext securityContext;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;
	
	@MockBean
	private ESUserEventRepository esUserEventRepository;

	@MockBean
	private ESPageLoadDataRespository esPageLoadDataRespository;


	// Mock timestemp Data
	private OffsetDateTime timestemp = OffsetDateTime.of(LocalDate.of(2015, 10, 18), LocalTime.of(11, 20, 30, 1000),
			DateUtils.getZoneOffset());

	// Mock UserSession Data
	private UserSession userSession = new UserSession("SES101", "USER101", 1L, "DEVICEID", "New York", "New York",
			"USA", 51.5074D, 0.1278, timestemp, null);

	// Mock User Data
	private User user = new User(userSession.getUser(), userSession.getAccount(), userSession.getDeviceId(), null,
			1000L);

	// Mock Account Data
	private Account account = new Account(userSession.getAccount(), "test@abc.com", "Test Account", "www.test.com",
			"ACTIVE", "GOLD", 0, timestemp, timestemp);

	// Mock Inventory Master Data
	private InventoryMaster inventory = new InventoryMaster(1L, "PROD001", account.getId(), "Test Product Name",
			"Test Desc", "Test Category", 200.12f, null, timestemp);

	// Mock User Event Request
	private UserEventRequest request = this.getUserEventRequest();

	// Mock User Demo Graphic data
	private UserDemographics userDemoGraphicData = new UserDemographics(user.getId(), account.getId(), 10L, "Male",
			"EDU", 5L, "RACE", 400.12f);

	// Mock Sale Data
	private Sale sale = new Sale(001L, request.getUserID(), request.getAccountID(), request.getSessionID(),
			request.getProductID(), request.getProductQuantity(), request.getSaleAmount(), timestemp, 1L, null);

	private Product product = new Product(inventory.getProduct(), account);
	
	private UserEventRequest getUserEventRequest() {
		return new UserEventRequest(account.getId(), userSession.getUser(), userSession.getId(),
				userSession.getDeviceId(), "DEVICETYPE", UserEvents.START, inventory.getProduct(),
				"www.producturl.test", 1000L, 1300.32f, 20L, userSession.getCity(), userSession.getState(),
				userSession.getCountry(), userSession.getLongitude(), userSession.getLatitude(), "ABC COMPANY");
	}

	private String BASE_URI = "/userEvent";

	private String requestURI = String.format("%s", BASE_URI);

	private String KEY = "TESTKEY";

	@Before
	public void setUp() {

		org.springframework.security.core.userdetails.User usr = new org.springframework.security.core.userdetails.User(
				account.getEmail(), "", new HashSet<GrantedAuthority>());
		Authentication auth = new Authentication(usr, null, account);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(securityContext.getAuthentication()).thenReturn(auth);

		Mockito.when(accountRepository.findAccountByAPIKey(Mockito.anyString())).thenReturn(account);

		Mockito.when(userSessionRepository.findById(userSession.getId())).thenReturn(Optional.of(userSession));
		Mockito.when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
		Mockito.when(userRepository.findByUserAndAccountId(user.getId(), user.getAccount())).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(user);
		Mockito.when(userDemographicsRepository.save(userDemoGraphicData)).thenReturn(userDemoGraphicData);
		Mockito.when(saleRepository.save(sale)).thenReturn(sale);
		Mockito.when(saleRepository.findById(sale.getId())).thenReturn(Optional.of(sale));
//		Mockito.when(inventoryRepository.findByProductId(inventory.getProduct())).thenReturn(inventory);
		Mockito.when(productRepository.getProductsByAccountAndId(user.getAccount(), inventory.getProduct())).thenReturn(product);
		Mockito.doNothing().when(esUserEventRepository).indexUserEvent(Mockito.any(Long.class));
	}

	/**
	 * Test Save User Event for Event Start
	 */
	@Test
	public void saveUserEvent() throws Exception {
		UserEventRequest requeustBody = this.getUserEventRequest();
		requeustBody.setUserID("USER104");
		requeustBody.setSessionID("SESS1100");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value())))
				.andExpect(jsonPath("$.responseData.message",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent"))))
				.andExpect(jsonPath("$.responseData.code",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent.code"))))
				.andReturn();
	}

	/**
	 * Test Save User Event for Event Start
	 */
	@Test
	public void saveUserEvent_Start_Event_With_Already_Started_Session() throws Exception {
		UserEventRequest requeustBody = this.getUserEventRequest();
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.session.already.start",
								new Object[] { requeustBody.getSessionID() }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.session.already.start.code"))))
				.andReturn();
	}

	/**
	 * Test Save User Event: Call Other Action When Session Not Started
	 */
	@Test
	public void saveUserEvent_Other_Action_When_Session_Not_Started() throws Exception {
		UserEventRequest requeustBody = this.getUserEventRequest();
		requeustBody.setSessionID("SESS454");
		requeustBody.setEventID(UserEvents.BUY);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.session.notfound",
								new Object[] { requeustBody.getSessionID() }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.session.notfound.code"))))
				.andReturn();
	}

	/**
	 * Test Save User Event: All Required Field Null
	 */
	@Test
	public void saveUserEvent_With_All_Required_Filed_Null() throws Exception {
		UserEventRequest requeustBody = this.getUserEventRequest();
		requeustBody.setSessionID(null);
		requeustBody.setEventID(null);
		requeustBody.setAccountID(null);
		requeustBody.setDeviceId(null);
		requeustBody.setDeviceType(null);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value()))).andReturn();
	}

	/**
	 * Test User Event for Event Start With Account not found
	 */
	@Test
	public void saveUserEvent_With_Unknow_Account() throws Exception {
		UserEventRequest requeustBody = this.getUserEventRequest();
		requeustBody.setSessionID("SES656");
		requeustBody.setAccountID(02L);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value()))).andReturn();
	}

	/**
	 * Test Product Buy Event
	 */
	@Test
	public void saveUserEvent_Action_Buy() throws Exception {
		UserEventRequest requeustBody = this.getUserEventRequest();
		requeustBody.setEventID(UserEvents.BUY);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value())))
				.andExpect(jsonPath("$.responseData.message",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent"))))
				.andExpect(jsonPath("$.responseData.code",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent.code"))))
				.andReturn();
	}

	/**
	 * Test Product Buy Event with product Id null
	 */
	@Test
	public void saveUserEvent_Action_Buy_With_ProductId_Null() throws Exception {
		UserEventRequest requeustBody = this.getUserEventRequest();
		requeustBody.setEventID(UserEvents.BUY);
		requeustBody.setProductID(null);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.usereventrequest.productId.null"))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.usereventrequest.productId.null.code"))))
				.andReturn();
	}

	/**
	 * Test Product Buy Event with productQuantity zero
	 */
	@Test
	public void saveUserEvent_Action_Buy_With_productQuantity_Zero() throws Exception {
		UserEventRequest requeustBody = this.getUserEventRequest();
		requeustBody.setEventID(UserEvents.BUY);
		requeustBody.setProductQuantity(0L);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.usereventrequest.productQuantity.invalid"))))
				.andExpect(
						jsonPath("$.errorList[0].code",
								is(this.errorMessageUtils
										.getMessage("api.error.usereventrequest.productQuantity.invalid.code"))))
				.andReturn();
	}

	/**
	 * Test Product Buy Event with Negative productQuantity
	 */
	@Test
	public void saveUserEvent_Action_Buy_With_productQuantity_Negative() throws Exception {
		UserEventRequest requeustBody = this.getUserEventRequest();
		requeustBody.setEventID(UserEvents.BUY);
		requeustBody.setProductQuantity(-10L);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.usereventrequest.productQuantity.invalid"))))
				.andExpect(
						jsonPath("$.errorList[0].code",
								is(this.errorMessageUtils
										.getMessage("api.error.usereventrequest.productQuantity.invalid.code"))))
				.andReturn();
	}

	/**
	 * Test Product Buy Event with Product Sale Amount zero
	 */
	@Test
	public void saveUserEvent_Action_Buy_With_productAmount_Zero() throws Exception {
		UserEventRequest requeustBody = this.getUserEventRequest();
		requeustBody.setEventID(UserEvents.BUY);
		requeustBody.setSaleAmount(0f);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.usereventrequest.saleAmount.invalid"))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.usereventrequest.saleAmount.invalid.code"))))
				.andReturn();
	}

	/**
	 * Test Product Buy Event with Negative Product Sale Amount
	 */
	@Test
	public void saveUserEvent_Action_Buy_With_productAmount_Negative() throws Exception {
		UserEventRequest requeustBody = this.getUserEventRequest();
		requeustBody.setEventID(UserEvents.BUY);
		requeustBody.setSaleAmount(-10.20f);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.usereventrequest.saleAmount.invalid"))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.usereventrequest.saleAmount.invalid.code"))))
				.andReturn();
	}

	/**
	 * Test Event Request with session Id that already closed
	 */
	@Test
	public void saveUserEvent_When_Sessio_Already_Closed() throws Exception {
		userSession.setEndTime(timestemp);
		UserEventRequest requeustBody = this.getUserEventRequest();
		requeustBody.setEventID(UserEvents.ADDCART);
		requeustBody.setSessionID(userSession.getId());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.usereventrequest.session.expired",
								new String[] { userSession.getId() }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.usereventrequest.session.expired.code"))))
				.andReturn();
	}

	/**
	 * Test Update Event: End User Session
	 */
	@Test
	public void saveUserEvent_End_Session() throws Exception {
		UserEventRequest requeustBody = this.getUserEventRequest();
		requeustBody.setEventID(UserEvents.END);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value())))
				.andExpect(jsonPath("$.responseData.message",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent"))))
				.andExpect(jsonPath("$.responseData.code",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent.code"))))
				.andReturn();
	}

	/**
	 * Test Update Event: End User Session that not started yet
	 */
	@Test
	public void saveUserEvent_End_Unknown_Session() throws Exception {
		UserEventRequest requeustBody = this.getUserEventRequest();
		requeustBody.setEventID(UserEvents.END);
		requeustBody.setSessionID("SESSION102");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.session.notfound",
								new Object[] { requeustBody.getSessionID() }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.session.notfound.code"))))
				.andReturn();
	}

	/**
	 * Test save Discover event
	 * 
	 * @throws Exception
	 */
	@Test
	public void saveDisover() throws Exception {
		UserEventRequest eventRequest = this.getUserEventRequest();
		UserEventRequest requeustBody = new UserEventRequest();
		requeustBody.setUserID(eventRequest.getUserID());
		requeustBody.setAccountID(eventRequest.getAccountID());
		requeustBody.setPageURL(eventRequest.getPageURL());
		requeustBody.setSessionID(eventRequest.getSessionID());
		requeustBody.setEventID(UserEvents.DISCOVER);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value())))
				.andExpect(jsonPath("$.responseData.message",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent"))))
				.andExpect(jsonPath("$.responseData.code",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent.code"))))
				.andReturn();
	}

	/**
	 * Test save Discover event without pageURL
	 * 
	 * @throws Exception
	 */
	@Test
	public void saveDisoverWithEmptyPageURL() throws Exception {
		UserEventRequest eventRequest = this.getUserEventRequest();
		UserEventRequest requeustBody = new UserEventRequest();
		requeustBody.setUserID(eventRequest.getUserID());
		requeustBody.setAccountID(eventRequest.getAccountID());
		requeustBody.setSessionID(eventRequest.getSessionID());
		requeustBody.setEventID(UserEvents.DISCOVER);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.pageURL.null"))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.pageURL.null.code"))))
				.andReturn();
	}

	/**
	 * Test news latter subscribe Events
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewLatterSubscribe() throws Exception {
		UserEventRequest eventRequest = this.getUserEventRequest();
		UserEventRequest requeustBody = new UserEventRequest();
		requeustBody.setUserID(eventRequest.getUserID());
		requeustBody.setAccountID(eventRequest.getAccountID());
		requeustBody.setSessionID(eventRequest.getSessionID());
		requeustBody.setEventID(UserEvents.NEWSLETTER_SUBSCRIBE);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value())))
				.andExpect(jsonPath("$.responseData.message",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent"))))
				.andExpect(jsonPath("$.responseData.code",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent.code"))))
				.andReturn();
	}

	/**
	 * Test Search Event
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSearchEvent() throws Exception {
		UserEventRequest eventRequest = this.getUserEventRequest();
		UserEventRequest requeustBody = new UserEventRequest();
		requeustBody.setUserID(eventRequest.getUserID());
		requeustBody.setAccountID(eventRequest.getAccountID());
		requeustBody.setSessionID(eventRequest.getSessionID());
		requeustBody.setEventID(UserEvents.SEARCH);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value())))
				.andExpect(jsonPath("$.responseData.message",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent"))))
				.andExpect(jsonPath("$.responseData.code",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent.code"))))
				.andReturn();
	}

	/**
	 * Test Menu Event
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMenuEvent() throws Exception {
		UserEventRequest eventRequest = this.getUserEventRequest();
		UserEventRequest requeustBody = new UserEventRequest();
		requeustBody.setUserID(eventRequest.getUserID());
		requeustBody.setAccountID(eventRequest.getAccountID());
		requeustBody.setSessionID(eventRequest.getSessionID());
		requeustBody.setEventID(UserEvents.MENU);
		requeustBody.setMenuId("MENUID");
		requeustBody.setMenuName("TEST MENU NAME");
		requeustBody.setMenuURL("www.menuurl.com");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value())))
				.andExpect(jsonPath("$.responseData.message",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent"))))
				.andExpect(jsonPath("$.responseData.code",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent.code"))))
				.andReturn();
	}

	/**
	 * Test Menu Event without menuUrl
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMenuEventWithEmptymenuUrl() throws Exception {
		UserEventRequest eventRequest = this.getUserEventRequest();
		UserEventRequest requeustBody = new UserEventRequest();
		requeustBody.setUserID(eventRequest.getUserID());
		requeustBody.setAccountID(eventRequest.getAccountID());
		requeustBody.setSessionID(eventRequest.getSessionID());
		requeustBody.setEventID(UserEvents.MENU);
		requeustBody.setMenuId("MENUID");
		requeustBody.setMenuName("TEST MENU NAME");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.menuURL.null"))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.menuURL.null.code"))))
				.andReturn();
	}

	/**
	 * Test Menu Event with empty menuID
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMenuEventWithEmptymenuID() throws Exception {
		UserEventRequest eventRequest = this.getUserEventRequest();
		UserEventRequest requeustBody = new UserEventRequest();
		requeustBody.setUserID(eventRequest.getUserID());
		requeustBody.setAccountID(eventRequest.getAccountID());
		requeustBody.setSessionID(eventRequest.getSessionID());
		requeustBody.setEventID(UserEvents.MENU);
		requeustBody.setMenuName("TEST MENU NAME");
		requeustBody.setMenuURL("www.menuurl.com");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.menuID.null"))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.menuID.null.code"))))
				.andReturn();
	}

	/**
	 * Test Menu Event with empty menuName
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMenuEventWithEmptymenuName() throws Exception {
		UserEventRequest eventRequest = this.getUserEventRequest();
		UserEventRequest requeustBody = new UserEventRequest();
		requeustBody.setUserID(eventRequest.getUserID());
		requeustBody.setAccountID(eventRequest.getAccountID());
		requeustBody.setSessionID(eventRequest.getSessionID());
		requeustBody.setEventID(UserEvents.MENU);
		requeustBody.setMenuId("MENUID");
		requeustBody.setMenuURL("www.menuurl.com");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.menuName.null"))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.menuName.null.code"))))
				.andReturn();
	}

	/**
	 * Test Back Navigation Event
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBackNav() throws Exception {
		UserEventRequest eventRequest = this.getUserEventRequest();
		UserEventRequest requeustBody = new UserEventRequest();
		requeustBody.setUserID(eventRequest.getUserID());
		requeustBody.setAccountID(eventRequest.getAccountID());
		requeustBody.setSessionID(eventRequest.getSessionID());
		requeustBody.setPageURL(eventRequest.getPageURL());
		requeustBody.setEventID(UserEvents.BACK_NAV);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value())))
				.andExpect(jsonPath("$.responseData.message",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent"))))
				.andExpect(jsonPath("$.responseData.code",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent.code"))))
				.andReturn();
	}

	/**
	 * Test Back Navigation Event without pageUrl
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBackNavEventWithEmptyPageUrl() throws Exception {
		UserEventRequest eventRequest = this.getUserEventRequest();
		UserEventRequest requeustBody = new UserEventRequest();
		requeustBody.setUserID(eventRequest.getUserID());
		requeustBody.setAccountID(eventRequest.getAccountID());
		requeustBody.setSessionID(eventRequest.getSessionID());
		requeustBody.setEventID(UserEvents.BACK_NAV);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.pageURL.null"))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.pageURL.null.code"))))
				.andReturn();
	}

	/**
	 * Test Save For later event
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSaveForLater() throws Exception {
		UserEventRequest eventRequest = this.getUserEventRequest();
		UserEventRequest requeustBody = new UserEventRequest();
		requeustBody.setUserID(eventRequest.getUserID());
		requeustBody.setAccountID(eventRequest.getAccountID());
		requeustBody.setSessionID(eventRequest.getSessionID());
		requeustBody.setProductID(eventRequest.getProductID());
		requeustBody.setEventID(UserEvents.SAVE_FOR_LATER);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value())))
				.andExpect(jsonPath("$.responseData.message",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent"))))
				.andExpect(jsonPath("$.responseData.code",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent.code"))))
				.andReturn();
	}

	/**
	 * Test Save For later event with productId null
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSaveForLaterWithOutProductId() throws Exception {
		UserEventRequest eventRequest = this.getUserEventRequest();
		UserEventRequest requeustBody = new UserEventRequest();
		requeustBody.setUserID(eventRequest.getUserID());
		requeustBody.setAccountID(eventRequest.getAccountID());
		requeustBody.setSessionID(eventRequest.getSessionID());
		requeustBody.setEventID(UserEvents.SAVE_FOR_LATER);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.usereventrequest.productId.null"))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.usereventrequest.productId.null.code"))))
				.andReturn();
	}

	/**
	 * Test Product Return event
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReturnProduct() throws Exception {
		UserEventRequest eventRequest = this.getUserEventRequest();
		UserEventRequest requeustBody = new UserEventRequest();
		requeustBody.setUserID(eventRequest.getUserID());
		requeustBody.setAccountID(eventRequest.getAccountID());
		requeustBody.setSessionID(eventRequest.getSessionID());
		requeustBody.setProductID(eventRequest.getProductID());
		requeustBody.setEventID(UserEvents.PROD_RETURN);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value())))
				.andExpect(jsonPath("$.responseData.message",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent"))))
				.andExpect(jsonPath("$.responseData.code",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent.code"))))
				.andReturn();
	}

	/**
	 * Test Product Return event with productId null
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReturnProductWithOutProductId() throws Exception {
		UserEventRequest eventRequest = this.getUserEventRequest();
		UserEventRequest requeustBody = new UserEventRequest();
		requeustBody.setUserID(eventRequest.getUserID());
		requeustBody.setAccountID(eventRequest.getAccountID());
		requeustBody.setSessionID(eventRequest.getSessionID());
		requeustBody.setEventID(UserEvents.PROD_RETURN);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.usereventrequest.productId.null"))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.usereventrequest.productId.null.code"))))
				.andReturn();
	}

	/**
	 * Test Visit BlogPost event
	 * 
	 * @throws Exception
	 */
	@Test
	public void testVisitBlogPost() throws Exception {
		UserEventRequest eventRequest = this.getUserEventRequest();
		UserEventRequest requeustBody = new UserEventRequest();
		requeustBody.setUserID(eventRequest.getUserID());
		requeustBody.setAccountID(eventRequest.getAccountID());
		requeustBody.setSessionID(eventRequest.getSessionID());
		requeustBody.setEventID(UserEvents.VISIT_BLOG_POST);
		requeustBody.setPostTitle("Test Post Title");
		requeustBody.setPostId("POSTID1001");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value())))
				.andExpect(jsonPath("$.responseData.message",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent"))))
				.andExpect(jsonPath("$.responseData.code",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent.code"))))
				.andReturn();
	}

	/**
	 * Test Visit BlogPost event with BlogPost title Empty
	 * 
	 * @throws Exception
	 */
	@Test
	public void testVisitBlogPostWithEmptyBlogTitle() throws Exception {
		UserEventRequest eventRequest = this.getUserEventRequest();
		UserEventRequest requeustBody = new UserEventRequest();
		requeustBody.setUserID(eventRequest.getUserID());
		requeustBody.setAccountID(eventRequest.getAccountID());
		requeustBody.setSessionID(eventRequest.getSessionID());
		requeustBody.setPostId("POSTID1001");
		requeustBody.setEventID(UserEvents.VISIT_BLOG_POST);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.post-title.null"))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.post-title.null.code"))))
				.andReturn();
	}

	/**
	 * Test Visit BlogPost event with BlogPost ID Empty
	 * 
	 * @throws Exception
	 */
	@Test
	public void testVisitBlogPostWithEmptyPostID() throws Exception {
		UserEventRequest eventRequest = this.getUserEventRequest();
		UserEventRequest requeustBody = new UserEventRequest();
		requeustBody.setUserID(eventRequest.getUserID());
		requeustBody.setAccountID(eventRequest.getAccountID());
		requeustBody.setSessionID(eventRequest.getSessionID());
		requeustBody.setPostTitle("Test Post Title");
		requeustBody.setEventID(UserEvents.VISIT_BLOG_POST);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.postID.null"))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.postID.null.code"))))
				.andReturn();
	}

	/**
	 * Test Visit social media Link
	 * 
	 * @throws Exception
	 */
	@Test
	public void testVisitSocailMediaLink() throws Exception {
		UserEventRequest eventRequest = this.getUserEventRequest();
		UserEventRequest requeustBody = new UserEventRequest();
		requeustBody.setUserID(eventRequest.getUserID());
		requeustBody.setAccountID(eventRequest.getAccountID());
		requeustBody.setSessionID(eventRequest.getSessionID());
		requeustBody.setEventID(UserEvents.VISIT_SOCIAL_MEDIA);
		requeustBody.setSocialMediaPlateForm("FaceBok");
		requeustBody.setSocialMediaURL("www.fb.com");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value())))
				.andExpect(jsonPath("$.responseData.message",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent"))))
				.andExpect(jsonPath("$.responseData.code",
						is(this.errorMessageUtils.getMessage("api.success.usereventrequest.saveUserEvent.code"))))
				.andReturn();
	}

	/**
	 * Test Visit social media Link with SocialMediaPlateForm Empty
	 * 
	 * @throws Exception
	 */
	@Test
	public void testVisitSocailMediaLinkWithEmptySocialMediaPlateForm() throws Exception {
		UserEventRequest eventRequest = this.getUserEventRequest();
		UserEventRequest requeustBody = new UserEventRequest();
		requeustBody.setUserID(eventRequest.getUserID());
		requeustBody.setAccountID(eventRequest.getAccountID());
		requeustBody.setSessionID(eventRequest.getSessionID());
		requeustBody.setSocialMediaURL("www.fb.com");
		requeustBody.setEventID(UserEvents.VISIT_SOCIAL_MEDIA);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.social-media-platform.null"))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.social-media-platform.null.code"))))
				.andReturn();
	}

	/**
	 * Test Visit social media Link with SocialMediaURL Empty
	 * 
	 * @throws Exception
	 */
	@Test
	public void testVisitBlogPostWithEmptySocialMediaURL() throws Exception {
		UserEventRequest eventRequest = this.getUserEventRequest();
		UserEventRequest requeustBody = new UserEventRequest();
		requeustBody.setUserID(eventRequest.getUserID());
		requeustBody.setAccountID(eventRequest.getAccountID());
		requeustBody.setSessionID(eventRequest.getSessionID());
		requeustBody.setSocialMediaPlateForm("FaceBok");
		requeustBody.setEventID(UserEvents.VISIT_SOCIAL_MEDIA);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestURI).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("version", "v1")
				.header(CustomAuthenticationFilter.HEADER, KEY).content(JsonUtils.json(requeustBody));
		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.social-media-url.null"))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.social-media-url.null.code"))))
				.andReturn();
	}
}
