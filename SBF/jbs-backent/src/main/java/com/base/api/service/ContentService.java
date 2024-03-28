package com.base.api.service;

import java.util.List;

import javax.validation.Valid;

import com.base.api.entities.ContentManagement;
import com.base.api.request.dto.ContentManagementDTO;
import com.base.api.request.dto.ContentPageLoadDTO;
import com.base.api.response.dto.ContentListDTO;

public interface ContentService {

	String create(@Valid ContentManagementDTO contentManagement);

	List<ContentListDTO> getContents();

	ContentManagement getContentById(String pageId);

	String update(@Valid ContentManagementDTO contentManagement, String pageId);

	ContentPageLoadDTO pageLoad(String pageId);

	String addContent(@Valid ContentPageLoadDTO contentManagement, String pageId);

	String changeStatus(String id, String status);
}
