package com.humaine.collection.api.rest.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.collection.api.es.projection.model.ElasticPageLoadData;
import com.humaine.collection.api.model.PageLoadData;

@Repository
public interface PageLoadDataRepository extends CrudRepository<PageLoadData, Long> {
	@Query(value = "select pd.id as id, pd.accountid as accountId,pd.userid as userId,pd.sessionid  as sessionId, pd.pageurl as pageURL, pd.loadtime as pageLoadTime, pd.\"timestamp\"\\:\\:text from pageload_data pd where date(pd.\"timestamp\")=date(:dateInput)", nativeQuery = true)
	List<ElasticPageLoadData> getPageLoadData(@Param("dateInput") OffsetDateTime date);

	@Query(value = "select pd.id as id, pd.accountid as accountId,pd.userid as userId,pd.sessionid  as sessionId, pd.pageurl as pageURL, pd.loadtime as pageLoadTime, pd.\"timestamp\"\\:\\:text from pageload_data pd where date(pd.\"timestamp\")=date(:dateInput) and pd.id >:id", nativeQuery = true)
	List<ElasticPageLoadData> getPageLoadData(@Param("id") Long id, @Param("dateInput") OffsetDateTime date);
	
	@Query(value = "select pd.id as id, pd.accountid as accountId,pd.userid as userId,pd.sessionid  as sessionId, pd.pageurl as pageURL, pd.loadtime as pageLoadTime, pd.\"timestamp\"\\:\\:text from pageload_data pd where date(pd.\"timestamp\")=date(:dateInput) and pd.id = :id", nativeQuery = true)
	ElasticPageLoadData getPageLoadObject(@Param("dateInput") OffsetDateTime date, @Param("id") Long id);
}
