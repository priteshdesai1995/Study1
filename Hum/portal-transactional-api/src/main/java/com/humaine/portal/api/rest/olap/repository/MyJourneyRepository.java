package com.humaine.portal.api.rest.olap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.olap.model.OLAPMyJourney;
import com.humaine.portal.api.response.dto.MyJourneyDBResponse;

@Repository
public interface MyJourneyRepository extends CrudRepository<OLAPMyJourney, Long> {

	@Query(value = "SELECT j.id as id, j.account_id as account, j.group_id as groupId, j.journey_steps as journeySteps, j.journey_success as journeySuccess, j.journey_time as journeyTime  FROM my_journey j WHERE j.account_id=:accountID and j.created_on  = (select max(mj.created_on) from my_journey mj where mj.account_id =:accountID) order by j.journey_success desc", nativeQuery = true)
	List<MyJourneyDBResponse> getMyJourneyList(@Param("accountID") Long account);

	@Query(value = "Select j from OLAPMyJourney j where j.account =:accountID and j.id =:journeyID")
	OLAPMyJourney getJourneyById(@Param("accountID") Long accountId, @Param("journeyID") Long journeyId);

	@Query(value = "SELECT j FROM OLAPMyJourney j WHERE j.account=:accountID AND j.id IN :journeyIds")
	List<OLAPMyJourney> getJourneyByIds(@Param("accountID") Long accountId, @Param("journeyIds") List<Long> ids);

	@Query(value = "SELECT j.id as id, j.account_id as account, j.group_id as groupId, j.journey_steps as journeySteps, j.journey_success as journeySuccess, j.journey_time as journeyTime, j.first_interest as firstInterest, j.decision as decison, j.purchase_add_cart as purchaseAddCart, j.purchase_buy as purchaseBuy, j.purchase_ownership as purchaseOwnership FROM my_journey j WHERE j.account_id=:accountID and j.id =:journeyID ", nativeQuery = true)
	MyJourneyDBResponse findJourneyById(@Param("accountID") Long accountId, @Param("journeyID") Long journeyId);

}
