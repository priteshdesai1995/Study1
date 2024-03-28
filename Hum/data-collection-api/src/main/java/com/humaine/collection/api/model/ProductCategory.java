package com.humaine.collection.api.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.humaine.collection.api.request.dto.CategoryWiseProductRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "master_category")
@NoArgsConstructor
@Getter
@Setter
public class ProductCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial")
	Long id;

	@Column(name = "cat_id")
	String categoryId;

	@Column(name = "cat_name")
	String categoryName;

	@ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="parent_id", nullable=true)
	ProductCategory parent;

	@ManyToOne
	Account account;

	public ProductCategory(CategoryWiseProductRequest request, Account account, ProductCategory parent) {
		this.categoryId = request.getCategoryId();
		this.categoryName = request.getCategoryName();
		this.parent = parent;
		this.account = account;
	}
}
