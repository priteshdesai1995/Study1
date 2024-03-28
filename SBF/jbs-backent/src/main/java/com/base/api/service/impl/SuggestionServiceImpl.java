package com.base.api.service.impl;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.base.api.dto.filter.FilterBase;
import com.base.api.dto.filter.SuggestionFilter;
import com.base.api.entities.Suggestion;
import com.base.api.entities.User;
import com.base.api.gateway.util.Util;
import com.base.api.repository.SuggestionRepository;
import com.base.api.request.dto.SuggestionDTO;
import com.base.api.request.dto.SuggestionRequest;
import com.base.api.service.SuggestionService;
import com.base.api.service.UserService;

import io.jsonwebtoken.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SuggestionServiceImpl implements SuggestionService {

	Logger logger = Logger.getLogger("SuggestionServiceImpl.class");
	@Autowired
	EntityManager entityManager;

	@Autowired
	SuggestionRepository suggestionRepository;

	@Autowired
	UserService userService;

	@SuppressWarnings("unchecked")
	@Override
	public List<SuggestionDTO> getAllSuggestions(SuggestionFilter filter) {
		StringBuilder query = new StringBuilder();
		query = createCommonQuery();

		if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
			query.append(" u.status LIKE '" + filter.getStatus() + "' ");
		}
		if (filter.getCategory_name() != null && !filter.getCategory_name().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");
			query.append(" u.category LIKE '%" + filter.getCategory_name() + "%'");
		}
		if (filter.getSuggestion() != null && !filter.getSuggestion().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");
			query.append(" u.suggestion LIKE '%" + filter.getSuggestion() + "%'");
		}
		if (filter.getPosted_name() != null && !filter.getPosted_name().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");
			// query.append(" co.userName LIKE '%" + filter.getPosted_name() + "%'");
			query.append(" (upe.firstName LIKE '%" + filter.getPosted_name() + "%' or  upe.lastName LIKE '%"
					+ filter.getPosted_name() + "%')");
		}

		if (query.indexOf("where") == (query.length() - 5))
			query = new StringBuilder(query.substring(0, query.indexOf("where")));

		FilterBase filterBase = new FilterBase();
		filterBase.setColumnName(filter.getSort_param());
		filterBase.setStartRec(filter.getStartRec());
		filterBase.setEndRec(filter.getEndRec());
		filterBase.setSortingOrder(filter.getSort_type());
		filterBase.setOrder(filter.getSort_type());
		String queryParam = Util.getFilterQueryForSuggestion(filterBase, query.toString());
		List<Suggestion> results = new ArrayList<Suggestion>();
		results = entityManager.createQuery(queryParam).setFirstResult(Integer.valueOf(filter.getStartRec()))
				.setMaxResults(Integer.valueOf(filter.getEndRec())).getResultList();
		List<SuggestionDTO> list = new ArrayList<SuggestionDTO>();
		for (Suggestion suggestionEntity : results) {
			SuggestionDTO suggestionDTO = new SuggestionDTO();
			suggestionDTO = this.mapEntityToDTO(suggestionEntity);
			list.add(suggestionDTO);
		}
		return list;
	}

	@Override
	public ByteArrayInputStream writeExcel(List<Suggestion> suggestionDetails, String filename) {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Suggestions");

			Row row = sheet.createRow(0);
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());

			// Creating header
			Cell cell = row.createCell(0);
			cell.setCellValue("Sr.No");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(1);
			cell.setCellValue("Category");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(2);
			cell.setCellValue("Suggestion");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(3);
			cell.setCellValue("Posted By");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(4);
			cell.setCellValue("Posted At");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(5);
			cell.setCellValue("Status");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(6);
			cell.setCellValue("Notes");
			cell.setCellStyle(headerCellStyle);

			DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");

			// Creating data rows for each customer
			for (int i = 0; i < suggestionDetails.size(); i++) {
				Row dataRow = sheet.createRow(i + 1);
				dataRow.createCell(0).setCellValue(suggestionDetails.get(i).getId().toString());
				dataRow.createCell(1).setCellValue(suggestionDetails.get(i).getCategory());
				dataRow.createCell(2).setCellValue(suggestionDetails.get(i).getSuggestion());
				dataRow.createCell(3).setCellValue(suggestionDetails.get(i).getUser().getUserProfile().getFirstName()
						+ " " + suggestionDetails.get(i).getUser().getUserProfile().getLastName());
				dataRow.createCell(4).setCellValue(sdf.format(suggestionDetails.get(i).getCreatedDate()));
				dataRow.createCell(5).setCellValue(suggestionDetails.get(i).getStatus());
				dataRow.createCell(6).setCellValue(suggestionDetails.get(i).getNotes());
			}

			// Making size of column auto resize to fit with data
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		} catch (IOException | java.io.IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public ByteArrayInputStream load(List<Suggestion> suggestionDetails) {
		ByteArrayInputStream in = exportCSV(suggestionDetails);
		return in;
	}

	public ByteArrayInputStream exportCSV(List<Suggestion> suggestionDetails) {
		final CSVFormat format = CSVFormat.DEFAULT.withHeader("Sr No", "Category", "Suggestion", "Posted By",
				"Posted At", "Status", "Notes");
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
			DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
			for (Suggestion entity : suggestionDetails) {
				String createdAt = sdf.format(entity.getCreatedDate());
				List<String> data = Arrays.asList(String.valueOf(entity.getId()), entity.getCategory(),
						entity.getSuggestion(),
						entity.getUser().getUserProfile().getFirstName() + " "
								+ entity.getUser().getUserProfile().getLastName(),
						createdAt, entity.getStatus(), entity.getNotes());
				csvPrinter.printRecord(data);
			}
			csvPrinter.flush();
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private SuggestionDTO mapEntityToDTO(Suggestion entity) {
		SuggestionDTO suggestionDTO = new SuggestionDTO();
		suggestionDTO.setSuggestionId(entity.getId());
		suggestionDTO.setCreatedDate(entity.getCreatedDate());
		suggestionDTO.setCreatedBy(entity.getUser().getUserProfile().getFirstName() + " "
				+ entity.getUser().getUserProfile().getLastName());
		suggestionDTO.setCategory(entity.getCategory());
		suggestionDTO.setStatus(entity.getStatus());
		suggestionDTO.setSuggestion(entity.getSuggestion());
		suggestionDTO.setUpdatedDate(entity.getUpdatedDate());
		return suggestionDTO;
	}

	private StringBuilder createCommonQuery() {
		StringBuilder query = new StringBuilder();
		query.append("select u from Suggestion u join u.user co join co.userProfile upe where");
		return query;
	}

	@Override
	public Suggestion findBySuggestionId(UUID suggestionId, Map<String, String> statusReq) {
		Suggestion suggestionEntity = suggestionRepository.findBySuggestionId(suggestionId);
		try {
			if (suggestionEntity != null) {
				suggestionEntity.setStatus(statusReq.get("status"));
				suggestionEntity.setNotes(statusReq.get("notes"));
				Suggestion updateSuggeEntity = suggestionRepository.save(suggestionEntity);
				return updateSuggeEntity;
			}

		} catch (Exception exception) {
			logger.info(exception.getMessage());
		}
		return suggestionEntity;
	}

	@Override
	public Suggestion addSuggestions(SuggestionRequest suggestionRequest) {
		Suggestion suggestion = new Suggestion();
		suggestion.setStatus("Accepted");
		suggestion.setCategory(suggestionRequest.getCategory());
		suggestion.setNotes(suggestionRequest.getInformation());

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = null;
		if (principal instanceof UserDetails) {
			String username = ((UserDetails) principal).getUsername();
			user = userService.findByUserName(username);
		} else {
			String username = principal.toString();
			user = userService.findByUserName(username);
		}
		
		if (suggestionRequest.getPost_anonymously() == 1) {
			suggestion.setUser(user);
		}

		return suggestionRepository.save(suggestion);
	}
}
