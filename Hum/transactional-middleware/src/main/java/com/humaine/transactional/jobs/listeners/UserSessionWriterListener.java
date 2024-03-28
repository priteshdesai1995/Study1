package com.humaine.transactional.jobs.listeners;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.humaine.transactional.model.UserSession;

public class UserSessionWriterListener implements ItemWriteListener<UserSession> {

	private static final Logger logger = LogManager.getLogger(UserSessionWriterListener.class);
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private final String SQL_USER_EVENT_DELETE = "DELETE FROM usersession\n" + "WHERE sessionid in (:ids)";

	public UserSessionWriterListener(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		super();
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public void beforeWrite(List<? extends UserSession> items) {

	}

	@Override
	public void afterWrite(List<? extends UserSession> items) {
		logger.info("UserSession Archive Job:: Delete written data from UserSession Table");
		Map<String, Object> param = Collections.singletonMap("ids",
				items.stream().map(e -> e.getId()).collect(Collectors.toList()));
		int rows = namedParameterJdbcTemplate.update(SQL_USER_EVENT_DELETE, param);

	}

	@Override
	public void onWriteError(Exception exception, List<? extends UserSession> items) {

	}

}
