package com.base.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.base.api.dto.filter.FilterBase;
import com.base.api.dto.filter.ReviewFilter;
import com.base.api.entities.Report;
import com.base.api.exception.APIException;
import com.base.api.exception.NoReportFound;
import com.base.api.gateway.util.Util;
import com.base.api.repository.ReportRepository;
import com.base.api.service.ReportService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author preyansh_prajapati
 *
 */
@Service(value = "reportService")
@Slf4j
public class ReportServiceImpl implements ReportService {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private ReportRepository reportRepository;

	@Override
	public List<Report> getAllReports() {
		log.info("ReportServiceImpl : getAllReports");

		List<Report> reportList = reportRepository.findAll();

		if (reportList.isEmpty()) {
			log.error("No report found in the Database");
			throw new NoReportFound("no.report.found.in.db");
		}

		return reportList;
	}

	/**
	 * This method will change the current status
	 * 
	 * @return
	 */
	@Override
	public Report changeStatus(Map<String, String> statusReq) {
		log.info("ReportServiceImpl : change status");

		Report report = reportRepository.findById(UUID.fromString(statusReq.get("reportId"))).get();
		if (report != null) {
			report.setStatus(statusReq.get("status"));
			log.info("ReportServiceImpl : report status changed successfully");
			return reportRepository.save(report);
		} else {
			log.error("Getting issue while changing report status");
			throw new APIException("failed.to.chnage.report.status");
		}
	}

	@Override
	public String deleteReport(UUID reportId) {
		log.info("ReportServiceImpl : deleteReport");

		Report reportEntity = reportRepository.findById(reportId).get();

		if (reportEntity != null) {
			reportRepository.delete(reportEntity);
			return HttpStatus.OK.name();
		}

		return HttpStatus.NOT_FOUND.name();
	}

	/**
	 * @param reportId
	 * @return
	 * 
	 *         This is the common method to find is report of certain reportId is
	 *         available in database or not
	 */
	private Report getReport(UUID reportId) {

		Optional<Report> reportOption = reportRepository.findById(reportId);

		if (reportOption.isEmpty()) {
			log.error("No report found with report id : " + reportId);
			throw new NoReportFound("no.report.found.in.db" + " : " + reportId);
		}
		return reportOption.get();
	}

	@Override
	public List<Report> getReports(ReviewFilter filter) {
		StringBuilder query = new StringBuilder();
		query = createCommonQuery();

		if (filter.getSearch_keyword() != null && !filter.getSearch_keyword().isEmpty()) {
			query.append(" where (LOWER(upe1.firstName) LIKE '%" + filter.getSearch_keyword().toLowerCase()
					+ "%' or LOWER(upe1.lastName) LIKE '%" + filter.getSearch_keyword().toLowerCase()
					+ "%' or LOWER(upe2.firstName) LIKE '%" + filter.getSearch_keyword().toLowerCase()
					+ "%' or LOWER(upe2.lastName) LIKE '%" + filter.getSearch_keyword().toLowerCase() + "%')");
		}

		String columnName = "u." + filter.getSort_param();
		if (filter.getSort_param().startsWith("from")) {
			columnName = "u.reportedBy";
		} else if (filter.getSort_param().startsWith("to")) {
			columnName = "u.username";
		}

		FilterBase filterBase = new FilterBase();
		filterBase.setColumnName(columnName);
		filterBase.setStartRec(filter.getStartRec());
		filterBase.setEndRec(filter.getEndRec());
		filterBase.setSortingOrder(filter.getSort_type());
		filterBase.setOrder(filter.getOrder());
		String queryParam = Util.getFilterQuery(filterBase, query.toString());
		List<Report> results = new ArrayList<Report>();
		results = entityManager.createQuery(queryParam).setFirstResult(Integer.valueOf(filter.getStartRec()))
				.setMaxResults(Integer.valueOf(filter.getEndRec())).getResultList();
		return results;
	}

	private StringBuilder createCommonQuery() {
		StringBuilder query = new StringBuilder();
		query.append(
				"select u from Report u join u.username u1 join u.reportedBy u2 join u1.userProfile upe1 join u2.userProfile upe2 ");
		return query;
	}

}
