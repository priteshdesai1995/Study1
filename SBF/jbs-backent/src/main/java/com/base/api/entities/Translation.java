package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "translations")
@AttributeOverride(name = "id", column = @Column(name = "translation_id"))
public class Translation extends BaseEntity	 {

	private static final long serialVersionUID = 6765519209912822044L;

	@Column(name = "page_title")
	private String pageTitle;

	@Column(name = "description")
	private String description;

	@Column(name = "meta_keywords")
	private String metaKeywords;

	@Column(name = "meta_description")
	private String metaDescription;

	@Column(name = "locale")
	public String locale;

	@ToString.Exclude
	@ManyToOne()
	@JsonIgnore
	@JoinColumn(name = "content_id")
	private ContentManagement contentManagement;
}
