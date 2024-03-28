package com.base.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.base.api.dto.filter.FilterBase;
import com.base.api.dto.filter.ReviewFilter;
import com.base.api.entities.Review;
import com.base.api.exception.APIException;
import com.base.api.gateway.util.Util;
import com.base.api.repository.ReviewRepository;
import com.base.api.request.dto.ReviewDTO;
import com.base.api.service.ReviewService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author kavitha_deshagani
 *
 */
@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	EntityManager entityManager;

	@Autowired
	ReviewRepository reviewRepo;

	@Autowired
	ReviewService reviewService;

	/**
	 * This is used to implement the all reviews
	 */
	@Override
	public List<Review> getReviews() {
		log.info("ReviewServiceImpl: Start getAllReviews");

		List<Review> reviewList = reviewRepo.findAll();
		return reviewList;
	}

	/**
	 * This implementation is used to delete the review
	 */
	@Override
	public String deleteReview(UUID reviewId) {

		log.info("ReviewServiceImpl : deleteReview");
		Optional<Review> review = reviewRepo.findById(reviewId);

		if (review != null) {
			reviewRepo.deleteById(reviewId);
			log.info("review deleted successfully..!");
			return HttpStatus.OK.name();
		} else {
			log.error("No such review is found with id :" + reviewId);
			return HttpStatus.NOT_FOUND.name();
		}
	}

	/**
	 * This implementation is used to add the data.
	 * 
	 * @return
	 */
	@Transactional
	@Override
	public Review addReview(ReviewDTO reviewDTO) {
		log.info("ReviewServiceImpl : addReview");
		Review newReview = new Review(reviewDTO);
		Review createdReview = reviewRepo.save(newReview);
		log.info("newReview is created" + newReview.toString());
		if (null != createdReview.getId() || null != createdReview.getId()) {
			return reviewRepo.save(createdReview);
		} else {
			log.error("Getting issue while creating review");
			throw new APIException("failed.to.add.review.data");
		}
	}

	/**
	 * This implementation is used to edit the review
	 */
	@Transactional
	@Override
	public void editReview(UUID reviewId, ReviewDTO reviewDTO) {

		log.info("ReviewServiceImpl : editReview");

		Optional<Review> reviewOptional = reviewRepo.findById(reviewId);
		if (reviewOptional.isPresent() && null != reviewOptional.get()) {
			Review review = reviewOptional.get();
			reviewRepo.save(review);
			log.info("edit review successfully...");
		} else {
			throw new APIException("invalid.id.was.provided..!");
		}

	}

	@Override
	public List<Review> getReviews(ReviewFilter reviewFilter) {

		StringBuilder query = new StringBuilder();
		query = createCommonQuery();

		if (reviewFilter.getSearch_keyword() != null && !reviewFilter.getSearch_keyword().isEmpty()) {
			query.append(" where (LOWER(upe1.firstName) LIKE '%" + reviewFilter.getSearch_keyword().toLowerCase()
					+ "%' or LOWER(upe1.lastName) LIKE '%" + reviewFilter.getSearch_keyword().toLowerCase()
					+ "%' or LOWER(upe2.firstName) LIKE '%" + reviewFilter.getSearch_keyword().toLowerCase()
					+ "%' or LOWER(upe2.lastName) LIKE '%" + reviewFilter.getSearch_keyword().toLowerCase() + "%')");
		}

		String columnName = "u." + reviewFilter.getSort_param();
		if (reviewFilter.getSort_param().startsWith("from")) {
			columnName = "u.fromName";
		} else if (reviewFilter.getSort_param().startsWith("to")) {
			columnName = "u.toName";
		}

		FilterBase filterBase = new FilterBase();
		filterBase.setColumnName(columnName);
		filterBase.setStartRec(reviewFilter.getStartRec());
		filterBase.setEndRec(reviewFilter.getEndRec());
		filterBase.setSortingOrder(reviewFilter.getSort_type());
		filterBase.setOrder(reviewFilter.getOrder());
		String queryParam = Util.getFilterQuery(filterBase, query.toString());
		List<Review> results = new ArrayList<Review>();
		results = entityManager.createQuery(queryParam).setFirstResult(Integer.valueOf(reviewFilter.getStartRec()))
				.setMaxResults(Integer.valueOf(reviewFilter.getEndRec())).getResultList();
		return results;
	}

	private StringBuilder createCommonQuery() {
		StringBuilder query = new StringBuilder();
		query.append(
				"select u from Review u join u.fromName u1 join u.toName u2 join u1.userProfile upe1 join u2.userProfile upe2 ");
		return query;
	}

}
