/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.base.api.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This class is an entity that represents Announcement and User mapping objects
 * in the database.
 * 
 * @author minesh_prajapati
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "announcement_users")
@AttributeOverride(name = "id", column = @Column(name = "announcement_user_id"))
public class AnnouncementUser extends BaseEntity {

	private static final long serialVersionUID = 8895498328173444574L;

	@OneToOne
	@JoinColumn(name = "announcement_id")
	private Announcement announcement;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User users;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status = Status.PENDING;

	public AnnouncementUser(Announcement announcement, User user) {
		this.announcement = announcement;
		this.users = user;
	}
}
