package com.humaine.transactional.jobs.writer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.humaine.transactional.model.UserSession;

public class UserSessionWriter implements ItemWriter<UserSession> {

	private JdbcTemplate jdbcTemplate;

	private final String SQL_USER_SESSOIN_ARCHIVE_INSERT = "INSERT INTO usersessionarchive\n"
			+ "(sessionid, userid, accountid, deviceid, city, state, country, lat, long, starttime, endtime)\n"
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public UserSessionWriter(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void write(List<? extends UserSession> items) throws Exception {
		List<UserSession> userSessions = items.stream().filter(e -> e != null).map(e -> e).collect(Collectors.toList());
		int[] insertedRecords = jdbcTemplate.batchUpdate(SQL_USER_SESSOIN_ARCHIVE_INSERT,
				new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						UserSession session = userSessions.get(i);
						ps.setString(1, session.getId());
						ps.setString(2, session.getUser());
						ps.setLong(3, session.getAccount());
						ps.setString(4, session.getDeviceId());
						ps.setString(5, session.getCity());
						ps.setString(6, session.getState());
						ps.setString(7, session.getCountry());
						if (session.getLatitude() != null) {
							ps.setDouble(8, session.getLatitude());
						} else {
							ps.setNull(8, Types.DOUBLE);
						}
						if (session.getLatitude() != null) {
							ps.setDouble(9, session.getLatitude());
						} else {
							ps.setNull(9, Types.DOUBLE);
						}
						ps.setObject(10, session.getStartTime());
						if (session.getEndTime() != null) {
							ps.setObject(11, session.getEndTime());
						} else {
							ps.setNull(9, Types.TIMESTAMP);
						}
					}

					@Override
					public int getBatchSize() {
						return userSessions.size();
					}
				});
	}

}
