package com.humaine.collection.api.rest.service.impl;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humaine.collection.api.exception.APIException;
import com.humaine.collection.api.model.User;
import com.humaine.collection.api.model.UserDemographics;
import com.humaine.collection.api.request.dto.UserEventRequest;
import com.humaine.collection.api.rest.repository.UserDemographicsRepository;
import com.humaine.collection.api.rest.repository.UserRepository;
import com.humaine.collection.api.rest.service.UserService;
import com.humaine.collection.api.util.ErrorMessageUtils;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger log = LogManager.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserDemographicsRepository userDemographicsRepository;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Override
	@Transactional
	public User addOrEditUser(UserEventRequest userEventRequest) throws APIException {

		log.debug("In user Add or Edit");

		if (userEventRequest.getAccountID() == null) {
			log.error("Account is Empty");
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.accountID.null", null,
					"api.error.accountID.null.code"));
		}

		if (StringUtils.isBlank(userEventRequest.getUserID())) {
			log.error("User Id is Empty");
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.usereventrequest.userID.null", null,
					"api.error.usereventrequest.userID.null.code"));
		}

		User user = this.userRepository.findByUserAndAccountId(userEventRequest.getUserID(),
				userEventRequest.getAccountID());


		boolean isNew = user == null;

		if (isNew) {
			log.info("User not found with User ID Inserting New User: {}", userEventRequest.getUserID());
			user = new User(userEventRequest);
		}

		user.setPageLoadTime(userEventRequest.getPageLoadTime());

		if (isNew) {
			UserDemographics userDemographics = new UserDemographics(user.getId(), user.getAccount());
			this.userDemographicsRepository.save(userDemographics);
			log.info("User Demo Graphic Info Save");
		}
		this.userRepository.save(user);
		return user;
	}
}
