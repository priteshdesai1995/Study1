package com.base.api.request.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuggestionDTO {

	private UUID suggestionId;
	private String category;
	private String suggestion;
	private String status;
	private String createdBy;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;

}
