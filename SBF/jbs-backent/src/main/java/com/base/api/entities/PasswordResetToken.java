package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "password_reset_token")
@AttributeOverride(name = "id", column = @Column(name = "password_reset_token_id"))
public class PasswordResetToken extends BaseEntity {

	
	private static final long serialVersionUID = -7162334439537054007L;

	@Column(name = "token", nullable = false)
	private String token;

	@ManyToOne
	private User user;

	@Column(name = "is_expired")
	private Boolean isExpired;

}