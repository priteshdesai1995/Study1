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
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.humaine.collection.api.es.model.ESUserEvent;
import com.humaine.collection.api.es.projection.model.ElasticUserEvent;
import com.humaine.collection.api.rest.repository.UserEventRepository;
import com.humaine.collection.api.util.DateUtils;
import com.humaine.collection.api.util.ElasticIndexUtils;
import com.humaine.collection.api.util.FileUtils;

import ua_parser.Client;
import ua_parser.Parser;

@Repository
public class ESUserEventRepository {

	private static final Logger logger = LogManager.getLogger(ESUserEventRepository.class);

	@Autowired
	RestHighLevelClient elasticsearchClient;

	@Value("${elasticsearch.schema.user-event.index.prefix}")
	String prefix;

	@Value("${elasticsearch.schema.user-event.index.alias}")
	String userEventAlias;

	@Value("${elasticsearch.schema.location}")
	String schemaFolderLocation;

	@Value("${elasticsearch.schema.user-event.file}")
	String schemaFile;

	Integer batchSize = 500;

	Parser uaParser = new Parser();

	@Autowired
	private UserEventRepository userEventRepository;

	public void createIndex(String indexName) {
		GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
		try {
			boolean exists = elasticsearchClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
			if (!exists) {
				logger.info("Elastic Index Does not exist with Name: {}: Creating Index with Name: {}", indexName,
						indexName);
				CreateIndexRequest request = new CreateIndexRequest(indexName);
				String mapping = FileUtils
						.readClasspathFile(ElasticIndexUtils.getSchemaLocation(schemaFolderLocation) + schemaFile);
				request.mapping(mapping, XContentType.JSON);
				request.alias(new Alias(userEventAlias));
				CreateIndexResponse indexResponse = elasticsearchClient.indices().create(request,
						RequestOptions.DEFAULT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Long getLastIndexedDocumetId(String indexName) throws IOException {
		Long lastRecord = null;
		SearchRequest searchRequest = new SearchRequest(indexName);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder);
		searchSourceBuilder.size(1);
		searchSourceBuilder.sort("userEventId", SortOrder.DESC);
		try {
			SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
			if (searchResponse.getHits().getHits().length > 0) {
				ESUserEvent event = new ObjectMapper()
						.readValue(searchResponse.getHits().getHits()[0].getSourceAsString(), ESUserEvent.class);
				lastRecord = event.getUserEventId();
			}
		} catch (Exception e) {

		}
		return lastRecord;
	}

	public List<ESUserEvent> getAllUserEvents(String indexName) {
		List<ESUserEvent> events = new ArrayList<>();
		SearchRequest searchRequest = new SearchRequest(indexName);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder);
		searchSourceBuilder.size(5);
		try {
			SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
			for (SearchHit searchHit : searchResponse.getHits().getHits()) {
				ESUserEvent event = new ObjectMapper().readValue(searchHit.getSourceAsString(), ESUserEvent.class);
				events.add(event);
			}
		} catch (Exception e) {

		}
		return events;
	}

	public void indexUserEventsData(OffsetDateTime date) throws IOException {
		String indexName = ElasticIndexUtils.getIndexName(date, prefix);
		createIndex(indexName);
		Long lastIndexedUserEventId = getLastIndexedDocumetId(indexName);
		List<ElasticUserEvent> events = new ArrayList<>();
		if (lastIndexedUserEventId == null) {
			events = userEventRepository.getUserEventsData(date);
		} else {
			events = userEventRepository.getUserEventsData(lastIndexedUserEventId, date);
		}
		if (events.isEmpty())
			return;
		List<ESUserEvent> esEvents = new ArrayList<ESUserEvent>();
		esEvents = events.stream().map(e -> {
			Client c = uaParser.parse(e.getDevicetype());
			return new ESUserEvent(e, c.userAgent.family);
		}).collect(Collectors.toList());
		batchBulkIndexDocuments(esEvents, indexName);
	}

	public String getIndexName(OffsetDateTime date) {
		return ElasticIndexUtils.getIndexName(date, prefix);
	}

	public String getCurrentDateIndexName() {
		return ElasticIndexUtils.getCurrentDateIndexName(prefix);
	}

	public void batchBulkIndexDocuments(List<ESUserEvent> events, String indexName) {
		List<List<ESUserEvent>> output = ListUtils.partition(events, batchSize);
		for (List<ESUserEvent> eventsList : output) {
			indexBulkDocument(eventsList, indexName);
		}
	}

	public void indexBulkDocument(List<ESUserEvent> events, String indexName) {
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

	public void indexDocument(ESUserEvent event, String indexName) {
		try {
			IndexRequest request = new IndexRequest(indexName).source(new ObjectMapper().writeValueAsString(event),
					XContentType.JSON);
			IndexResponse indexResponse = elasticsearchClient.index(request, RequestOptions.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void indexUserEvent(Long id) {
		try {
			OffsetDateTime date = DateUtils.getCurrentTimestemp();
			String indexName = ElasticIndexUtils.getIndexName(date, prefix);
			createIndex(indexName);
			ElasticUserEvent eventObj = userEventRepository.getUserEventObject(id);
			if (eventObj == null)
				return;
			Client c = uaParser.parse(eventObj.getDevicetype());
			ESUserEvent event = new ESUserEvent(eventObj, c.userAgent.family);
			indexDocument(event, indexName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteUserEventIndices(int count) {
		try {
			logger.info("In deleteUserEventIndices call");
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
			if (indicesRemove.size() > 0) {
				DeleteIndexRequest deleteRequest = new DeleteIndexRequest(res);
				elasticsearchClient.indices().delete(deleteRequest, RequestOptions.DEFAULT);
				logger.info("Indices deleted successfully");
			} else {
				logger.info("No index found");
			}
			logger.info("deleteUserEventIndices call ended");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void indexUserEvents(List<Long> ids) {
		try {
			OffsetDateTime date = DateUtils.getCurrentTimestemp();
			String indexName = ElasticIndexUtils.getIndexName(date, prefix);
			createIndex(indexName);
			List<ElasticUserEvent> eventObjs = userEventRepository.getUserEventObject(ids);
			eventObjs.forEach(e -> {
				System.out.println("-==--==================");
				System.out.println(e.getUsereventid());
				System.out.println(e.getUserid());
				System.out.println(e.getSessionid());
				System.out.println(e.getEventid());
				System.out.println("-==--==================");
			});
			if (eventObjs == null)
				return;
			List<ESUserEvent> elasticUserEvents = new ArrayList<>();
			eventObjs.forEach(e -> {
				Client c = uaParser.parse(e.getDevicetype());
				elasticUserEvents.add(new ESUserEvent(e, c.userAgent.family));
			});
			// indexBulkDocument(elasticUserEvents, indexName);
			batchBulkIndexDocuments(elasticUserEvents, indexName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
