package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.base.api.dto.filter.OfferFilter;

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
@Table(name = "offers")
@AttributeOverride(name = "id", column = @Column(name = "offer_id"))
public class Offer extends BaseEntity {

	private static final long serialVersionUID = 9021402814531307168L;

	@Column(name = "name")
	private String name;

	@Column(name = "code")
	private String code;

	@Column(name = "type")
	private String type;

	@Column(name = "value")
	private int value;

	@Column(name = "start_date")
	private String startDate;

	@Column(name = "end_date")
	private String endDate;

	@Column(name = "applicable")
	private String applicable;

	@Column(name = "usage")
	private String usage;
	
	@Column(name = "status")
	private String status;
	
	public Offer(OfferFilter filter) {
		this.name = filter.name;
		this.code = filter.code;
		this.type = filter.type;
		this.value = filter.value;
		this.startDate = filter.start_date;
		this.endDate = filter.end_date;
		this.applicable  = filter.applicable;
		this.usage = filter.usage;
		this.status = "Active";
	}
}
