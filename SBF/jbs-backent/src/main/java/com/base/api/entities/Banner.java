package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "banners")
@AttributeOverride(name = "id", column = @Column(name = "banner_id"))
public class Banner extends BaseEntity {

	private static final long serialVersionUID = -6582204273141716175L;

//	@Column(name = "banner_title")
//	private String bannerTitle;
	
	@Column(name = "title_en")
	private String titleEN;

	@Column(name = "title_ar")
	private String titleAR;

	@Column(name = "status")
	private String status;

	@Column(name = "image")
	private String image;

	@OneToOne
	private User user;

}
