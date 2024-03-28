package com.base.api.request.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.base.api.entities.Template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TemplateDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 2452012612914971716L;
	
	private UUID templateId;
	private String status;
	private String templatePurpose;
	private String templateType;
	private List<TemplateTranslableDTO> template;
	
	public TemplateDTO(Template template) {
		this.setTemplateId(template.getId());
		this.setStatus(template.getStatus());
		this.setTemplatePurpose(template.getTemplatePurpose());
		this.setTemplateType(template.getTemplateType());
	}
	
}
