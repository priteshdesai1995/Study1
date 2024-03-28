package com.humaine.transactional.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dashboardmetrics")
@NoArgsConstructor
@Getter
@Setter
public class DashboardMetrics {

	@Id
	@Column(name = "metricid")
	String id;
	
	@Column(name = "accountid")
	Long account;
	
	@Column(name = "metricvalue")
	String value;
	
	@Column(name = "modifiedon")
	OffsetDateTime modifiedOn;
}
