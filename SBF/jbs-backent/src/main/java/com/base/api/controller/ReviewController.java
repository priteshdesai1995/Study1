package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.PermissionConstants.ADD_REVIEW;
import static com.base.api.constants.PermissionConstants.CHANGE_REVIEW_STATUS;
import static com.base.api.constants.PermissionConstants.DELETE_REVIEW;
import static com.base.api.constants.PermissionConstants.EDIT_REVIEW;
import static com.base.api.constants.PermissionConstants.GET_ALL_REVIEWS;
import static com.base.api.constants.PermissionConstants.PERMISSION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.constants.Constants;
import com.base.api.dto.filter.ReviewFilter;
import com.base.api.entities.Review;
import com.base.api.exception.APIException;
import com.base.api.repository.ReviewRepository;
import com.base.api.request.dto.ReviewDTO;
import com.base.api.service.ReviewService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kavitha_deshagani
 *
 */
@Slf4j
@RestController
@RequestMapping("/review")
public class ReviewController {

	@Autowired
	ReviewService reviewService;

	@Autowired
	ReviewRepository reviewRepo;

	@Autowired
	ResourceBundle resourceBundle;

	@PostMapping(value = "/list", headers = { "Version=V1" }, produces = APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to Get list of the Reviews", notes = PERMISSION
			+ GET_ALL_REVIEWS, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_ALL_REVIEWS })
	public ResponseEntity<TransactionInfo> getReviews(@RequestBody ReviewFilter reviewFilter) {

		log.info("ReviewController : getReviews");
		List<Review> result = reviewService.getReviews(reviewFilter);
		if (result == null) {
			log.error("Review not found");
			return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
		}
		log.info("ReviewController :: End-get-all-reviews");
		log.info("Get the Reviews from database");
		return ResponseBuilder.buildOkResponse(result);
	}

	/**
	 * This Rest API is used to get all reviews
	 * 
	 * @param reviewDTO
	 * @return
	 */
	@GetMapping(value = "/list")
	@ApiOperation(value = "API Endpoint to Get all the Reviews", notes = PERMISSION
			+ GET_ALL_REVIEWS, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_ALL_REVIEWS })
	public ResponseEntity<TransactionInfo> getReviews() {
		log.info("ReviewController :: start-get-all-reviews");
		List<Review> listOfReviews = reviewService.getReviews();
		if (listOfReviews == null) {
			log.error("There is no Reviews available here:");
			return ResponseBuilder.buildRecordNotFoundResponse(listOfReviews,
					resourceBundle.getString("record_not_found"));
		}
		log.info("ReviewController :: End-get-all-reviews");
		log.info("Get the all Reviews from database");
		return ResponseBuilder.buildOkResponse(listOfReviews);
	}

	/**
	 * This Rest API is used to add the review
	 * 
	 * @param reviewDTO
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/add")
	@ApiOperation(value = "API Endpoint to change the Reviews", notes = PERMISSION + ADD_REVIEW, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { ADD_REVIEW })
	public ResponseEntity<TransactionInfo> addReview(@RequestBody ReviewDTO reviewDTO) throws Exception {
		log.info("ReviewController :: StartAdd");
		Review review = reviewService.addReview(reviewDTO);
		if (review != null) {
			log.info("Review addeded successfull");
			return ResponseBuilder.buildCRUDResponse(review, resourceBundle.getString("review_added"), HttpStatus.OK);
		}

		log.info("ReviewController :: EndAdd");
		return ResponseBuilder.buildRecordNotFoundResponse(null, resourceBundle.getString("review_add_issue"));
	}

	/**
	 * This Rest API is used to change the status
	 * 
	 * @param statusReq
	 * @return
	 */
	@PostMapping(value = "/change_status")
	@ApiOperation(value = "API Endpoint to change the status of Reviews", notes = PERMISSION
			+ CHANGE_REVIEW_STATUS, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CHANGE_REVIEW_STATUS })
	public ResponseEntity<TransactionInfo> changeReviewStatus(@RequestBody Map<String, String> statusReq) {

		log.info("RuleController:updateReviewStatus");
		Review result = reviewRepo.findById(UUID.fromString(statusReq.get("reviewId"))).get();

		if (result != null) {

			result.setStatus(statusReq.get("status"));
			Review updatedReview = reviewRepo.save(result);
			if (updatedReview != null) {
				return ResponseBuilder.buildStatusChangeResponse(updatedReview, resourceBundle.getString("status"));
			}
		}
		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}

	/**
	 * This Rest API is used to delete the review
	 * 
	 * @param reviewId
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	@ApiOperation(value = "API Endpoint to delete the Reviews", notes = PERMISSION + DELETE_REVIEW, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { DELETE_REVIEW })
	public ResponseEntity<TransactionInfo> deleteReview(@RequestParam("review_id") UUID reviewId) {
		log.info("ReviewController :: StartDelete ");
		Optional<Review> reviewOptional = reviewRepo.findById(reviewId);
		if (null == reviewOptional.get() || !reviewOptional.isPresent()) {
			throw new APIException("no.such.review.found" + ":" + reviewId, HttpStatus.NOT_FOUND);
		}
		String result = reviewService.deleteReview(reviewId);
		log.info("Review Deleted successfully");
		log.info("ReviewController :: EndDelete");
		if (result.equals(HttpStatus.OK.name())) {

			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("review_deleted"), HttpStatus.OK);

		} else if (result.equals(HttpStatus.NOT_FOUND.name())) {

			return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
		}

		return ResponseBuilder.buildInternalServerErrorResponse(result, resourceBundle.getString("review_delete_fail"));
	}

	/**
	 * This Rest API is used to add the review
	 * 
	 * @param reviewDTO
	 * @param reviewId
	 * @return
	 * @throws Exception
	 */
	@PutMapping(value = "/edit/{reviewId}")
	@ApiOperation(value = "API Endpoint to Update the Data", notes = PERMISSION + EDIT_REVIEW, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { EDIT_REVIEW })
	public ResponseEntity<TransactionInfo> editReview(@Valid @NotNull @RequestBody ReviewDTO reviewDTO,
			@PathVariable("reviewId") UUID reviewId) throws Exception {
		log.info("ReviewController :: startEdit");
		log.info("API hit of the edit review");
		Optional<Review> data = reviewRepo.findById(reviewId);
		Review editReview = data.get();
		if (editReview == null) {
			log.error("Not able to fetch data");
			return ResponseBuilder.buildInternalServerErrorResponse(editReview,
					resourceBundle.getString("review_not_updated"));
		}
		editReview.update(reviewDTO);
		Review review = reviewRepo.save(editReview);
		if (review != null) {
			log.info("Review data edited successfully..!");
			log.info("ReviewController :: EndEdit");
			return ResponseBuilder.buildCRUDResponse(editReview, resourceBundle.getString("review_updated"),
					HttpStatus.OK);
		}

		return ResponseBuilder.buildRecordNotFoundResponse(editReview, resourceBundle.getString("record_not_found"));

	}

}
