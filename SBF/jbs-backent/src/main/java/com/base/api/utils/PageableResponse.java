/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This class is use for pageable response for list API.
 * 
 * @author minesh_prajapati
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageableResponse {
	private int currentPage;
	private int totalPages;
	private long totalItems;
}
