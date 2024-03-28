package com.humaine.collection.api.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.humaine.collection.api.request.dto.ProductRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "master_product")
@NoArgsConstructor
@Getter
@Setter
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial")
	Long id;

	@Column(name = "p_id")
	String productId;

	@Column(name = "p_name")
	String productName;

	@OneToOne(optional = true)
	@JoinColumn(name = "p_cat_id", nullable = true)
	ProductCategory category;

	@Column(name = "p_image")
	String productImage;

	@Column(name = "p_desc")
	String productDescription;

	@Column(name = "p_sku")
	String productSKU;

	@ManyToOne
	@JoinColumn(name = "account_id")
	Account account;

	@ElementCollection
	@CollectionTable(name = "product_attributes", joinColumns = @JoinColumn(name = "product_id"))
	Set<ProductAttribute> attributes = new HashSet<ProductAttribute>();

	@OneToMany(cascade = CascadeType.ALL)
	Set<ProductPrice> prices = new HashSet<>();

	@OneToOne(optional = true)
	@JoinColumn(name = "parent_product_id", nullable = true)
	Product parentProduct;

	public Product(String productId, Account account, ProductCategory cat) {
		this.productId = productId;
		this.account = account;
	}

	public Product(String productId, Account account) {
		this(productId, account, null);
	}

	public Product(ProductRequest request, Account account, ProductCategory cat) {
		this.productId = request.getProductId();
		this.productName = request.getProductName();
		this.productImage = request.getProductImage();
		this.productDescription = request.getProductDescription();
		this.productSKU = request.getProductSKU();
		this.account = account;
		this.category = cat;
		if (request.getAttributes()!=null) {			
			request.getAttributes().forEach(a -> {
				this.attributes.add(new ProductAttribute(a));
			});
		}
	}
}
