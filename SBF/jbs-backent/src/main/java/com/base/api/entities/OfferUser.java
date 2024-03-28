package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "offer_users")
@AttributeOverride(name = "id", column = @Column(name = "offer_user_id"))
public class OfferUser extends BaseEntity{

	private static final long serialVersionUID = -6369987612305910991L;
	
	@OneToOne
	private Offer offer;
	
	@OneToOne
	private User user;

}

