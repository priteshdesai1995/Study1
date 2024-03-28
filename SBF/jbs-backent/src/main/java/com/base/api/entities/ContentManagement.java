package com.base.api.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "content_management")
@AttributeOverride(name = "id", column = @Column(name = "cms_id"))
public class ContentManagement extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6885537516752698048L;

	@Column(name = "status")
	private String status;

	@Column(name = "styles", columnDefinition = "TEXT")
	private String styles;

	@Column(name = "css", columnDefinition = "TEXT")
	private String css;

	@Column(name = "html", columnDefinition = "TEXT")
	private String html;

	@Column(name = "assets", columnDefinition = "TEXT")
	private String assets;

	@Column(name = "cms_page_name")
	private String cmsPageName;

	@Column(name = "component", columnDefinition = "TEXT")
	private String component;

	@Column(name = "page_type")
	private String pageType;

	@Column(name = "slug")
	private String slug;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "contentManagement")
	private List<Translation> translations;
}
