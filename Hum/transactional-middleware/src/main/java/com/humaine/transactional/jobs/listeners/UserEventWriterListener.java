package com.humaine.transactional.jobs.listeners;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.humaine.transactional.model.UserEvent;

public class UserEventWriterListener implements ItemWriteListener<UserEvent> {

	private static final Logger logger = LogManager.getLogger(UserEventWriterListener.class);
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private final String SQL_USER_EVENT_DELETE = "DELETE FROM userevent\n" + "WHERE usereventid in (:ids)";

	public UserEventWriterListener(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		super();
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public void beforeWrite(List<? extends UserEvent> items) {

	}

	@Override
	public void afterWrite(List<? extends UserEvent> items) {
		logger.info("UserEvent Archive Job:: Delete written data from UserEvent Table");
		Map<String, Object> param = Collections.singletonMap("ids",
				items.stream().map(e -> e.getId()).collect(Collectors.toList()));
		int rows = namedParameterJdbcTemplate.update(SQL_USER_EVENT_DELETE, param);
	}

	@Override
	public void onWriteError(Exception exception, List<? extends UserEvent> items) {
		
	}
}
