package com.humaine.collection.api.model;

import java.time.OffsetDateTime;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.humaine.collection.api.converter.JsonType;
import com.humaine.collection.api.request.dto.PageLoadEventRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pageload_data")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@TypeDef(name = "JsonType", typeClass = JsonType.class)
public class PageLoadData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, columnDefinition = "bigserial")
	Long id;

	@Column(name = "accountid")
	Long accountId;

	@Column(name = "sessionid")
	String sessionId;

	@Column(name = "userid")
	String userId;

	@Column(name = "pageurl")
	String pageURL;

	@Column(name = "performance_data")
	@Type(type = "JsonType")
	Map<String, Object> performanceData;

	@Column(name = "loadtime")
	Long loadTime;

	@Column(name = "page_source")
	String pageSource;

	@Column(name = "timestamp")
	OffsetDateTime timestamp;

	public PageLoadData(PageLoadEventRequest request, OffsetDateTime timestamp) {
		this.accountId = request.getAccountID();
		this.sessionId = request.getSessionID();
		this.userId = request.getUserID();
		this.pageURL = request.getPageURL();
		this.performanceData = request.getPerformanceData();
		this.loadTime = request.getPageLoadTime();
		this.pageSource = request.getPageSource();
		this.timestamp = timestamp;
	}
}
