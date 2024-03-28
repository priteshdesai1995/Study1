package com.base.api.service;

import java.util.List;
import java.util.UUID;

import com.base.api.dto.filter.ReviewFilter;
import com.base.api.entities.Review;
import com.base.api.request.dto.ReviewDTO;

public interface ReviewService {

	List<Review> getReviews();
	
	String deleteReview(UUID reviewId);

	Review addReview(ReviewDTO reviewDTO);

	void editReview(UUID reviewId, ReviewDTO reviewDTO);
	
	List<Review> getReviews(ReviewFilter reviewFilter);
	
}
