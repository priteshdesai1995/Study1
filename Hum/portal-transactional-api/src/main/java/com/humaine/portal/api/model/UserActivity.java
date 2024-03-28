package com.humaine.portal.api.model;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.humaine.admin.api.log.ActivityLogObject;

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
public class UserActivity implements Serializable {

	private static final long serialVersionUID = -1217744708353881940L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Long id;

	@Column(name = "status", nullable = false)
	private String status;

	@Column(name = "action")
	private String action;

	@Column(name = "ip_address")
	private String ipAddress;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserAdmin user;

	@Column(name = "time")
	OffsetDateTime time;

	public UserActivity(ActivityLogObject log, UserAdmin usr) {
		this.status = log.getStatus().toString();
		this.action = log.getAction();
		this.ipAddress = log.getIp();
		this.user = usr;
		this.time = log.getTime();
	}
}
