package com.humaine.collection.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventorymaster")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InventoryMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inventoryid", columnDefinition = "bigserial")
	Long id;

	@Column(name = "productid")
	String product;

	@Column(name = "accountid")
	Long account;

	@Column(name = "name")
	String name;

	@Column(name = "description")
	String description;

	@Column(name = "category")
	String category;

	@Column(name = "price")
	Float price;

	@Column(name = "relatedimages")
	String images;

	@Column(name = "addedon")
	OffsetDateTime addedOn;

}
