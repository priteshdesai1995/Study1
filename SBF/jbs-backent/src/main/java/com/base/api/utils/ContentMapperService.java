package com.base.api.utils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.base.api.entities.ContentManagement;
import com.base.api.entities.Translation;
import com.base.api.request.dto.ContentManagementDTO;
import com.base.api.request.dto.TranslationDTO;
import com.base.api.response.dto.ContentListDTO;

@Component
public class ContentMapperService implements Serializable{

	private static final long serialVersionUID = 290572404730995977L;
	
	public List<Translation> mapTranslationEntity(List<TranslationDTO> translationDTOs, String operationType) {
		
		List<Translation> translationEntities = new ArrayList<Translation>();
		for (int i = 0; i < translationDTOs.size(); i++) { 
			Translation entity = new Translation();
			if (operationType.equals("create")) {
				entity.setCreatedDate(LocalDateTime.now());
			}
			if (operationType.equals("update")) {
				entity.setId(UUID.fromString(translationDTOs.get(i).getId()));
			}
			entity.setDescription(translationDTOs.get(i).getDescription());
			entity.setLocale(translationDTOs.get(i).getLocale());
			entity.setMetaDescription(translationDTOs.get(i).getMetaDescription());
			entity.setMetaKeywords(translationDTOs.get(i).getMetaKeywords());
			entity.setPageTitle(translationDTOs.get(i).getPageTitle());
			translationEntities.add(entity);
		}
		
		return translationEntities;
	}
	
	public ContentManagement mapContentFromDTO(ContentManagementDTO contentManagementDTO, String operationType) { 
		
		ContentManagement contentManagement = new ContentManagement();
		if (operationType.equals("create")) { 
			contentManagement.setPageType("cms");
			List<TranslationDTO> translationEntities = contentManagementDTO.getTranslations().stream()
					.filter(p -> p.locale.equals("en")).collect(Collectors.toList());
			
			String s[] = translationEntities.get(0).getPageTitle().toLowerCase().split(" ");
			for (int i = 0; i < s.length; i++) {
				if (s[i].equals("-")) {
					s[i] = null;
				}
			}
			String slug = StringUtils.join(s, "-");
			contentManagement.setSlug(slug);
			contentManagement.setCreatedDate(LocalDateTime.now());
		}
		if (operationType.equals("update")) {
			contentManagement.setId(UUID.fromString(contentManagementDTO.getCmsId()));
		}
		contentManagement.setStatus(contentManagementDTO.getStatus());
		return contentManagement;
	}
	
	public ContentListDTO mapContentFromDTO(ContentManagement contentManagement) {
		System.out.println(contentManagement.getTranslations());
		List<Translation> translationEntities = new ArrayList<Translation>();
		translationEntities = contentManagement.getTranslations().stream().filter(p -> p.locale.equals("en"))
				.collect(Collectors.toList());
		ContentListDTO contentListDTO = new ContentListDTO();
		contentListDTO.setAssets(contentManagement.getAssets());
		contentListDTO.setCms_page_name(contentManagement.getCmsPageName());
		contentListDTO.setComponent(contentManagement.getComponent());
		contentListDTO.setCss(contentManagement.getCss());
		contentListDTO.setDescription(translationEntities.get(0).getDescription());
		contentListDTO.setHtml(contentManagement.getHtml());
		contentListDTO.setId(contentManagement.getId().toString());
		contentListDTO.setLocale(translationEntities.get(0).getLocale());
		contentListDTO.setMeta_description(translationEntities.get(0).getMetaDescription());
		contentListDTO.setMeta_keywords(translationEntities.get(0).getMetaKeywords());
		contentListDTO.setPage_title(translationEntities.get(0).getPageTitle());
		contentListDTO.setPage_type(contentManagement.getPageType());
		contentListDTO.setSlug(contentManagement.getSlug());
		contentListDTO.setStatus(contentManagement.getStatus());
		contentListDTO.setStyles(contentManagement.getStyles());
		return contentListDTO;
	}
	
	
}
