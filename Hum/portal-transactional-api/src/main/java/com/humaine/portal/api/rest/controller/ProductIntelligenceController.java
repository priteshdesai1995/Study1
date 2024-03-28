package com.humaine.portal.api.rest.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humaine.portal.api.enums.SortOrder;
import com.humaine.portal.api.exception.APIException;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.projection.model.WeeklyPopularProduct;
import com.humaine.portal.api.request.dto.ProductIntelligenceListRequest;
import com.humaine.portal.api.request.dto.SortBy;
import com.humaine.portal.api.response.dto.ProductListResponse;
import com.humaine.portal.api.rest.olap.repository.OLAPUserPersonaRepository;
import com.humaine.portal.api.rest.repository.UserEventRepository;
import com.humaine.portal.api.rest.service.AuthService;
import com.humaine.portal.api.rest.service.ProductIntelligenceService;
import com.humaine.portal.api.security.config.AWSConfig;
import com.humaine.portal.api.util.ErrorMessageUtils;
import com.humaine.portal.api.util.ResponseBuilder;
import com.humaine.portal.api.util.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "Merchandise Product Intelligence", description = "Merchandise Product Intelligence")
@RequestMapping("/merchandise/intelligence/products")
@Slf4j
public class ProductIntelligenceController {

	@Autowired
	private ErrorMessageUtils errorMessageUtils;
	
	@Autowired
	private OLAPUserPersonaRepository userPersonaRepository;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private UserEventRepository userEventRepository;
		
	@Value("${product.default.title}")
	private String defaultProductTitle;

	@Autowired
	private ProductIntelligenceService productIntelligenceService;
	
	@PostMapping(value = "", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get All Products", notes = "Get All Products", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> signUpRequest(@RequestBody ProductIntelligenceListRequest request, HttpServletRequest httpRequest)
			throws Exception {
		
		log.info("api called ");
		
		Integer size = 10;
		Integer page = 0;
		SortBy order = new SortBy("totalQty", SortOrder.DESC);
		
		if (request != null) {
			if (request.getPage() != null) {
				page = request.getPage();
			}
			if (request.getSize() != null && request.getSize() > 0) {
				size = request.getSize();
			}
			if (request.getSort() != null) {
				order = request.getSort();
			}
		}
		
		Sort sort = Sort.by(order.getField());
		if (SortOrder.DESC.equals(order.getOrder())) {
			sort = sort.descending();
		}

		if (!StringUtils.isBlank(request.getFilter())) {
			page = 0;
			size = Integer.MAX_VALUE;
		}
				
		Pageable paging = PageRequest.of(page, size, sort);
		Account account = authService.getLoginUserAccount(true, httpRequest);
		
		log.info("beforw calling service ");
		ProductListResponse responseResult = productIntelligenceService.getProductIntelligenceList(account.getId(), paging, request);
		
		return ResponseBuilder.buildResponse(responseResult);
	}
	
	@GetMapping(value = "/productsByPersona/{userGroup}", headers = {
	"version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get All Products by user persona", notes = "Get All Products by user persona", authorizations = {
	@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getProductDetailsByUserPersona(@PathVariable("userGroup") String userGroupId, HttpServletRequest httpRequest){
		
		Account account = authService.getLoginUserAccount(true, httpRequest);
		List<String> userList = userPersonaRepository.findUserIdsForPersona(account.getId(), userGroupId);
		if(userList.isEmpty()) {
			log.error("User List is empty ");
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user.not.found", new Object[] {},
					"api.error.user.not.found.code"));
		}
		List<WeeklyPopularProduct> products = userEventRepository.getProductIdByAccountIdAndUserIdsForBuyEvent(account.getId(), userList);
		
		return ResponseBuilder.buildResponse(products);
	}
}
