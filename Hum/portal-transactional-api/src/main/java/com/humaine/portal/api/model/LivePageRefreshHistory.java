package com.humaine.portal.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.TypeDef;

import com.humaine.portal.api.converter.JsonType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This model will create a table named live_page_refresh_history
 * which keep record of last refreshed time
 *
 */
@Entity
@Table(name = "live_page_refresh_history")
@NoArgsConstructor
@Getter
@Setter
@TypeDef(name = "JsonType", typeClass = JsonType.class)
public class LivePageRefreshHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "refresh_history_id", unique = true, nullable = false)
	Long id;
	
	@Column(name = "accountid", unique = true)
	Long account;
	
	@Column(name = "timestamp")
	OffsetDateTime timestamp;

	public LivePageRefreshHistory(Long account, OffsetDateTime timestamp) {
		super();
		this.account = account;
		this.timestamp = timestamp;
	}
	
}
