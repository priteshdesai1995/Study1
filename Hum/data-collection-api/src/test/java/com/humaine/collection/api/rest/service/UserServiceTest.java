package com.humaine.collection.api.rest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.humaine.collection.api.exception.APIException;
import com.humaine.collection.api.model.User;
import com.humaine.collection.api.request.dto.UserEventRequest;
import com.humaine.collection.api.rest.repository.UserDemographicsRepository;
import com.humaine.collection.api.rest.repository.UserRepository;
import com.humaine.collection.api.util.ErrorMessageUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserService userService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private UserDemographicsRepository userDemographicsRepository;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	private User user = new User("0001", 1L, "Device", "DeviceType", 1000L);

	@Before
	public void setUp() {
		Mockito.when(userRepository.findByUserAndAccountId(user.getId(), user.getAccount())).thenReturn(user);
	}

	/**
	 * Test Add User With Unique UserID and Account Id
	 */
	@Test
	public void testAddOrEditUser_With_Unique_UserId_And_AcountId() {
		UserEventRequest userEventRequest = new UserEventRequest();
		userEventRequest.setUserID("0002");
		userEventRequest.setAccountID(10L);
		userEventRequest.setDeviceId("device Id");
		userEventRequest.setPageLoadTime(2000L);

		User expectedResult = new User(userEventRequest);
		expectedResult.setDeviceType(userEventRequest.getDeviceType());

		User actualResult = userService.addOrEditUser(userEventRequest);
		assertThat(actualResult.getId()).isEqualTo(expectedResult.getId());
		assertThat(actualResult.getAccount()).isEqualTo(expectedResult.getAccount());
		assertThat(actualResult.getDevice()).isEqualTo(expectedResult.getDevice());
		assertThat(actualResult.getPageLoadTime()).isEqualTo(expectedResult.getPageLoadTime());
	}

	/**
	 * Test Add User With User Id null
	 */
	@Test
	public void testAddOrEditUser_With_UserId_null() {
		UserEventRequest userEventRequest = new UserEventRequest();
		userEventRequest.setUserID(null);
		userEventRequest.setAccountID(user.getAccount());
		userEventRequest.setDeviceId("device Id");
		userEventRequest.setPageLoadTime(2000L);

		var expectedOutput = errorMessageUtils.getMessageWithCode("api.error.usereventrequest.userID.null", null,
				"api.error.usereventrequest.userID.null.code");

		Exception exception = assertThrows(APIException.class, () -> {
			userService.addOrEditUser(userEventRequest);
		});

		assertEquals(expectedOutput, exception.getMessage());
	}

	/**
	 * Test Add User With Account Id null
	 */
	@Test
	public void testAddOrEditUser_With_AccountId_null() {
		UserEventRequest userEventRequest = new UserEventRequest();
		userEventRequest.setUserID("0002");
		userEventRequest.setAccountID(null);
		userEventRequest.setDeviceId("device Id");
		userEventRequest.setPageLoadTime(2000L);

		var expectedOutput = errorMessageUtils.getMessageWithCode("api.error.accountID.null", null,
				"api.error.accountID.null.code");

		Exception exception = assertThrows(APIException.class, () -> {
			userService.addOrEditUser(userEventRequest);
		});

		assertEquals(expectedOutput, exception.getMessage());
	}

}
