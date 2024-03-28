package com.humaine.transactional.jobs.writer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humaine.transactional.model.UserEvent;
import com.humaine.transactional.model.UserEventArchive;

public class UserEventWriter implements ItemWriter<UserEvent> {

	private JdbcTemplate jdbcTemplate;

	private final String SQL_USER_EVENT_ARCHIVE_INSERT = "INSERT INTO usereventarchive\n"
			+ "(usereventid,userid, accountid, eventid, sessionid, productid, pageurl, timestamp, post_id,post_title,product_metadata,social_media_platform,social_media_url,menu_id,menu_name,menu_url)\n"
			+ "VALUES(?,?, ?, ?, ?, ?, ?, ?, ?,?, cast(? as JSONB), ?, ?, ?, ?, ?)";

	ObjectMapper objectMapper;

	public UserEventWriter(JdbcTemplate jdbcTemplate) {
		super();
		this.objectMapper = new ObjectMapper();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void write(List<? extends UserEvent> items) throws Exception {
		List<UserEventArchive> userEvents = items.stream().map(e -> new UserEventArchive(e))
				.collect(Collectors.toList());
		int[] insertedRecords = jdbcTemplate.batchUpdate(SQL_USER_EVENT_ARCHIVE_INSERT,
				new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ObjectMapper objectMapper = new ObjectMapper();
						UserEventArchive event = userEvents.get(i);
						ps.setLong(1, event.getId());
						ps.setString(2, event.getUser());
						ps.setLong(3, event.getAccount());
						ps.setString(4, event.getEvent());
						ps.setString(5, event.getSession());
						ps.setString(6, event.getProductid());
						ps.setString(7, event.getPageUrl());
						ps.setObject(8, event.getTimestamp());
						ps.setString(9, event.getPostId());
						ps.setString(10, event.getPostTitle());
						if (event.getProductMetaData() != null) {
							String data = null;
							try {
								data = objectMapper.writeValueAsString(event.getProductMetaData());
							} catch (Exception e) {
								// TODO: handle exception
							} finally {
								ps.setString(11, data == null ? "{}" : data);
							}

						} else {
							ps.setString(11, "{}");
						}
						ps.setString(12, event.getSocialMediaPlateform());
						ps.setString(13, event.getSocialMediaURL());
						ps.setString(14, event.getMenuId());
						ps.setString(15, event.getMenuName());
						ps.setString(16, event.getMenuURL());

					}

					@Override
					public int getBatchSize() {
						return userEvents.size();
					}
				});
	}
}
