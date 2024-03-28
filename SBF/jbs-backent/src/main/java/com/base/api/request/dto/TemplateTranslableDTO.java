package com.base.api.request.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TemplateTranslableDTO implements Serializable {

	private static final long serialVersionUID = -393565054463005312L;

	private String emailBody;
	private String emailKey;
	private String emailSubject;
	private String locale;

}
