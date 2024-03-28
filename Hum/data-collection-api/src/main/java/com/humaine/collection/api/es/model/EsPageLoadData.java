package com.humaine.collection.api.es.model;

import com.humaine.collection.api.es.projection.model.ElasticPageLoadData;
import com.humaine.collection.api.util.DateUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class EsPageLoadData {

	Long id;

	Long accountId;

	String sessionId;

	String userId;

	String pageURL;

	Long pageLoadTime;

	String timestamp;

	public EsPageLoadData(ElasticPageLoadData data) {
		if (data == null)
			return;
		this.id = data.getId();
		this.accountId = data.getAccountId();
		this.sessionId = data.getSessionId();
		this.pageURL = data.getPageURL();
		this.userId = data.getUserId();
		this.pageLoadTime = data.getPageLoadTime();
		this.timestamp = DateUtils.parse(data.getTimestamp());
	}
}
