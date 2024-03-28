package com.humaine.collection.api.rest.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.collection.api.es.projection.model.ElasticUserEvent;
import com.humaine.collection.api.model.UserEvent;

@Repository
public interface UserEventRepository extends CrudRepository<UserEvent, Long> {
	@Query(value = "SELECT ue.usereventid,us.userid,u.externaluserid,u.bodegaid, u.deviceid , u.devicetype , u.pageloadtime,us.sessionid, us.city , us.state , us.country , us.lat, us.long as lon, us.starttime\\:\\:timestamp\\:\\:text ,us.endtime\\:\\:timestamp\\:\\:text,ue.accountid, ue.eventid , im.productid , im.name , im.description , im.category,im.price , im.relatedimages, sd.saleid, sd.saleamount, sd.productquantity, sd.saleon\\:\\:timestamp\\:\\:text, ue.pageurl ,ue.menu_id , ue.menu_name , ue.menu_url, ue.post_id , ue.post_title , ue.social_media_platform ,ue.social_media_url , ue.\"timestamp\"\\:\\:timestamp\\:\\:text  FROM userevent ue left join usersession us on ue.sessionid = us.sessionid left join \"user\" u on us.userid = u.userid left join inventorymaster im on ue.productid = im.productid left join saledata sd on ue.usereventid = sd.usereventid where date(ue.\"timestamp\")=date(:dateInput)", nativeQuery = true)
	List<ElasticUserEvent> getUserEventsData(@Param("dateInput") OffsetDateTime date);

	@Query(value = "SELECT ue.usereventid,us.userid,u.externaluserid,u.bodegaid, u.deviceid , u.devicetype , u.pageloadtime,us.sessionid, us.city , us.state , us.country , us.lat, us.long as lon, us.starttime\\:\\:timestamp\\:\\:text , us.endtime\\:\\:timestamp\\:\\:text,ue.accountid, ue.eventid , im.productid , im.name , im.description , im.category,im.price , im.relatedimages, sd.saleid, sd.saleamount, sd.productquantity, sd.saleon\\:\\:timestamp\\:\\:text, ue.pageurl ,ue.menu_id , ue.menu_name , ue.menu_url, ue.post_id , ue.post_title , ue.social_media_platform ,ue.social_media_url , ue.timestamp\\:\\:timestamp\\:\\:text FROM userevent ue left join usersession us on ue.sessionid = us.sessionid left join \"user\" u on us.userid = u.userid left join inventorymaster im on ue.productid = im.productid left join saledata sd on ue.usereventid = sd.usereventid WHERE ue.usereventid > :userEventID AND date(ue.\"timestamp\")=date(:dateInput)", nativeQuery = true)
	List<ElasticUserEvent> getUserEventsData(@Param("userEventID") Long userEventID,
			@Param("dateInput") OffsetDateTime date);

	@Query(value = "SELECT ue.usereventid,us.userid,u.externaluserid,u.bodegaid, u.deviceid , u.devicetype , u.pageloadtime,us.sessionid, us.city , us.state , us.country , us.lat, us.long as lon, us.starttime\\:\\:timestamp\\:\\:text ,us.endtime\\:\\:timestamp\\:\\:text,ue.accountid, ue.eventid , im.productid , im.name , im.description , im.category,im.price , im.relatedimages, sd.saleid, sd.saleamount, sd.productquantity, sd.saleon\\:\\:timestamp\\:\\:text, ue.pageurl ,ue.menu_id , ue.menu_name , ue.menu_url, ue.post_id , ue.post_title , ue.social_media_platform ,ue.social_media_url , ue.\"timestamp\"\\:\\:timestamp\\:\\:text  FROM userevent ue left join usersession us on ue.sessionid = us.sessionid left join \"user\" u on us.userid = u.userid left join inventorymaster im on ue.productid = im.productid left join saledata sd on ue.usereventid = sd.usereventid where ue.usereventid=:id", nativeQuery = true)
	ElasticUserEvent getUserEventObject(@Param("id") Long id);
	
	@Query(value = "SELECT ue.usereventid,us.userid,u.externaluserid,u.bodegaid, u.deviceid , u.devicetype , u.pageloadtime,us.sessionid, us.city , us.state , us.country , us.lat, us.long as lon, us.starttime\\:\\:timestamp\\:\\:text ,us.endtime\\:\\:timestamp\\:\\:text,ue.accountid, ue.eventid , im.productid , im.name , im.description , im.category,im.price , im.relatedimages, sd.saleid, sd.saleamount, sd.productquantity, sd.saleon\\:\\:timestamp\\:\\:text, ue.pageurl ,ue.menu_id , ue.menu_name , ue.menu_url, ue.post_id , ue.post_title , ue.social_media_platform ,ue.social_media_url , ue.\"timestamp\"\\:\\:timestamp\\:\\:text  FROM userevent ue left join usersession us on ue.sessionid = us.sessionid left join \"user\" u on us.userid = u.userid left join inventorymaster im on ue.productid = im.productid left join saledata sd on ue.usereventid = sd.usereventid where ue.usereventid IN :ids", nativeQuery = true)
	List<ElasticUserEvent> getUserEventObject(@Param("ids") List<Long> ids);
}
