package com.base.api.service;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import com.base.api.entities.Template;
import com.base.api.request.dto.TemplateDTO;


public interface TemplateService {

	Template create(TemplateDTO templateDTO);

	List<TemplateDTO> getAllTemplates();

	TemplateDTO getTemplateById(UUID id);

	Template update(@Valid TemplateDTO templateDTO,UUID id);

	Template changeStatus(UUID id, String status);

}
