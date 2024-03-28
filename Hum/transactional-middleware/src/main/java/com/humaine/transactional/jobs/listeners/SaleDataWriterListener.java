package com.humaine.transactional.jobs.listeners;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.humaine.transactional.jobs.SaleDataJob;
import com.humaine.transactional.model.Sale;

public class SaleDataWriterListener implements ItemWriteListener<Sale> {

	private static final Logger logger = LogManager.getLogger(SaleDataWriterListener.class);

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private final String SQL_USER_EVENT_DELETE = "DELETE FROM saledata\n" + "WHERE saleid in (:ids)";

	public SaleDataWriterListener(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		super();
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public void beforeWrite(List<? extends Sale> items) {

	}

	@Override
	public void afterWrite(List<? extends Sale> items) {
		logger.info("SaleData Archive Job:: Delete written data from SaleData Table");
		Map<String, Object> param = Collections.singletonMap("ids",
				items.stream().map(e -> e.getId()).collect(Collectors.toList()));
		int rows = namedParameterJdbcTemplate.update(SQL_USER_EVENT_DELETE, param);
	}

	@Override
	public void onWriteError(Exception exception, List<? extends Sale> items) {

	}

}
