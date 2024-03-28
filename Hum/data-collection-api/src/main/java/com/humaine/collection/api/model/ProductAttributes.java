package com.humaine.collection.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "master_attributes")
@NoArgsConstructor
@Getter
@Setter
public class ProductAttributes {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial")
	Long id;
	
	@OneToOne
	Product product;
	
	@Column(name = "attr_value")
	String value;
	
	@ManyToOne
	Account account;
}
