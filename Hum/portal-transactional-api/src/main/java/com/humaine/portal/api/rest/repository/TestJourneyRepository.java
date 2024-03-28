package com.humaine.portal.api.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.TestJourney;
import com.humaine.portal.api.response.dto.JournyResponse;

@Repository
public interface TestJourneyRepository extends CrudRepository<TestJourney, Long> {

	@Query(value = "SELECT new com.humaine.portal.api.response.dto.JournyResponse(j, u.name as groupName, u.bigFive.value as bigFive) FROM TestJourney j LEFT JOIN UserGroup u ON u.id = j.groupId WHERE j.account.id=:accountID")
	List<JournyResponse> getJourneyList(@Param("accountID") Long account);

	@Query(value = "SELECT j FROM TestJourney j WHERE j.account.id=:accountID AND j.id=:journeyId")
	TestJourney getJourneyById(@Param("accountID") Long account, @Param("journeyId") Long journeyId);

	@Query(value = "SELECT j FROM TestJourney j WHERE j.account.id=:accountID AND j.id IN :journeyIds")
	List<TestJourney> getJourneyByIds(@Param("accountID") Long account, @Param("journeyIds") List<Long> journeyIds);

	@Query(value = "SELECT COUNT(j) FROM TestJourney j WHERE j.account.id=:accountID AND j.groupId=:groupId")
	Long getJourniesCountByGroupId(@Param("accountID") Long account, @Param("groupId") Long groupId);

	@Query(value = "SELECT new com.humaine.portal.api.response.dto.JournyResponse(j, u.name as groupName, u.bigFive.value as bigFive) FROM TestJourney j LEFT JOIN UserGroup u ON u.id = j.groupId WHERE j.account.id=:accountID AND j.id=:journeyId")
	JournyResponse getJourneyFullDetailsById(@Param("accountID") Long account, @Param("journeyId") Long journeyId);

	@Query(value = "SELECT COUNT(j) FROM TestJourney j WHERE j.account.id=:accountID")
	Long getJourniesCountByAccount(@Param("accountID") Long account);

}
