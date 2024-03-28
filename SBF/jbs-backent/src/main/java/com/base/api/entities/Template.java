package com.base.api.entities;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "templates")
@AttributeOverride(name = "id", column = @Column(name = "template_id"))
public class Template extends BaseEntity{

	private static final long serialVersionUID = 377183066454525477L;

	@Column(name = "template_purpose")
	private String templatePurpose;

	@Column(name = "template_type")
	private String templateType;

	@Column(name = "status")
	public String status;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "template_id")
	private List<TemplateTranslable> templateTranslable;
}
