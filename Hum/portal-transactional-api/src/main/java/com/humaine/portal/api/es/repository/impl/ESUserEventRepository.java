package com.humaine.portal.api.es.repository.impl;

import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.PipelineAggregatorBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.Cardinality;
import org.elasticsearch.search.aggregations.metrics.Sum;
import org.elasticsearch.search.aggregations.pipeline.SimpleValue;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humaine.portal.api.enums.DurationGraphTimeUnit;
import com.humaine.portal.api.enums.GraphDuration;
import com.humaine.portal.api.enums.UserEvents;
import com.humaine.portal.api.es.model.ESUserEvent;
import com.humaine.portal.api.exception.APIException;
import com.humaine.portal.api.response.dto.ActiveDeviceUsedResponse;
import com.humaine.portal.api.response.dto.ChartData;
import com.humaine.portal.api.response.dto.ChartDataWithRange;
import com.humaine.portal.api.response.dto.ConversionChartResultResponse;
import com.humaine.portal.api.response.dto.LiveDashboardChartData;
import com.humaine.portal.api.response.dto.LiveDashboardPageResponse;
import com.humaine.portal.api.response.dto.LivePageChartData;
import com.humaine.portal.api.response.dto.SessionDurationChartResponse;
import com.humaine.portal.api.response.dto.TopActiveStatesData;
import com.humaine.portal.api.util.DateUtils;
import com.humaine.portal.api.util.ErrorMessageUtils;

@Repository
public class ESUserEventRepository {

	private static final Logger logger = LogManager.getLogger(ESUserEventRepository.class);

	private Long apdeskInterval = 1L;

	private Long tolarateRate = 4L;

	private DecimalFormat decimalFormatter = new DecimalFormat("#.##");

	private final String elasticAggrDateTimeFormat = "yyyy-MM-dd'T'hh a";
	// Converter WRT Milliseconds
	private Map<DurationGraphTimeUnit, Double> timeUnitConvertor = new HashMap<DurationGraphTimeUnit, Double>() {
		private static final long serialVersionUID = -1832704959240479915L;

		{
			put(DurationGraphTimeUnit.MILLISECONDS, 1D);
			put(DurationGraphTimeUnit.SECONDS, 1000D);
			put(DurationGraphTimeUnit.MINUTES, 60000D);
		}
	};

	@Autowired
	RestHighLevelClient elasticsearchClient;

	@Value("${elasticsearch.schema.user-event.index.prefix}")
	String prefix;

	@Value("${elasticsearch.schema.user-event.index.alias}")
	String userEventAlias;

	@Value("${elasticsearch.schema.page-load.index.prefix}")
	String pageLoadPrefix;

	@Value("${elasticsearch.schema.page-load.index.alias}")
	String pageLoadAlias;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Value("${elasticsearch.host}")
	private String EsHost;

	@Value("${elasticsearch.port}")
	private String EsPort;

	@Value("${elasticsearch.rest.username}")
	private String username;

	@Value("${elasticsearch.rest.password}")
	private String password;

	@Autowired
	private RestTemplate restTemplate;

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
			logger.info("getAllUserEvents()");
			e.printStackTrace();
		}
		return events;
	}

	public String getIndexName(OffsetDateTime date) {
		return prefix + DateUtils.getFromatedDate(date).replaceAll("-", "");
	}

	public String getCurrentDateIndexName() {
		return getIndexName(DateUtils.getCurrentTimestemp());
	}

	public LiveDashboardPageResponse getStateWiseProductSale(List<String> indexes, Long accountId,
			Integer noOfResults) {
		if (noOfResults == null)
			noOfResults = 5;

//		String lte = DateUtils.getTime(DateUtils.getCurrentTimestemp());
//		String gte = DateUtils.getTime(DateUtils.getCurrentTimestemp().minusHours(23));
		String gte = DateUtils.getHourTime(DateUtils.getCurrentTimestemp());

		SearchRequest searchRequest = new SearchRequest(String.join(",", indexes))
				.indicesOptions(IndicesOptions.fromOptions(true, false, false, false));
		BoolQueryBuilder q = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("accountId", accountId))
				.mustNot(QueryBuilders.termQuery("eventId", UserEvents.END))
				.must(QueryBuilders.rangeQuery("timestamp").gte(gte));

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(q);
		searchSourceBuilder.aggregation(AggregationBuilders.terms("states").field("state")
				.order(BucketOrder.aggregation("stateCount", false)).size(noOfResults)
				.subAggregation(AggregationBuilders.cardinality("stateCount").field("sessionId")));
		searchRequest.source(searchSourceBuilder);
		searchSourceBuilder.size(0);
		SearchResponse searchResponse = null;
		List<TopActiveStatesData> result = new ArrayList<>();
		try {
			searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
			Terms terms = searchResponse.getAggregations().get("states");
			result = terms.getBuckets().stream().map(e -> {
				Cardinality sum = e.getAggregations().get("stateCount");
				return new TopActiveStatesData(e.getKeyAsString(), sum.getValue());
			}).collect(Collectors.toList());
		} catch (Exception e) {
			logger.info("getStateWiseProductSale()");
			e.printStackTrace();
		}
		return new LiveDashboardPageResponse(result);
	}

	public ChartDataWithRange<Long> getUserInLastTwentyFourHours(List<String> indexes, Long accountId) {
		SearchRequest searchRequest = new SearchRequest(String.join(",", indexes))
				.indicesOptions(IndicesOptions.fromOptions(true, false, false, false));

		String lte = DateUtils.getTime(DateUtils.getCurrentTimestemp().plusHours(1));
		String gte = DateUtils.getTime(DateUtils.getCurrentTimestemp().minusHours(23));

		BoolQueryBuilder q = QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery("eventId", UserEvents.END))
				.must(QueryBuilders.termQuery("accountId", accountId))
				.must(QueryBuilders.rangeQuery("timestamp").lte(lte).gte(gte));

		final String aggName = "hourlyUserCount";
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(q);
		searchSourceBuilder.aggregation(AggregationBuilders.dateHistogram(aggName).field("timestamp")
				.calendarInterval(DateHistogramInterval.HOUR).format(elasticAggrDateTimeFormat)
				.subAggregation(AggregationBuilders.cardinality("unique_users").field("userId")));
		searchRequest.source(searchSourceBuilder);
		searchSourceBuilder.size(0);
		SearchResponse searchResponse = null;
		HashMap<String, Long> activeUsers = new HashMap<>();
		try {
			searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
			Histogram histoGram = searchResponse.getAggregations().get(aggName);
			histoGram.getBuckets().stream().forEach(e -> {
				Cardinality cardinality = e.getAggregations().get("unique_users");
				activeUsers.put(e.getKeyAsString(), cardinality.getValue());
			});
		} catch (Exception e) {
			logger.info("getUserInLastTwentyFourHours()");
			e.printStackTrace();
		}
		return new ChartDataWithRange<Long>(activeUsers, lte, gte);
//		return new ActiveUserChangesResponse(activeUsers, DateUtils.getCurrentGraphDate());
	}

	public List<ActiveDeviceUsedResponse> getActiveDevicesUsedData(Long accountId) {
		List<String> indexs = new ArrayList<>();
		OffsetDateTime current = DateUtils.getCurrentTimestemp();
		indexs.add(getIndexName(current));

		SearchRequest searchRequest = new SearchRequest(String.join(",", indexs))
				.indicesOptions(IndicesOptions.fromOptions(true, false, false, false));

		String gte = DateUtils.getHourTime(DateUtils.getCurrentTimestemp());

		BoolQueryBuilder q = QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery("eventId", UserEvents.END))
				.must(QueryBuilders.termQuery("accountId", accountId))
				.must(QueryBuilders.rangeQuery("timestamp").gte(gte));

		final String aggName = "browserWiseCount";
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(q);
		searchSourceBuilder.aggregation(AggregationBuilders.terms(aggName).field("browser")
				.subAggregation(AggregationBuilders.cardinality("uniqueSessions").field("sessionId")));
		searchRequest.source(searchSourceBuilder);
		searchSourceBuilder.size(0);
		SearchResponse searchResponse = null;
		List<ActiveDeviceUsedResponse> result = new ArrayList<>();
		try {
			searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
			Terms terms = searchResponse.getAggregations().get(aggName);
			result = terms.getBuckets().stream().map(e -> {
				Cardinality car = e.getAggregations().get("uniqueSessions");
				return new ActiveDeviceUsedResponse(e.getKey().toString(), car.getValue());
			}).collect(Collectors.toList());
		} catch (Exception e) {
			logger.info("getActiveDevicesUsedData()");
			e.printStackTrace();
		}
		return result;
	}

	private SearchRequest getActiveUsersChartDataEsRequest(List<String> indexes, Long accountId, Boolean multisearch) {
		if (multisearch == null)
			multisearch = false;
		String index = multisearch == true ? userEventAlias : String.join(",", indexes);
		SearchRequest searchRequest = new SearchRequest(index)
				.indicesOptions(IndicesOptions.fromOptions(true, false, false, false));
		String lte = DateUtils.getTime(DateUtils.getCurrentTimestemp());
		String gte = DateUtils.getTime(DateUtils.getCurrentTimestemp().minusHours(24));
		BoolQueryBuilder q = QueryBuilders.boolQuery()
//				.must(QueryBuilders.termQuery("eventId", UserEvents.START.value()))
				.mustNot(QueryBuilders.termQuery("eventId", UserEvents.END))
				.must(QueryBuilders.termQuery("accountId", accountId))
				.must(QueryBuilders.rangeQuery("timestamp").lte(lte).gte(gte));

		final String aggName = "hourlyUserCount";
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(q);
		searchSourceBuilder.aggregation(AggregationBuilders.dateHistogram(aggName).field("timestamp")
				.calendarInterval(DateHistogramInterval.HOUR).format(elasticAggrDateTimeFormat)
				.subAggregation(AggregationBuilders.cardinality("sessionCount").field("sessionId")));
		searchRequest.source(searchSourceBuilder);
		searchSourceBuilder.size(0);
		return searchRequest;
	}

	private LivePageChartData processActiveUserChartDataResponse(SearchResponse searchResponse) {
		HashMap<String, Long> activeUsersData = new HashMap<>();
		Long count = 0L;
		final String aggName = "hourlyUserCount";
		if (searchResponse == null) {
			return new LivePageChartData(activeUsersData, count, lastHourChange(activeUsersData));
		}
		try {
			Histogram histoGram = searchResponse.getAggregations().get(aggName);
//			count = searchResponse.getHits().getTotalHits().value;
			histoGram.getBuckets().stream().forEach(e -> {
				Cardinality car = e.getAggregations().get("sessionCount");
				activeUsersData.put(e.getKeyAsString(), car.getValue());
			});
		} catch (Exception e) {
			logger.info("getActiveUsersChartData()");
			e.printStackTrace();
		}
		count = getCurrentHourCount(activeUsersData);
		return new LivePageChartData(activeUsersData, count, lastHourChange(activeUsersData));
	}

	public LivePageChartData getActiveUsersChartData(List<String> indexes, Long accountId) {
		SearchRequest searchRequest = getActiveUsersChartDataEsRequest(indexes, accountId, false);
		SearchResponse searchResponse = null;
		try {
			searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (Exception e) {
			logger.info("getActiveUsersChartData()");
			e.printStackTrace();
		}
		return processActiveUserChartDataResponse(searchResponse);
	}

//	SessionDurationChartResponse
	public HashMap<String, Double> getSessionDurationData(Long accountId, GraphDuration duration) {
		List<String> indexes = getIndexes(duration);

		final String timeDifferenceScript = "ZonedDateTime zdt1 = doc.sessionStartTime.value;\n"
				+ "        ZonedDateTime zdt2 = doc.sessionEndTime.value;\n"
				+ "        long differenceInMillis = ChronoUnit.MILLIS.between(zdt1, zdt2);\n"
				+ "        return Math.abs(differenceInMillis);";

		String keyFindScript = "def zdt = doc.sessionStartTime.value;\n"
				+ "DateTimeFormatter dtf = DateTimeFormatter.ofPattern(\"" + elasticAggrDateTimeFormat + "\");\n"
				+ "return zdt.format(dtf);";
		if (GraphDuration.ONE_WEEK.equals(duration)) {
//			keyFindScript = "def dayOfWeek = doc.sessionStartTime.value.getDayOfWeek();\n"
//					+ "        return (dayOfWeek == 1 ? 'Mon' : (dayOfWeek == 2 ? 'Tue' : ((dayOfWeek == 3 ? 'Wed' : ((dayOfWeek == 4 ? 'Thu' : ((dayOfWeek == 5 ? 'Fri' : ((dayOfWeek == 6 ? 'Sat' : 'Sun'))))))))))";

			keyFindScript = "return doc.sessionStartTime.value.toString('yyyy-MM-dd')";
		}

		final String keyAgg = "keyAgg";
		final String durationAgg = "durationAgg";

		String lte = DateUtils.getTime(DateUtils.getCurrentTimestemp());
		String gte = DateUtils.getTime(DateUtils.getCurrentTimestemp().minusHours(24));

//		String lte = "now";
//		String gte = "now-1d/d";
		DateHistogramInterval interval = DateHistogramInterval.HOUR;
		String dateFormat = elasticAggrDateTimeFormat;
		if (GraphDuration.ONE_WEEK.equals(duration)) {
//			gte = "now-7d/d";
			gte = DateUtils.getTime(DateUtils.getCurrentTimestemp().minusDays(7));
			interval = DateHistogramInterval.DAY;
			dateFormat = "yyyy-MM-dd";
		} else if (GraphDuration.FORTY_EIGHT_HOURS.equals(duration)) {
//			gte = "now-2d/d";
			gte = DateUtils.getTime(DateUtils.getCurrentTimestemp().minusHours(48));
		}

		SearchRequest searchRequest = new SearchRequest(String.join(",", indexes))
				.indicesOptions(IndicesOptions.fromOptions(true, true, false, false));
		BoolQueryBuilder q = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("eventId", UserEvents.END.value()))
				.must(QueryBuilders.termQuery("accountId", accountId)).must(QueryBuilders.existsQuery("sessionEndTime"))
				.must(QueryBuilders.rangeQuery("sessionStartTime").lte(lte).gte(gte));

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(q);
		searchSourceBuilder.aggregation(AggregationBuilders.dateHistogram(keyAgg).field("sessionStartTime")
				.calendarInterval(interval).format(dateFormat).subAggregation(AggregationBuilders.avg(durationAgg)
						.script(new Script(ScriptType.INLINE, "painless", timeDifferenceScript, new HashMap<>()))));
		searchRequest.source(searchSourceBuilder);
		searchSourceBuilder.size(0);
		SearchResponse searchResponse = null;
		HashMap<String, Double> sessions = new HashMap<>();
		Long count = 0L;
		try {
			searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
			if (searchResponse.getAggregations() != null) {
				Histogram terms = searchResponse.getAggregations().get(keyAgg);
				if (terms != null) {
					terms.getBuckets().stream().forEach(e -> {

						Avg avg = e.getAggregations().get(durationAgg);
						Double v = avg.getValue();
						if (v != null && v > 0 && Double.POSITIVE_INFINITY != avg.getValue()) {
							sessions.put(e.getKeyAsString(), Math.abs(formattedDoubleValue(avg.getValue())));
						}
					});
				}
			}
		} catch (Exception e) {
			logger.info("getSessionDurationData()");
			e.printStackTrace();
		}

		return sessions;
	}

	/**
	 * Get Elastic Search Request For the Over All Conversition Rate
	 * 
	 * @param indexes
	 * @param accountId
	 * @param lastHourCount
	 * @return SearchRequest
	 */
	private SearchRequest overAllConversitionRateSearchRequest(List<String> indexes, Long accountId,
			Boolean lastHourCount, Boolean multisearch) {
		if (multisearch == null)
			multisearch = false;
		String index = multisearch == true ? userEventAlias : String.join(",", indexes);
		SearchRequest searchRequest = new SearchRequest(index)
				.indicesOptions(IndicesOptions.fromOptions(true, false, false, false));
		String lte = DateUtils.getTime(DateUtils.getCurrentTimestemp());
		String gte = DateUtils.getTime(DateUtils.getCurrentTimestemp().minusHours(24));

		BoolQueryBuilder q = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("accountId", accountId))
				.mustNot(QueryBuilders.termQuery("eventId", UserEvents.END))
				.must(QueryBuilders.rangeQuery("timestamp").lte(lte).gte(gte));

		final String aggGlobalAggName = "globalCount";
		final String graphAggName = "grpah";
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(q);

		Map<String, String> bucketsPathsMap = new HashMap<>();
		bucketsPathsMap.put("totalVisist", "user_count");
		bucketsPathsMap.put("totalSale", "buy_out_come > unique_user_sale_id");

		searchSourceBuilder.aggregation(AggregationBuilders.cardinality("totalCount").field("sessionId"))
				.aggregation(AggregationBuilders
						.filter(aggGlobalAggName, QueryBuilders.termsQuery("eventId", Arrays.asList(UserEvents.BUY)))
						.subAggregation(AggregationBuilders.cardinality("count").field("sessionId")))
				.aggregation(
						AggregationBuilders.dateHistogram(graphAggName).field("timestamp")
								.calendarInterval(DateHistogramInterval.HOUR).format(elasticAggrDateTimeFormat)
								.subAggregation(AggregationBuilders.cardinality("user_count").field("sessionId"))
								.subAggregation(
										AggregationBuilders
												.filter("buy_out_come",
														QueryBuilders.termsQuery("eventId",
																Arrays.asList(UserEvents.BUY)))
												.subAggregation(AggregationBuilders.cardinality("unique_user_sale_id")
														.field("sessionId")))
								.subAggregation(PipelineAggregatorBuilders.bucketScript("rateGraph", bucketsPathsMap,
										new Script("(params.totalSale/ params.totalVisist)*100"))));

		searchRequest.source(searchSourceBuilder);
		searchSourceBuilder.size(0);
		return searchRequest;
	}

	/**
	 * Get All Conversition Rate Response
	 * 
	 * @param searchResponse
	 * @param lastHourCount
	 * @return
	 */
	private ConversionChartResultResponse<Long> processAllConversitionRateEsResponse(SearchResponse searchResponse,
			Boolean lastHourCount) {
		HashMap<String, Double> result = new HashMap<>();
		if (searchResponse == null) {
			return new ConversionChartResultResponse<Long>(result, null, null, getLastHourChanges(result));
		}
		Long count = 0L;
		Long totalCount = 0L;
		final String aggGlobalAggName = "globalCount";
		final String graphAggName = "grpah";
		try {
			ParsedFilter parsedFilter = searchResponse.getAggregations().get("globalCount");
			Cardinality cardinality = parsedFilter.getAggregations().get("count");
			if (cardinality != null) {
				count = cardinality.getValue();
			}
			Cardinality totalCardinality = searchResponse.getAggregations().get("totalCount");
			if (totalCardinality != null) {
				totalCount = totalCardinality.getValue();
			}
			Histogram histoGram = searchResponse.getAggregations().get(graphAggName);
			histoGram.getBuckets().stream().forEach(e -> {
				SimpleValue value = e.getAggregations().get("rateGraph");
				if (value != null) {
					result.put(e.getKeyAsString(), formattedDoubleValue(value.value()));
				}
			});
		} catch (Exception e) {
			logger.info("getOverallConversionRate()");
			e.printStackTrace();
		}

		Double percentage = 0D;
		percentage = currentHourCount(result);

		if (lastHourCount == false) {
			percentage = 0D;
			if (totalCount > 0 && count > 0) {
				percentage = formattedDoubleValue(count / Double.parseDouble(totalCount.toString())) * 100;
			}
		}
		return new ConversionChartResultResponse<Long>(result, null, percentage, getLastHourChanges(result));
	}

	/**
	 * Get Elastic Data For Overall Conversiton Rate
	 * 
	 * @param indexes
	 * @param accountId
	 * @return ConversionChartResultResponse
	 */
	public ConversionChartResultResponse<Long> getOverallConversionRate(List<String> indexes, Long accountId) {
		return getOverallConversionRate(indexes, accountId, true);
	}

	public ConversionChartResultResponse<Long> getOverallConversionRate(List<String> indexes, Long accountId,
			Boolean lastHourCount) {
		SearchRequest searchRequest = this.overAllConversitionRateSearchRequest(indexes, accountId, lastHourCount,
				false);
		SearchResponse searchResponse = null;
		try {
			searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (Exception e) {
			logger.info("getOverallConversionRate()");
			e.printStackTrace();
		}
		return processAllConversitionRateEsResponse(searchResponse, lastHourCount);
	}

	private SearchRequest getActiveUsersEsRequest(List<String> indexes, Long accountId, Boolean multisearch) {
		if (multisearch == null)
			multisearch = false;
		String index = multisearch == true ? userEventAlias : String.join(",", indexes);

		SearchRequest searchRequest = new SearchRequest(index)
				.indicesOptions(IndicesOptions.fromOptions(true, false, false, false));

		String lte = DateUtils.getTime(DateUtils.getCurrentTimestemp());
		String gte = DateUtils.getTime(DateUtils.getCurrentTimestemp().minusHours(24));

		BoolQueryBuilder q = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("accountId", accountId))
				.mustNot(QueryBuilders.termQuery("eventId", UserEvents.END))
				.must(QueryBuilders.rangeQuery("timestamp").lte(lte).gte(gte));

		final String dateWiseAggName = "date_wise";
		final String totalCountAggName = "totalCount";
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(q);

		searchSourceBuilder.aggregation(AggregationBuilders.cardinality(totalCountAggName).field("userId"))
				.aggregation(AggregationBuilders.dateHistogram(dateWiseAggName).field("timestamp")
						.calendarInterval(DateHistogramInterval.HOUR).format(elasticAggrDateTimeFormat)
						.subAggregation(AggregationBuilders.cardinality("count").field("userId")));

		searchRequest.source(searchSourceBuilder);
		searchSourceBuilder.size(0);

		return searchRequest;
	}

	private ConversionChartResultResponse<Double> processGetActiveUsersEsResponse(SearchResponse searchResponse) {

		HashMap<String, Double> result = new HashMap<>();
		Double totalCount = 0D;
		final String dateWiseAggName = "date_wise";
		final String totalCountAggName = "totalCount";
		if (searchResponse == null) {
			return new ConversionChartResultResponse<Double>(result, totalCount, null, getLastHourChanges(result));
		}
		try {
			Histogram histoGram = searchResponse.getAggregations().get(dateWiseAggName);
			histoGram.getBuckets().stream().forEach(e -> {
				Cardinality value = e.getAggregations().get("count");
				result.put(e.getKeyAsString(), value.value());
			});
		} catch (Exception e) {
			logger.info("getActiveUsers()");
			e.printStackTrace();
		}
		totalCount = currentHourCount(result);
		return new ConversionChartResultResponse<Double>(result, totalCount, null, getLastHourChanges(result));
	}

	public ConversionChartResultResponse<Double> getActiveUsers(List<String> indexes, Long accountId) {
		SearchRequest searchRequest = getActiveUsersEsRequest(indexes, accountId, false);
		SearchResponse searchResponse = null;
		try {
			searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (Exception e) {
			logger.info("getActiveUsers()");
			e.printStackTrace();
		}
		return processGetActiveUsersEsResponse(searchResponse);
	}

	private SearchRequest getTotalConversionsEsRequest(List<String> indexes, Long accountId, Boolean multiSearch) {
		if (multiSearch == null)
			multiSearch = false;
		String index = multiSearch == true ? userEventAlias : String.join(",", indexes);
		SearchRequest searchRequest = new SearchRequest(index)
				.indicesOptions(IndicesOptions.fromOptions(true, false, false, false));

		String lte = DateUtils.getTime(DateUtils.getCurrentTimestemp());
		String gte = DateUtils.getTime(DateUtils.getCurrentTimestemp().minusHours(24));

		BoolQueryBuilder q = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("accountId", accountId))
				.mustNot(QueryBuilders.termQuery("eventId", UserEvents.END))
				.must(QueryBuilders.termsQuery("eventId", Arrays.asList(UserEvents.BUY)))
				.must(QueryBuilders.rangeQuery("timestamp").lte(lte).gte(gte));

		final String aggGlobalAggName = "globalCount";
		final String graphAggName = "grpah";
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(q);

		searchSourceBuilder.aggregation(AggregationBuilders.dateHistogram(graphAggName).field("timestamp")
				.calendarInterval(DateHistogramInterval.HOUR).format(elasticAggrDateTimeFormat)
				.subAggregation(AggregationBuilders.cardinality("user_count").field("userId")));

		searchRequest.source(searchSourceBuilder);
		searchSourceBuilder.size(0);
		return searchRequest;
	}

	private ConversionChartResultResponse<Double> processGetTotalConversions(SearchResponse searchResponse) {
		HashMap<String, Double> result = new HashMap<>();
		Double count = 0D;
		if (searchResponse == null) {
			return new ConversionChartResultResponse<Double>(result, count, null, getLastHourChanges(result));
		}
		final String aggGlobalAggName = "globalCount";
		final String graphAggName = "grpah";
		try {
			Histogram histoGram = searchResponse.getAggregations().get(graphAggName);
			histoGram.getBuckets().stream().forEach(e -> {
				Cardinality value = e.getAggregations().get("user_count");
				result.put(e.getKeyAsString(), value.value());
			});
		} catch (Exception e) {
			logger.info("getTotalConversions()");
			e.printStackTrace();
		}
		count = currentHourCount(result);
		return new ConversionChartResultResponse<Double>(result, count, null, getLastHourChanges(result));
	}

	public ConversionChartResultResponse<Double> getTotalConversions(List<String> indexes, Long accountId) {

		SearchRequest searchRequest = getTotalConversionsEsRequest(indexes, accountId, false);
		SearchResponse searchResponse = null;
		try {
			searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (Exception e) {
			logger.info("getTotalConversions()");
			e.printStackTrace();
		}
		return processGetTotalConversions(searchResponse);
	}

	public Double getLastHourChanges(HashMap<String, Double> result) {
		OffsetDateTime current = DateUtils.getCurrentTimestemp();
		String currentKey = DateTimeFormatter.ofPattern(elasticAggrDateTimeFormat).format(current);
		String previousKey = DateTimeFormatter.ofPattern(elasticAggrDateTimeFormat).format(current.minusHours(1));

		Double currentValue = 0D;
		Double previousValue = 0D;
		if (result.containsKey(currentKey)) {
			currentValue = result.get(currentKey);
		}

		if (result.containsKey(previousKey)) {
			previousValue = result.get(previousKey);
		}

		Double diff = currentValue - previousValue;
		Double percentage = currentValue;

		if (previousValue != null && previousValue != 0) {
			percentage = (Math.abs(diff) * 100) / previousValue;
		}

		if (previousValue == 0 && currentValue != 0) {
			return 100D;
		}

		if (diff < 0) {
			percentage = 0 - percentage;
		}

		return formattedDoubleValue(percentage);
	}

	public Long lastHourChange(HashMap<String, Long> result) {
		OffsetDateTime current = DateUtils.getCurrentTimestemp();
		String currentKey = DateTimeFormatter.ofPattern(elasticAggrDateTimeFormat).format(current);
		String previousKey = DateTimeFormatter.ofPattern(elasticAggrDateTimeFormat).format(current.minusHours(1));

		Long currentValue = 0L;
		Long previousValue = 0L;
		if (result.containsKey(currentKey)) {
			currentValue = result.get(currentKey);
		}

		if (result.containsKey(previousKey)) {
			previousValue = result.get(previousKey);
		}

		Long diff = currentValue - previousValue;
		Long percentage = currentValue;

		if (previousValue != null && previousValue != 0) {
			percentage = (Math.abs(diff) * 100) / previousValue;
		}

		if (previousValue == 0 && currentValue != 0) {
			return 100L;
		}

		if (diff < 0) {
			percentage = 0 - percentage;
		}

		return percentage;
	}

	public Double currentHourCount(HashMap<String, Double> result) {
		OffsetDateTime current = DateUtils.getCurrentTimestemp();
		String currentKey = DateTimeFormatter.ofPattern(elasticAggrDateTimeFormat).format(current);

		Double currentValue = 0D;
		if (result.containsKey(currentKey)) {
			currentValue = result.get(currentKey);
		}

		return formattedDoubleValue(currentValue);
	}

	public Long getCurrentHourCount(HashMap<String, Long> result) {
		OffsetDateTime current = DateUtils.getCurrentTimestemp();
		String currentKey = DateTimeFormatter.ofPattern(elasticAggrDateTimeFormat).format(current);

		Long currentValue = 0L;
		if (result.containsKey(currentKey)) {
			currentValue = result.get(currentKey);
		}

		return currentValue;
	}

	public List<String> getIndexes(GraphDuration duration) {
		if (duration == null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.duration.null", new Object[] {},
					"api.error.duration.null.code"));
		}
		List<String> indexs = new ArrayList<>();
		OffsetDateTime today = DateUtils.getCurrentTimestemp();
		if (GraphDuration.TWENTY_FOUR_HOURS.equals(duration)) {
			indexs.add(getIndexName(today));
			if (!indexs.contains(getIndexName(today.minusHours(23)))) {
				indexs.add(getIndexName(today.minusHours(23)));
			}
		} else if (GraphDuration.FORTY_EIGHT_HOURS.equals(duration)) {
//			indexs.add(getIndexName(today.minusDays(1)));
			indexs.add(getIndexName(today));
			if (!indexs.contains(getIndexName(today.minusHours(23)))) {
				indexs.add(getIndexName(today.minusHours(23)));
			}
			if (!indexs.contains(getIndexName(today.minusHours(46)))) {
				indexs.add(getIndexName(today.minusHours(46)));
			}
		} else if (GraphDuration.ONE_WEEK.equals(duration)) {
			OffsetDateTime monday = today.with(DayOfWeek.MONDAY);
			indexs.add(getIndexName(monday));
			int i = 1;
			while (i < 7) {
				indexs.add(getIndexName(monday.plusDays(i)));
				i++;
			}
		}
		return indexs;
	}

	public List<String> getPageLoadIndexes(GraphDuration duration) {
		if (duration == null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.duration.null", new Object[] {},
					"api.error.duration.null.code"));
		}
		List<String> indexs = new ArrayList<>();
		OffsetDateTime today = DateUtils.getCurrentTimestemp();
		if (GraphDuration.TWENTY_FOUR_HOURS.equals(duration)) {
			indexs.add(getPageLoadIndexName(today));
			if (!indexs.contains(getPageLoadIndexName(today.minusHours(23)))) {
				indexs.add(getPageLoadIndexName(today.minusHours(23)));
			}
		} else if (GraphDuration.FORTY_EIGHT_HOURS.equals(duration)) {
			indexs.add(getPageLoadIndexName(today));
			if (!indexs.contains(getPageLoadIndexName(today.minusHours(23)))) {
				indexs.add(getPageLoadIndexName(today.minusHours(23)));
			}
			if (!indexs.contains(getPageLoadIndexName(today.minusHours(46)))) {
				indexs.add(getPageLoadIndexName(today.minusHours(46)));
			}
		} else if (GraphDuration.ONE_WEEK.equals(duration)) {
			OffsetDateTime monday = today.with(DayOfWeek.MONDAY);
			indexs.add(getPageLoadIndexName(monday));
			int i = 1;
			while (i < 7) {
				indexs.add(getPageLoadIndexName(monday.plusDays(i)));
				i++;
			}
		}
		return indexs;
	}

	private Double formattedDoubleValue(double value) {
		return Double.valueOf(decimalFormatter.format(value));
	}

	private SessionDurationChartResponse getDurationData(GraphDuration duration, HashMap<String, Double> sessions) {
		if (sessions.size() == 0)
			return new SessionDurationChartResponse(new HashMap<>(), DurationGraphTimeUnit.MILLISECONDS,
					DateUtils.getFromatedDate(DateUtils.getCurrentTimestemp()),
					DateUtils.getFromatedDate(DateUtils.getCurrentTimestemp().minusDays(1)));
		Double minumValue = Collections.min(sessions.values().stream().filter(e -> e > 0).collect(Collectors.toList()));
		DurationGraphTimeUnit unit = DurationGraphTimeUnit.MILLISECONDS;
		if (minumValue >= timeUnitConvertor.get(DurationGraphTimeUnit.MINUTES)) {
			unit = DurationGraphTimeUnit.MINUTES;
		} else if (minumValue < timeUnitConvertor.get(DurationGraphTimeUnit.MINUTES)
				&& minumValue >= (timeUnitConvertor.get(DurationGraphTimeUnit.MILLISECONDS) / 2)) {
			unit = DurationGraphTimeUnit.SECONDS;
		} else {
			unit = DurationGraphTimeUnit.MILLISECONDS;
		}
		return getDurationObject(duration, sessions, unit);
	}

	private SessionDurationChartResponse getDurationObject(GraphDuration duration, HashMap<String, Double> sessions,
			DurationGraphTimeUnit unit) {
		Map<String, List<ChartData<Double>>> result = new HashMap<>();
		final DurationGraphTimeUnit finalUnit = unit;
		sessions.entrySet().forEach(e -> {
			String key = e.getKey();
			String date = null;
			String time = null;
			String fullDate = null;
			if (GraphDuration.TWENTY_FOUR_HOURS.equals(duration) || GraphDuration.FORTY_EIGHT_HOURS.equals(duration)) {
				String arr[] = key.split("T");
				date = arr[0];
				time = arr[1];
				key = date;
				fullDate = e.getKey().replace("T", " ");
			}

			if (!result.containsKey(key)) {
				result.put(key, new ArrayList<>());
			}
			Double value = null;
			if (unit != null) {
				value = formattedDoubleValue(e.getValue() / timeUnitConvertor.get(finalUnit));
			} else {
				value = formattedDoubleValue(e.getValue());
			}

			if (GraphDuration.TWENTY_FOUR_HOURS.equals(duration) || GraphDuration.FORTY_EIGHT_HOURS.equals(duration)) {
				result.get(key).add(new ChartData<Double>(time, date, fullDate, value));
			} else {
				result.get(key).add(new ChartData<Double>(key, date, fullDate, value));
			}
		});

		return new SessionDurationChartResponse(result, unit,
				DateUtils.getFromatedDate(DateUtils.getCurrentTimestemp()),
				DateUtils.getFromatedDate(DateUtils.getCurrentTimestemp().minusDays(1)));
	}

	public ChartDataWithRange<Double> getApdexScore(Long accountId, GraphDuration duration) {
		List<String> indexes = getPageLoadIndexes(duration);
		SearchRequest searchRequest = new SearchRequest(String.join(",", indexes))
				.indicesOptions(IndicesOptions.fromOptions(true, false, false, false));

		String lte = DateUtils.getTime(DateUtils.getCurrentTimestemp());
		String gte = DateUtils.getTime(DateUtils.getCurrentTimestemp().minusHours(23));
		if (GraphDuration.FORTY_EIGHT_HOURS.equals(duration)) {
			gte = DateUtils.getTime(DateUtils.getCurrentTimestemp().minusHours(47));
		}
		BoolQueryBuilder q = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("accountId", accountId))
				.mustNot(QueryBuilders.termQuery("eventId", UserEvents.END));

		final String dateWiseAggName = "date_wise";
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(q);

		Map<String, String> bucketsPathsMap = new HashMap<>();
		bucketsPathsMap.put("satisfiedCount", "satisfied>_count");
		bucketsPathsMap.put("frustratedCount", "frustrated>_count");
		bucketsPathsMap.put("toleratedCount", "tolerated>_count");
		bucketsPathsMap.put("total", "totalCount");

		searchSourceBuilder.aggregation(AggregationBuilders.dateHistogram(dateWiseAggName).field("timestamp")
				.calendarInterval(DateHistogramInterval.HOUR).format(elasticAggrDateTimeFormat)
				.subAggregation(AggregationBuilders.filter("satisfied",
						QueryBuilders.rangeQuery("pageLoadTime").to(apdeskInterval * 1000)))
				.subAggregation(AggregationBuilders.filter("tolerated",
						QueryBuilders.rangeQuery("pageLoadTime").from(apdeskInterval * 1000 + 1)
								.to(apdeskInterval * 1000 * tolarateRate)))
				.subAggregation(AggregationBuilders.filter("frustrated",
						QueryBuilders.rangeQuery("pageLoadTime").from(apdeskInterval * 1000 * tolarateRate + 1)))
				.subAggregation(AggregationBuilders.cardinality("totalCount").field("id"))
				.subAggregation(PipelineAggregatorBuilders.bucketScript("score", bucketsPathsMap, new Script(
						"if (params.total == 0) { return 0;} else { (params.satisfiedCount + (params.toleratedCount / 2))/params.total }"))));

		searchRequest.source(searchSourceBuilder);
		searchSourceBuilder.size(0);
		SearchResponse searchResponse = null;
		HashMap<String, Double> result = new HashMap<>();
		try {
			searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
			Histogram histoGram = searchResponse.getAggregations().get(dateWiseAggName);
			histoGram.getBuckets().stream().forEach(e -> {
				SimpleValue value = e.getAggregations().get("score");
				if (value != null) {
					result.put(e.getKeyAsString(), formattedDoubleValue(value.value()));
				} else {
					result.put(e.getKeyAsString(), 0D);
				}
			});
		} catch (Exception e) {
			logger.info("getApdexScore()");
			e.printStackTrace();
		}
		ChartDataWithRange<Double> values = new ChartDataWithRange<Double>(result, lte, gte);
		return values;
	}

	public ChartDataWithRange<Double> getPageLoadTime(Long accountId, GraphDuration duration) {
		List<String> indexes = getPageLoadIndexes(duration);
		SearchRequest searchRequest = new SearchRequest(String.join(",", indexes))
				.indicesOptions(IndicesOptions.fromOptions(true, false, false, false));

		BoolQueryBuilder q = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("accountId", accountId))
				.mustNot(QueryBuilders.termQuery("eventId", UserEvents.END))
				.must(QueryBuilders.rangeQuery("pageLoadTime").gte(0));
		final String dateWiseAggName = "date_wise";
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(q);

		searchSourceBuilder.aggregation(AggregationBuilders.dateHistogram(dateWiseAggName).field("timestamp")
				.calendarInterval(DateHistogramInterval.HOUR).format(elasticAggrDateTimeFormat)
				.subAggregation(AggregationBuilders.avg("avgLoadTime").field("pageLoadTime")));

		searchRequest.source(searchSourceBuilder);
		searchSourceBuilder.size(0);
		SearchResponse searchResponse = null;
		HashMap<String, Double> result = new HashMap<>();
		try {
			searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
			Histogram histoGram = searchResponse.getAggregations().get(dateWiseAggName);
			histoGram.getBuckets().stream().forEach(e -> {
				Avg avg = e.getAggregations().get("avgLoadTime");
				Double val = 0D;
				if (avg != null) {
					Double v = avg.getValue();
					if (v != null && v > 0 && Double.POSITIVE_INFINITY != avg.getValue()) {
						val = formattedDoubleValue(v);
					}
				}
				result.put(e.getKeyAsString(), val);
			});
		} catch (Exception e) {
			logger.info("getPageLoadTime()");
			e.printStackTrace();
		}
		return new ChartDataWithRange<>(result, null, null);
	}

	public ConversionChartResultResponse<Double> getBounceRate(List<String> indexes, Long accountId) {
		String lte = DateUtils.getTime(DateUtils.getCurrentTimestemp());
		String gte = DateUtils.getTime(DateUtils.getCurrentTimestemp().minusHours(23));
		final int bounceRateNoOfDocuments = 2;
		String query = "{\n" + "  \"size\": 0, \n" + "  \"query\":{\n" + "      \"bool\":{\n"
				+ " \"must_not\": [{\"term\": {\"eventId\": \"" + UserEvents.END.value() + "\"}}], \"must\":[\n"
				+ "            {\n" + "               \"term\":{\n" + "                  \"accountId\":" + accountId
				+ "\n" + "               }\n" + "            },\n" + "            {\n" + "              \"range\": {\n"
				+ "                \"sessionStartTime\": {\n" + "                  \"gte\":\"" + gte + "\" ,\n"
				+ "                  \"lte\": \"" + lte + "\"\n" + "                }\n" + "              }\n"
				+ "            }\n" + "         ]\n" + "      }\n" + "   },\n" + "   \"aggs\": {\n"
				+ "     \"date_wise\": {\n" + "       \"date_histogram\":{\n"
				+ "            \"field\": \"timestamp\",\n" + "        \"interval\": \"hour\",\n"
				+ "        \"format\": \"" + elasticAggrDateTimeFormat + "\"\n" + "         },\n"
				+ "         \"aggs\": {\n" + "           \"ttlCnt\": {\n" + "             \"cardinality\": {\n"
				+ "               \"field\": \"sessionId\"\n" + "             }\n" + "           },\n"
				+ "           \"sessions\": {\n" + "                \"rare_terms\": {\n"
				+ "                \"field\": \"sessionId\",\n" + "                \"max_doc_count\": "
				+ bounceRateNoOfDocuments + "\n" + "              }\n" + "           },\n"
				+ "           \"bounceRate\": {\n" + "             \"bucket_script\": {\n"
				+ "               \"buckets_path\": {\n" + "                 \"totlCnt\": \"ttlCnt\",\n"
				+ "                 \"bounes\": \"sessions._bucket_count\"\n" + "               },\n"
				+ "               \"script\": \"params.totlCnt > 0 ? params.bounes*100/params.totlCnt  : 0\"\n"
				+ "             }\n" + "           }\n" + "         }\n" + "     }\n" + "   }\n" + "}\n";
		HashMap<String, Double> result = new HashMap<>();
		try {
			HttpHeaders httpHeaders = getHeaders();
			HttpEntity<String> httpEntity = new HttpEntity<>(query, httpHeaders);
			ResponseEntity<String> responseEntity = restTemplate.exchange(getUrl(indexes), HttpMethod.POST, httpEntity,
					String.class);
			JSONObject jsonObject = new JSONObject(responseEntity.getBody());
			JSONObject getSth = jsonObject.getJSONObject("aggregations");
			JSONObject dateWise = getSth.getJSONObject("date_wise");
			JSONArray buckets = dateWise.getJSONArray("buckets");
			try {
				buckets.forEach(e -> {
					JSONObject ob = new JSONObject(e.toString());
					String arr[] = ob.getString("key_as_string").split("T");
					result.put(ob.getString("key_as_string"),
							formattedDoubleValue(ob.getJSONObject("bounceRate").getDouble("value")));
				});
			} catch (Exception e) {
			}
		} catch (Exception e) {
			logger.info("getBounceRate()");
			e.printStackTrace();
		}
		Double percentage = 0D;
		percentage = currentHourCount(result);
		return new ConversionChartResultResponse<Double>(result, null, percentage, getLastHourChanges(result));
	}

	private String getUrl(List<String> indexes) {
		return "https://" + EsHost + ":" + EsPort + "/" + String.join(",", indexes)
				+ "/_search?ignore_unavailable=true";
	}

	private HttpHeaders getHeaders() {
		// String adminuserCredentials = username + ":" + password;
		// String encodedCredentials = new
		// String(Base64.encodeBase64(adminuserCredentials.getBytes()));
		HttpHeaders httpHeaders = new HttpHeaders();
		// httpHeaders.add("Authorization", "Basic " + encodedCredentials);
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		return httpHeaders;
	}

	public String getPageLoadIndexName(OffsetDateTime date) {
		return pageLoadPrefix + DateUtils.getFromatedDate(date).replaceAll("-", "");
	}

	public String getPageLoadCurrentDateIndexName() {
		return getPageLoadIndexName(DateUtils.getCurrentTimestemp());
	}

	public ConversionChartResultResponse<Double> getAvarageUserSpend(List<String> indexes, Long accountId) {

		SearchRequest searchRequest = new SearchRequest(String.join(",", indexes))
				.indicesOptions(IndicesOptions.fromOptions(true, false, false, false));

		String lte = DateUtils.getTime(DateUtils.getCurrentTimestemp());
		String gte = DateUtils.getTime(DateUtils.getCurrentTimestemp().minusHours(24));

		BoolQueryBuilder q = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("accountId", accountId))
				.must(QueryBuilders.termsQuery("eventId", Arrays.asList(UserEvents.BUY)))
				.must(QueryBuilders.rangeQuery("timestamp").lte(lte).gte(gte));

		final String aggGlobalAggName = "dateWiseUserSpent";
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(q);

		Map<String, String> bucketsPathsMap = new HashMap<>();
		bucketsPathsMap.put("spent", "totalSpent");
		bucketsPathsMap.put("users", "userCount");
		searchSourceBuilder
				.aggregation(AggregationBuilders.dateHistogram(aggGlobalAggName).field("timestamp")
						.calendarInterval(DateHistogramInterval.HOUR).format(elasticAggrDateTimeFormat)
						.subAggregation(AggregationBuilders.sum("totalSpent").field("saleAmount"))
						.subAggregation(AggregationBuilders.cardinality("userCount").field("userId"))
						.subAggregation(PipelineAggregatorBuilders.bucketScript("avgSpent", bucketsPathsMap, new Script(
								"BigDecimal.valueOf(params.spent/params.users).setScale(2, RoundingMode.HALF_UP)"))))
				.aggregation(AggregationBuilders.sum("totlSpent").field("saleAmount"))
				.aggregation(AggregationBuilders.cardinality("totalUSR").field("userId"));

		searchRequest.source(searchSourceBuilder);
		searchSourceBuilder.size(0);
		SearchResponse searchResponse = null;
		HashMap<String, Double> result = new HashMap<>();
		Double totlSpent = 0D;
		Long totalUSR = 0L;
		try {
			searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
			Histogram histoGram = searchResponse.getAggregations().get(aggGlobalAggName);
			Sum sum = searchResponse.getAggregations().get("totlSpent");
			Cardinality cardinality = searchResponse.getAggregations().get("totalUSR");
			if (sum != null) {
				totlSpent = sum.getValue();
			}
			if (cardinality != null) {
				totalUSR = cardinality.getValue();
			}

			histoGram.getBuckets().stream().forEach(e -> {
				SimpleValue value = e.getAggregations().get("avgSpent");
				if (value != null) {
					result.put(e.getKeyAsString(), value.value());
				}
			});
		} catch (Exception e) {
			logger.info("getAvarageUserSpend()");
			e.printStackTrace();
		}
//		count = currentHourCount(result);
		Double count = 0D;
		if (totalUSR > 0) {
			count = formattedDoubleValue(totlSpent / totalUSR);
		}
		return new ConversionChartResultResponse<Double>(result, count, null, getLastHourChanges(result));
	}

	public LiveDashboardChartData getMultiSearchStatastics(List<String> indexes, Long accountId) {

		LivePageChartData sessionData = new LivePageChartData();
		ConversionChartResultResponse<Double> totalRate = new ConversionChartResultResponse<>();
		ConversionChartResultResponse<Double> activeUsers = new ConversionChartResultResponse<>();
		ConversionChartResultResponse<Long> overallRate = new ConversionChartResultResponse<>();

		MultiSearchRequest request = new MultiSearchRequest();

		SearchRequest overallRatRequest = overAllConversitionRateSearchRequest(indexes, accountId, true, true);
		SearchRequest sessionDataRequest = getActiveUsersChartDataEsRequest(indexes, accountId, true);
		SearchRequest totalRateRequest = getTotalConversionsEsRequest(indexes, accountId, true);
		SearchRequest activeUsersRequest = getActiveUsersEsRequest(indexes, accountId, true);
		request.add(overallRatRequest);
		request.add(sessionDataRequest);
		request.add(totalRateRequest);
		request.add(activeUsersRequest);
		request.indicesOptions(IndicesOptions.fromOptions(true, false, false, false));

		try {
			MultiSearchResponse response = elasticsearchClient.msearch(request, RequestOptions.DEFAULT);
			overallRate = processAllConversitionRateEsResponse(response.getResponses()[0].getResponse(), true);
			sessionData = processActiveUserChartDataResponse(response.getResponses()[1].getResponse());
			totalRate = processGetTotalConversions(response.getResponses()[2].getResponse());
			activeUsers = processGetActiveUsersEsResponse(response.getResponses()[3].getResponse());

		} catch (Exception e) {
			logger.info("getMultiSearchStatastics() Error");
			e.printStackTrace();
		}

		return new LiveDashboardChartData(sessionData, overallRate, totalRate, activeUsers);
	}
}