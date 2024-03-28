package com.base.api.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.base.api.request.dto.ActivityLogObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "user_activity_log")
@AttributeOverride(name = "id", column = @Column(name = "id"))
public class Activity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 4422472300836094801L;

	@Column(name = "status", nullable = false)
	private String status;

	@Column(name = "action")
	private String action;

	@Column(name = "ip_address")
	private String ipAddress;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "created_at")
	OffsetDateTime createdAt;

	public Activity(ActivityLogObject log, User usr) {
		this.status = log.getStatus().toString();
		this.action = log.getAction();
		this.ipAddress = log.getIp();
		this.user = usr;
		this.createdAt = log.getTime();
	}

}
