/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This class is an entity that represents Announcement objects in the database.
 * 
 * @author minesh_prajapati
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "announcements")
@AttributeOverride(name = "id", column = @Column(name = "announcement_id"))
public class Announcement extends BaseEntity {

	private static final long serialVersionUID = 8619411714225120764L;

	@Column(name = "title_en")
	private String titleEN;

	@Column(name = "title_ar")
	private String titleAR;

	@Column(name = "type")
	private String type;

	@Column(name = "description_en")
	private String descriptionEN;
	
	@Column(name = "description_ar")
	private String descriptionAR;

	@Column(name = "status")
	private String status;

	@Column(name = "target_platform")
	private String targetPlatform;

	@Column(name = "attachment")
	private String attachment;

	@Column(name = "advanced_filters")
	private String advancedFilters;

	@Column(name = "start_date")
	private String start_date;

	@Column(name = "end_date")
	private String end_date;

}
