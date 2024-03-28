package com.base.api.response.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDTO implements Serializable{

	private static final long serialVersionUID = -3344189370037855585L;
	
	private String contact_details;
	private LocalDateTime created_at;
	private String created_by;
	private boolean deleted;
	private String message;
	private String name;
	private String status;
	private String uuid;
	private String subject;
	private LocalDateTime updated_at;
	private String username;
}
