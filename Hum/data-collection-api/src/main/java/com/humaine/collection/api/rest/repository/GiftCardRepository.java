package com.humaine.collection.api.rest.repository;

import java.time.OffsetDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.collection.api.model.GiftCard;

@Repository
public interface GiftCardRepository extends JpaRepository<GiftCard, Long> {

	@Query(value = "SELECT c FROM GiftCard c where surveyUuid =:uuid")
	GiftCard findBySurveyUuid(@Param("uuid") String surveyUuid);

	@Query(value = "SELECT c FROM GiftCard c where c.userId =:userId AND Date(c.surveyStartTime) =Date(:startedOn)")
	GiftCard findGiftCardByUserIdAndDate(@Param("userId") String userId, @Param("startedOn") OffsetDateTime startedOn);

	@Query(value = "SELECT c FROM GiftCard c where c.userId =:userId")
	GiftCard findGiftCardByUserId(@Param("userId") String userId);

	@Query(value = "SELECT COUNT(*) FROM GiftCard c where Date(c.surveyStartTime)=Date(:startedOn)")
	Long findGiftCardCountByDate(@Param("startedOn") OffsetDateTime startedOn);

	@Query(value = "SELECT COUNT(*) FROM GiftCard c WHERE c.surveyEndTime IS NOT NULL")
	Long findGiftCardCount();
}
