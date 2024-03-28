package com.humaine.transactional.jobs.writer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.humaine.transactional.model.Sale;

public class SaleDataWriter implements ItemWriter<Sale> {

	private JdbcTemplate jdbcTemplate;

	private final String SQL_SALE_DATA_ARCHIVE_INSERT = "INSERT INTO saledataarchive\n"
			+ "(saleid, userid, accountid, saleon, sessionid, productid, saleamount, productquantity, usereventid)\n"
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public SaleDataWriter(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void write(List<? extends Sale> items) throws Exception {
		List<Sale> saleData = items.stream().map(e -> e).collect(Collectors.toList());
		int[] insertedRecords = jdbcTemplate.batchUpdate(SQL_SALE_DATA_ARCHIVE_INSERT,
				new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						Sale sale = saleData.get(i);
						ps.setLong(1, sale.getId());
						ps.setString(2, sale.getUser());
						ps.setLong(3, sale.getAccount());
						ps.setObject(4, sale.getSaleOn());
						ps.setString(5, sale.getSession());
						ps.setString(6, sale.getProduct());
						if (sale.getSaleAmount() != null) {
							ps.setFloat(7, sale.getSaleAmount());
						} else {
							ps.setNull(7, Types.FLOAT);
						}
						if (sale.getProductQuantity() != null) {
							ps.setLong(8, sale.getProductQuantity());
						} else {
							ps.setNull(8, Types.BIGINT);
						}
						if (sale.getUserEventId() != null) {
							ps.setLong(9, sale.getUserEventId());
						} else {
							ps.setNull(9, Types.BIGINT);
						}
					}

					@Override
					public int getBatchSize() {
						return saleData.size();
					}
				});
	}

}
