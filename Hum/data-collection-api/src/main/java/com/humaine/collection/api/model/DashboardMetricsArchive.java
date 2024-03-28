package com.humaine.collection.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
@Table(name = "dashboardmetricsarchive")
@NoArgsConstructor
@Getter
@Setter
public class DashboardMetricsArchive {
	
	@Column(name = "metricid")
	String id;

	@Column(name = "accountid")
	Long account;

	@Column(name = "metricvalue")
	String value;

	@Column(name = "modifiedon")
	OffsetDateTime modifiedOn;
}
