package com.base.api.request.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer rating;
	private String review;
	private boolean isActive;
	private String status;

}
