package com.humaine.portal.api.rest.olap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.ManualJourneyData;
import com.humaine.portal.api.olap.model.OLAPManualJourney;

@Repository
public interface OLAPManualJourneyRepository extends CrudRepository<OLAPManualJourney, Long> {

	@Query(value = "select journey_id as journeyId, journey_success as successMatch, journey_time as journeyTime from humai_olap.test_journey mj where journey_id IN (:ids) and date(created_on) = (select max(date(mjs.created_on)) from humai_olap.test_journey mjs where mjs.account_id =:accountID and mjs.journey_id in (:ids))", nativeQuery = true)
	List<ManualJourneyData> getGroupsRankByIds(@Param("accountID") Long account, @Param("ids") List<Long> ids);

	@Query(value = "select journey_id as journeyId, journey_success as successMatch, journey_time as journeyTime from test_journey where journey_id = :id and date(created_on) =(select max(date(mjs.created_on)) from test_journey mjs where mjs.account_id  =:accountID and mjs.journey_id = :id)", nativeQuery = true)
	ManualJourneyData getGroupRankByIds(@Param("accountID") Long account, @Param("id") Long id);
}
