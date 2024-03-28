package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.base.api.request.dto.ReviewDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "reviews")
@AttributeOverride(name = "id", column = @Column(name = "review_id"))
public class Review  extends BaseEntity { 

	private static final long serialVersionUID = -1776020010629869380L;

	@OneToOne
	@JoinColumn(name = "from_name")
	private User fromName;

	@OneToOne
	@JoinColumn(name = "to_name")
	private User toName;

	@Column(name = "rating")
	private Integer rating;

	@Column(name = "review")
	private String review;

	@Column(name = "status")
	private String status;
	
	@Column(name = "isActive")
	private boolean isActive = true;
	
	public Review(ReviewDTO reviewDTO) {

		this.rating = reviewDTO.getRating();
		this.review = reviewDTO.getReview();
		this.isActive = reviewDTO.isActive();
		this.status = reviewDTO.getStatus();
	}

	public void update(@Valid @NotNull ReviewDTO reviewDTO) {
		this.setRating(reviewDTO.getRating());
		this.setReview(reviewDTO.getReview());
		this.setActive(reviewDTO.isActive());
		this.setStatus(reviewDTO.getStatus());
		
	}	
}
