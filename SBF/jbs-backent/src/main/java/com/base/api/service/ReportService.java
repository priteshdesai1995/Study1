package com.base.api.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.base.api.dto.filter.ReviewFilter;
import com.base.api.entities.Report;

/**
 * @author preyansh_prajapati
 *
 */
public interface ReportService {

	public List<Report> getAllReports();

	public Report changeStatus(Map<String, String> statusReq);

	public String deleteReport(UUID reportId);
	
	public List<Report> getReports(ReviewFilter reviewFilter);
	
}
