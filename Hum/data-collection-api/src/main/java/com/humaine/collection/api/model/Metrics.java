package com.humaine.collection.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "metricsmaster")
@NoArgsConstructor
@Getter
@Setter
public class Metrics {
	
	@Id
	@Column(name = "metricid")
	String id;
	
	@Column(name = "accountid")
	Long account;
	
	@Column(name = "name")
	String name;

	@Column(name = "description")
	String description;
	
	@Column(name = "refreshfrequency")
	Float refreshFrequency;
	
	@Column(name = "createdon")
	OffsetDateTime createdOn;

	@Column(name = "modifiedon")
	OffsetDateTime modifiedOn;

}
