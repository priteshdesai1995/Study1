package com.humaine.collection.api.es.repository.impl;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.humaine.collection.api.es.model.EsPageLoadData;
import com.humaine.collection.api.es.projection.model.ElasticPageLoadData;
import com.humaine.collection.api.rest.repository.PageLoadDataRepository;
import com.humaine.collection.api.util.DateUtils;
import com.humaine.collection.api.util.ElasticIndexUtils;
import com.humaine.collection.api.util.FileUtils;

@Repository
public class ESPageLoadDataRespository {

	private static final Logger logger = LogManager.getLogger(ESPageLoadDataRespository.class);

	@Autowired
	RestHighLevelClient elasticsearchClient;

	@Value("${elasticsearch.schema.page-load.index.prefix}")
	String prefix;

	@Value("${elasticsearch.schema.page-load.index.alias}")
	String userEventAlias;

	@Value("${elasticsearch.schema.location}")
	String schemaFolderLocation;

	@Value("${elasticsearch.schema.page-load.file}")
	String schemaFile;

	Integer batchSize = 500;

	@Autowired
	PageLoadDataRepository pageLoadDataRespository;

	public void createIndex(String indexName) throws IOException {
		GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
		boolean exists = elasticsearchClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
		if (!exists) {
			logger.info("Elastic Index Does not exist with Name: {}: Creating Index with Name: {}", indexName,
					indexName);
			CreateIndexRequest request = new CreateIndexRequest(indexName);
			String mapping = FileUtils
					.readClasspathFile(ElasticIndexUtils.getSchemaLocation(schemaFolderLocation) + schemaFile);
			request.mapping(mapping, XContentType.JSON);
			request.alias(new Alias(userEventAlias));
			CreateIndexResponse indexResponse = elasticsearchClient.indices().create(request, RequestOptions.DEFAULT);
		}
	}

	public Long getLastIndexedDocumetId(String indexName) throws IOException {
		Long lastRecord = null;
		SearchRequest searchRequest = new SearchRequest(indexName);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder);
		searchSourceBuilder.size(1);
		searchSourceBuilder.sort("id", SortOrder.DESC);
		try {
			SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
			if (searchResponse.getHits().getHits().length > 0) {
				EsPageLoadData event = new ObjectMapper()
						.readValue(searchResponse.getHits().getHits()[0].getSourceAsString(), EsPageLoadData.class);
				lastRecord = event.getId();
			}
		} catch (Exception e) {

		}
		return lastRecord;
	}

	public void indexPageLoadEventsData(OffsetDateTime date) throws IOException {
		String indexName = ElasticIndexUtils.getIndexName(date, prefix);
		createIndex(indexName);
		Long lastIndexedUserEventId = getLastIndexedDocumetId(indexName);
		List<ElasticPageLoadData> events = new ArrayList<>();
		if (lastIndexedUserEventId == null) {
			events = pageLoadDataRespository.getPageLoadData(date);
		} else {
			events = pageLoadDataRespository.getPageLoadData(lastIndexedUserEventId, date);
		}

		if (events.isEmpty())
			return;
		List<EsPageLoadData> esEvents = new ArrayList<EsPageLoadData>();
		esEvents = events.stream().map(e -> {
			return new EsPageLoadData(e);
		}).collect(Collectors.toList());
		batchBulkIndexDocuments(esEvents, indexName);
	}

	public void batchBulkIndexDocuments(List<EsPageLoadData> events, String indexName) {
		List<List<EsPageLoadData>> output = ListUtils.partition(events, batchSize);
		for (List<EsPageLoadData> eventsList : output) {
			indexBulkDocument(eventsList, indexName);
		}
	}

	public void indexBulkDocument(List<EsPageLoadData> events, String indexName) {
		if (events == null || events != null && events.isEmpty())
			return;

		BulkRequest bulkRequest = new BulkRequest();
		events.forEach(e -> {
			try {
				bulkRequest.add(new IndexRequest(indexName).source(new ObjectMapper().writeValueAsString(e),
						XContentType.JSON));
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			}
		});
		try {
			BulkResponse bulkResponse = elasticsearchClient.bulk(bulkRequest, RequestOptions.DEFAULT);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void indexDocument(EsPageLoadData event, String indexName) {
		try {
			IndexRequest request = new IndexRequest(indexName).source(new ObjectMapper().writeValueAsString(event),
					XContentType.JSON);

			IndexResponse indexResponse = elasticsearchClient.index(request, RequestOptions.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void indexPageLoad(Long id) {
		try {
			OffsetDateTime date = DateUtils.getCurrentTimestemp();
			String indexName = ElasticIndexUtils.getIndexName(date, prefix);
			createIndex(indexName);
			ElasticPageLoadData pageLoadObject = pageLoadDataRespository.getPageLoadObject(date, id);
			if (pageLoadObject == null) {
				return;
			}
			EsPageLoadData event = new EsPageLoadData(pageLoadObject);
			indexDocument(event, indexName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deletePageLoadIndices(int count) {
		try {
			logger.info("In deletePageLoadIndices call");
			List<String> indicesPreserve = new ArrayList<>();
			List<String> indicesRemove = new ArrayList<>();
			OffsetDateTime date;
			for (int i = 0; i < count; i++) {
				date = DateUtils.getCurrentTimestemp().minusDays(i);
				String indexName = ElasticIndexUtils.getIndexName(date, prefix);
				indicesPreserve.add(indexName);
			}
			String indexPrefix = prefix.substring(0, (prefix.length() - 1));
			GetIndexRequest request = new GetIndexRequest(indexPrefix + "*");
			GetIndexResponse response = elasticsearchClient.indices().get(request, RequestOptions.DEFAULT);
			String[] totalIndices = response.getIndices();
			Set<String> filteredTotalIndices = new HashSet<String>(Arrays.asList(totalIndices));
			filteredTotalIndices.removeAll(indicesPreserve);
			indicesRemove.addAll(filteredTotalIndices);
			String res = StringUtils.join(indicesRemove, ',');
			logger.info("response" + res);
			if (indicesRemove.size() > 0) {
				DeleteIndexRequest deleteRequest = new DeleteIndexRequest(res);
				elasticsearchClient.indices().delete(deleteRequest, RequestOptions.DEFAULT);
				logger.info("Indices deleted successfully");
			} else {
				logger.info("No index found");
			}
			logger.info("deletePageLoadIndices call ended");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}