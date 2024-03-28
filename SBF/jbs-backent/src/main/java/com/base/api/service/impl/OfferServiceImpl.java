package com.base.api.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.api.dto.filter.FilterBase;
import com.base.api.dto.filter.OfferFilter;
import com.base.api.entities.Offer;
import com.base.api.entities.OfferUser;
import com.base.api.entities.User;
import com.base.api.exception.APIException;
import com.base.api.exception.DataNotFoundException;
import com.base.api.gateway.util.Util;
import com.base.api.repository.OfferRepository;
import com.base.api.repository.OfferUserRepository;
import com.base.api.repository.UserRepository;
import com.base.api.request.dto.OfferDTO;
import com.base.api.service.OfferService;

import io.jsonwebtoken.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kavitha_deshagani
 *
 */
@Slf4j
@Service
public class OfferServiceImpl implements OfferService {

	@Autowired
	OfferRepository offerRepository;

	@Autowired
	OfferUserRepository offerUserRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	EntityManager entityManager;

	/**
	 *This implementation is used to create the offer.
	 */
	@Transactional
	@Override
	public String createOffer(OfferFilter offerFilter) {
		
		log.info("OfferServiceImpl : createOffer");
		log.info("OfferFilter : " + offerFilter.toString());
		
		Offer offer = new Offer();
		offer.setName(offerFilter.getName());
		offer.setCode(offerFilter.getCode());
		offer.setStatus("Active");
		offer.setType(offerFilter.getType());
		offer.setApplicable(offerFilter.getApplicable());
		offer.setUsage(offerFilter.getUsage());
		offer.setValue(offerFilter.getValue());
		offer.setStartDate(offerFilter.getStart_date());
		offer.setEndDate(offerFilter.getEnd_date());

		offer = offerRepository.save(offer);
		
			for(String userId : offerFilter.getUsers().split(",")) {
				
				if(	userId != null && !userId.isEmpty()) {
		
					User user = userRepository.findById(UUID.fromString(userId)).get();
					
					if(user != null) {
						OfferUser offerUser = new OfferUser();
						offerUser.setOffer(offer);
						offerUser.setUser(user);
						offerUserRepository.save(offerUser);
					}
					else
					 throw new APIException("failed.to.get.user");
				}
				else {
					log.error("some thing went wrong while creating offer");
					throw new APIException("failed.to.create.offer");
				}
			}
		return "offer created";
	}

	/**
	 * This implementation is used to to get the all offers.
	 */
	@Override
	public List<Offer> getAllOffers(OfferFilter filter) throws ParseException {

		StringBuilder query = new StringBuilder();
		query = createCommonQuery();

		if (filter.getName() != null && !filter.getName().isEmpty()) {
			query.append(" LOWER(u.name) LIKE '%" + filter.getName().toLowerCase() + "%' ");
		}
		if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");
			query.append(" LOWER(u.status) LIKE '" + filter.getStatus().toLowerCase() + "'");
		}
		if (filter.getStart_date() != null && !filter.getStart_date().isEmpty() && filter.getEnd_date() != null
				&& !filter.getEnd_date().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Date fromDate = sdf.parse(filter.getStart_date());
			Date toDate = sdf.parse(filter.getEnd_date());

			query.append(" TO_DATE(u.startDate, 'YYYY-MM-DD') >= '" + fromDate
					+ "' and TO_DATE(u.endDate, 'YYYY-MM-DD') <= '" + toDate + "'");
		}

		if (query.indexOf("where") == (query.length() - 5))
			query = new StringBuilder(query.substring(0, query.indexOf("where")));

		FilterBase filterBase = new FilterBase();
		filterBase.setColumnName(filter.getSort_param());
		filterBase.setStartRec(filter.getStartRec());
		filterBase.setEndRec(filter.getEndRec());
		filterBase.setSortingOrder(filter.getSort_type());
		filterBase.setOrder(filter.getOrder());
		String queryParam = Util.getFilterQuery(filterBase, query.toString());
		List<Offer> results = new ArrayList<Offer>();
		results = entityManager.createQuery(queryParam).setFirstResult(Integer.valueOf(filter.getStartRec()))
				.setMaxResults(Integer.valueOf(filter.getEndRec())).getResultList();
		return results;
	}

	private StringBuilder createCommonQuery() {
		StringBuilder query = new StringBuilder();
		query.append("select u from Offer u where");
		return query;
	}

	/**
	 * This implementation is used to to get the offer by id.
	 */
	@Override
	public OfferDTO getOffer(UUID offerId) {
		log.info("OfferServiceImpl : getOffer");

		OfferDTO offerDTO = new OfferDTO();
		Offer offer = offerRepository.findById(offerId).get();
		offerDTO.setOffer(offer);
		List<User> users = new ArrayList<User>();
		for (OfferUser offerUser : offerUserRepository.findByOffer(offer)) {
			users.add(offerUser.getUser());
		}

		offerDTO.setUsers(users);

		return offerDTO;
	}

	/**
	 * This implementation is used to update the offer.
	 */
	@Transactional
	@Override
	public String updateOffer(UUID offerId, OfferFilter offerFilter) {

		log.info("OfferServiceImpl : updateOffer");

		Offer offer = offerRepository.findById(offerId).get();

		if (offer != null) {
			offer.setName(offerFilter.getName());
			offer.setCode(offerFilter.getCode());
			offer.setType(offerFilter.getType());
			offer.setApplicable(offerFilter.getApplicable());
			offer.setUsage(offerFilter.getUsage());
			offer.setValue(offerFilter.getValue());
			offer.setStartDate(offerFilter.getStart_date());
			offer.setEndDate(offerFilter.getEnd_date());

			offer = offerRepository.save(offer);

			deleteOfferUserRecords(offer);

			for (String userId : offerFilter.getUsers().split(",")) {

				if (userId != null && !userId.isEmpty()) {
					User userEntity = userRepository.findById(UUID.fromString(userId)).get();

					if (userEntity != null) {
						OfferUser offerUser = new OfferUser();
						offerUser.setOffer(offer);
						offerUser.setUser(userEntity);

						offerUserRepository.save(offerUser);
					}
				}
			}
			log.info("offer updated successfully...");
			return "offerUpdated";
		} else {
			throw new DataNotFoundException("invalid.id.was.provided..!");
		}
	}

	/**
	 * This implementation is used to delete the offer.
	 */
	@Override
	public String deleteOffer(UUID offerId){

		log.info("OfferServiceImpl : deleteOffer");
		Offer offer = offerRepository.findById(offerId).get();

		if (null != offer) {
			deleteOfferUserRecords(offer);
			offerRepository.delete(offer);
			log.info("offer deleted successfully..!");
			return "offer deleted";
		} else {
			log.error("No such offer is found with id :" + offerId);
			throw new DataNotFoundException("offer.not.found");
		}
	}

	private void deleteOfferUserRecords(Offer offer) {
		List<OfferUser> offerUsers = offerUserRepository.findByOffer(offer);
		for (OfferUser offerUser : offerUsers) {
			offerUserRepository.delete(offerUser);
		}
	}

	/**
	 * This method will change the current status
	 */
	@Override
	public void changeStatus(Map<String, String> statusReq) {
		log.info("OfferServiceImpl : change status");

		Offer offer = offerRepository.findById(UUID.fromString(statusReq.get("offer_id"))).get();
		if (offer != null) {
			offer.setStatus(statusReq.get("status"));
			offerRepository.save(offer);
			log.info("OfferServiceImpl : offer status changed successfully");
		} else {
			log.error("Getting issue while changing offer status");
			throw new APIException("failed.to.chnage.offer.status");
		}
	}

	@Override
	public ByteArrayInputStream writeExcel(List<Offer> offers, String filename) {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Offers");

			Row row = sheet.createRow(0);
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());

			Font font = workbook.createFont();
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			headerCellStyle.setFont(font);

			// Creating header
			Cell cell = row.createCell(0);
			cell.setCellValue("Sr. No.");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(1);
			cell.setCellValue("Offer Name");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(2);
			cell.setCellValue("Code");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(3);
			cell.setCellValue("Value");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(4);
			cell.setCellValue("Type");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(5);
			cell.setCellValue("Usage");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(6);
			cell.setCellValue("Total Usage");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(7);
			cell.setCellValue("Offer Start Date");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(8);
			cell.setCellValue("Offer End Date");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(9);
			cell.setCellValue("Applicable To");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(10);
			cell.setCellValue("Status");
			cell.setCellStyle(headerCellStyle);

			// Creating data rows for each customer
			for (int i = 0; i < offers.size(); i++) {
				Row dataRow = sheet.createRow(i + 1);
				dataRow.createCell(0).setCellValue(i + 1);
				dataRow.createCell(1).setCellValue(offers.get(i).getName());
				dataRow.createCell(2).setCellValue(offers.get(i).getCode());
				dataRow.createCell(3).setCellValue(offers.get(i).getValue());
				dataRow.createCell(4).setCellValue(offers.get(i).getType().equals("0") ? "Percentage" : "Amount");
				dataRow.createCell(5)
						.setCellValue(offers.get(i).getUsage().equals("1") ? "One Time" : "Multiple Times");
				dataRow.createCell(6).setCellValue("");
				dataRow.createCell(7).setCellValue(offers.get(i).getStartDate());
				dataRow.createCell(8).setCellValue(offers.get(i).getEndDate());
				dataRow.createCell(9)
						.setCellValue(offers.get(i).getApplicable().equals("0") ? "All Users" : "Selected Users");
				dataRow.createCell(10).setCellValue(offers.get(i).getStatus());
			}

			// Making size of column auto resize to fit with data
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
			sheet.autoSizeColumn(9);
			sheet.autoSizeColumn(10);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		} catch (IOException | java.io.IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public InputStream load(List<Offer> offers) {
		ByteArrayInputStream in = exportCSV(offers);
		return in;
	}

	public ByteArrayInputStream exportCSV(List<Offer> offers) {
		final CSVFormat format = CSVFormat.DEFAULT.withHeader("Sr No.", "Offer Name", "Code", "Value", "Type", "Usage",
				"Total Usage", "Offer Start Date", "Offer End Date", "Applicable To", "Status");
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
			int srNo = 1;
			for (Offer entity : offers) {
				List<String> data = Arrays.asList(String.valueOf(srNo), entity.getName(), entity.getCode(),
						String.valueOf(entity.getValue()), entity.getType().equals("0") ? "Percentage" : "Amount",
						entity.getUsage().equals("1") ? "One Time" : "Multiple Times", "", entity.getStartDate(),
						entity.getEndDate(), entity.getApplicable().equals("0") ? "All Users" : "Selected Users",
						entity.getStatus());
				csvPrinter.printRecord(data);
				srNo++;
			}
			csvPrinter.flush();
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
