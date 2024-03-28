package com.base.api.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.api.entities.Template;
import com.base.api.entities.TemplateTranslable;
import com.base.api.exception.APIException;
import com.base.api.repository.TemplateRepository;
import com.base.api.repository.TemplateTranslableRepository;
import com.base.api.request.dto.TemplateDTO;
import com.base.api.request.dto.TemplateTranslableDTO;
import com.base.api.service.TemplateService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TemplateServiceImpl implements TemplateService {

	@Autowired
	TemplateRepository templateRepository;

	@Autowired
	TemplateTranslableRepository templateTranslableRepository;

	@Override
	public Template create(TemplateDTO templateDTO) {
		
		log.info("TemplateServiceImpl : create");
		Template Template = createOrUpdate(templateDTO, null);
		Template.setStatus("Active");
		Template.setCreatedDate(LocalDateTime.now());
		return templateRepository.save(Template);
	
	}
	
	@Override
	public List<TemplateDTO> getAllTemplates() {
		
		log.info("TemplateServiceImpl : getAllTemplates");
		List<Template> templates = new ArrayList<Template>();
		templates = templateRepository.findAll();
		List<TemplateDTO> templateDTOs = new ArrayList<TemplateDTO>();
		for (Template template : templates) {
			TemplateDTO dto = getTemplate(template, "list");
			templateDTOs.add(dto);
		}
		return templateDTOs;
	}

	private TemplateDTO getTemplate(Template template, String operation) {
		List<TemplateTranslable> translationEntities = new ArrayList<TemplateTranslable>();
		List<TemplateTranslableDTO> templateTranslableDTOs = new ArrayList<TemplateTranslableDTO>();
		if (operation.equalsIgnoreCase("get")) {
			for (TemplateTranslable translableEntity : template.getTemplateTranslable()) {
				TemplateTranslableDTO templateTranslableDTO = get(translableEntity);
				templateTranslableDTOs.add(templateTranslableDTO);
			}
		} else if (operation.equalsIgnoreCase("list")) {
			translationEntities = template.getTemplateTranslable().stream().filter(p -> p.locale.equals("en"))
					.collect(Collectors.toList());
			TemplateTranslableDTO templateTranslableDTO = get(template.getTemplateTranslable().get(0));
			templateTranslableDTOs.add(templateTranslableDTO);
		}
		TemplateDTO dto = new TemplateDTO();
		dto.setStatus(template.getStatus());
		dto.setTemplateId(template.getId());
		dto.setTemplatePurpose(template.getTemplatePurpose());
		dto.setTemplateType(template.getTemplateType());
		//dto.setCreatedDate(template.getCreatedDate());
		dto.setTemplate(templateTranslableDTOs);
		return dto;
	}

	private TemplateTranslableDTO get(TemplateTranslable translationEntities) {
		TemplateTranslableDTO templateTranslableDTO = new TemplateTranslableDTO();
		templateTranslableDTO.setEmailBody(translationEntities.getEmailBody());
		templateTranslableDTO.setEmailKey(translationEntities.getEmailKey());
		templateTranslableDTO.setEmailSubject(translationEntities.getEmailSubject());
		templateTranslableDTO.setLocale(translationEntities.getLocale());
		return templateTranslableDTO;
	}


//	@Override
//	public List<TemplateDTO> getAllTemplates() {
//		List<Template> templates = templateRepository.findAll();
//		List<TemplateDTO> templateDTOs = new ArrayList<TemplateDTO>();
//		for (Template template : templates) {
//			templateDTOs.add(new TemplateDTO(template));
//		
//		}
//		if (templates != null) {
//			return templateDTOs;
//		}
//		throw new APIException("api.success.getalltemplates");
//	}

	@Override
	public TemplateDTO getTemplateById(UUID id) {
		
		log.info("TemplateServiceImpl : getTemplateById");
		Optional<Template> entity = templateRepository.findById(id);
		if (entity.isPresent()) {
			return new TemplateDTO(entity.get());
		} else {
		     throw new APIException("api.error.gettemplatebyid");
		}
	}

	@Override
	public Template update(TemplateDTO templateDTO, UUID id) {
		
		log.info("TemplateServiceImpl : update");
		Optional<Template> entity = templateRepository.findById(id);
		
		if (!entity.isPresent()) {
			throw new APIException("api.error.record.not.found");
			}
		
		Template template = createOrUpdate(templateDTO, entity.get());
		
		return templateRepository.save(template);	
	}

	private Template createOrUpdate(TemplateDTO templateDTO, Template Template) {
		
		log.info("TemplateServiceImpl : createOrUpdate");

		if (Template == null) {
			Template = new Template();
		}
		Template.setTemplatePurpose(templateDTO.getTemplatePurpose());
		Template.setTemplateType(templateDTO.getTemplateType());

		List<TemplateTranslable> templateTranslableEntities = new ArrayList<TemplateTranslable>();
		for (TemplateTranslableDTO template : templateDTO.getTemplate()) {
			templateTranslableEntities.add(new TemplateTranslable(template));
		}

		Template.setTemplateTranslable(templateTranslableEntities);
		return Template;
	}

	@Override
	public Template changeStatus(UUID id, String status) {
		
		log.info("TemplateServiceImpl : changeStatus");
		Optional<Template> template = templateRepository.findById(id);
		if (!template.isPresent()) {
			throw new APIException("api.error.record.not.found");
		}
		template.get().setStatus(status);
			return templateRepository.save(template.get());
		}
	}

