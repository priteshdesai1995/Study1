package com.humaine.transactional.jobs;

import java.time.OffsetDateTime;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.humaine.transactional.util.DateUtils;

@Configuration
public class PageLoadDataJob {

	@Autowired
	DataSource dataSource;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Value("${page.load.data.retain.range}")
	Integer range;

	private final String SQL_DELETE_PAGE_LOAD_DATA_QUERY = "DELETE FROM pageload_data pd WHERE DATE(pd.\"timestamp\") <= DATE(?)";

	public int deleteOlderSchedular() {
		OffsetDateTime date = DateUtils.getCurrentTimestemp();
		Object[] args = new Object[] { DateUtils.getFromatedDate(date.minusDays(range)) };
		int deletedCount = jdbcTemplate.update(SQL_DELETE_PAGE_LOAD_DATA_QUERY, args);
		return deletedCount;
	}
}
