package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.base.api.request.dto.TemplateTranslableDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "template_translable")
@AttributeOverride(name = "id", column = @Column(name = "template_translable_id"))
public class TemplateTranslable extends BaseEntity{

	private static final long serialVersionUID = -4907422643042047012L;

	@Column(name = "email_body")
	private String emailBody;

	@Column(name = "email_key")
	private String emailKey;

	@Column(name = "email_subject")
	private String emailSubject;

	@Column(name = "locale")
	public String locale;

	public TemplateTranslable(TemplateTranslableDTO dto) {
		this.emailBody = dto.getEmailBody();
		this.emailKey = dto.getEmailKey();
		this.emailSubject = dto.getEmailSubject();
		this.locale = dto.getLocale();
	}

}
