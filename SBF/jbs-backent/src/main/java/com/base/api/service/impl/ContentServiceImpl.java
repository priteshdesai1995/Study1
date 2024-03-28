package com.base.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.base.api.entities.ContentManagement;
import com.base.api.entities.Translation;
import com.base.api.repository.ContentManagementRepository;
import com.base.api.request.dto.ContentManagementDTO;
import com.base.api.request.dto.ContentPageLoadDTO;
import com.base.api.response.dto.ContentListDTO;
import com.base.api.service.ContentService;
import com.base.api.utils.ContentMapperService;

@Service(value = "contentService")
public class ContentServiceImpl implements ContentService {

	@Autowired
	private ContentMapperService mapperService;

	@Autowired
	ContentManagementRepository contentManagementRepository;

	private static final String CREATE_ACTION = "create";

	private static final String UPDATE_ACTION = "update";

	@Override
	public String create(@Valid ContentManagementDTO contentManagementDTO) {

		try {
			List<Translation> translations = mapperService
					.mapTranslationEntity(contentManagementDTO.getTranslations(), CREATE_ACTION);
			ContentManagement contentManagement = mapperService.mapContentFromDTO(contentManagementDTO, CREATE_ACTION);

			for (int i = 0; i < translations.size(); i++) {
				translations.get(i).setContentManagement(contentManagement);
			}
			contentManagement.setTranslations(translations);
			contentManagement.setStatus(contentManagementDTO.getStatus());
			contentManagementRepository.save(contentManagement);
			return HttpStatus.OK.name();
		} catch (DataIntegrityViolationException e) {
			return HttpStatus.INTERNAL_SERVER_ERROR.name();
		}
	}

	@Override
	public List<ContentListDTO> getContents() {

		List<ContentManagement> contentPages = new ArrayList<ContentManagement>();
		contentPages = contentManagementRepository.findAll();
		List<ContentListDTO> contentListDTOs = new ArrayList<ContentListDTO>();

		for (ContentManagement contentManagement : contentPages) {
			ContentListDTO contentListDTO = new ContentListDTO();
			contentListDTO = mapperService.mapContentFromDTO(contentManagement);
			contentListDTOs.add(contentListDTO);
		}
		return contentListDTOs;
	}

	@Override
	public ContentManagement getContentById(String pageId) {
		Optional<ContentManagement> content = contentManagementRepository.findById(UUID.fromString(pageId));
		if (content != null && content.isPresent()) {
			return content.get();
		} else {
			return null;
		}
	}

	@Override
	public String update(@Valid ContentManagementDTO contentManagementDTO, String pageId) {
		try {
			ContentManagement content = contentManagementRepository.findById(UUID.fromString(pageId)).get();
			if (content != null) {
				contentManagementDTO.setCmsId(pageId);
				List<Translation> translations = mapperService
						.mapTranslationEntity(contentManagementDTO.getTranslations(), UPDATE_ACTION);
				ContentManagement contentManagement = mapperService.mapContentFromDTO(contentManagementDTO,
						UPDATE_ACTION);
				for (int i = 0; i < translations.size(); i++) {
					translations.get(i).setContentManagement(contentManagement);
				}
				contentManagement.setTranslations(translations);
				contentManagementRepository.save(contentManagement);
				return HttpStatus.OK.name();
			}
			return HttpStatus.NOT_FOUND.name();
		} catch (DataIntegrityViolationException e) {
			return HttpStatus.INTERNAL_SERVER_ERROR.name();
		}
	}

	@Override
	public ContentPageLoadDTO pageLoad(String pageId) {
		ContentManagement contentManagement = contentManagementRepository.findById(UUID.fromString(pageId)).get();
		if (contentManagement != null) {
			ContentPageLoadDTO contentPageLoadDTO = new ContentPageLoadDTO();
			contentPageLoadDTO.setCmsPageName(contentManagement.getCmsPageName());
			contentPageLoadDTO.setGjsAssets(contentManagement.getAssets());
			contentPageLoadDTO.setGjsComponents(contentManagement.getComponent());
			contentPageLoadDTO.setGjsCSS(contentManagement.getCss());
			contentPageLoadDTO.setGjsHtml(contentManagement.getHtml());
			contentPageLoadDTO.setGjsStyles(contentManagement.getStyles());
			return contentPageLoadDTO;
		}
		return null;

	}

	@Override
	public String addContent(@Valid ContentPageLoadDTO contentLoadDTO, String pageId) {

		try {
			ContentManagement contentManagement = contentManagementRepository.findById(UUID.fromString(pageId)).get();
			if (contentManagement != null) {
				contentManagement.setAssets(contentLoadDTO.getGjsAssets());
				contentManagement.setCmsPageName(contentLoadDTO.getCmsPageName());
				contentManagement.setComponent(contentLoadDTO.getGjsComponents());
				contentManagement.setCss(contentLoadDTO.getGjsCSS());
				contentManagement.setHtml(contentLoadDTO.getGjsHtml());
				contentManagement.setStyles(contentLoadDTO.getGjsStyles());
				contentManagementRepository.save(contentManagement);
				return HttpStatus.OK.name();
			}

			return HttpStatus.NOT_FOUND.name();
		} catch (DataIntegrityViolationException e) {
			return HttpStatus.INTERNAL_SERVER_ERROR.name();
		}
	}

	@Override
	public String changeStatus(String id, String status) {
		Optional<ContentManagement> contentManagement = contentManagementRepository.findById(UUID.fromString(id));

		if (contentManagement.get() != null) {
			contentManagement.get().setStatus(status);
			contentManagementRepository.save(contentManagement.get());
			return HttpStatus.OK.name();
		}

		return HttpStatus.NOT_FOUND.name();
	}

}
