package com.humaine.collection.api.rest.service.impl;

import java.time.OffsetDateTime;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.humaine.collection.api.enums.UserEvents;
import com.humaine.collection.api.exception.APIException;
import com.humaine.collection.api.model.InventoryMaster;
import com.humaine.collection.api.model.Product;
import com.humaine.collection.api.model.Sale;
import com.humaine.collection.api.model.UserEvent;
import com.humaine.collection.api.request.dto.UserEventRequest;
import com.humaine.collection.api.rest.repository.InventoryRepository;
import com.humaine.collection.api.rest.repository.ProductRepository;
import com.humaine.collection.api.rest.repository.SaleRepository;
import com.humaine.collection.api.rest.service.SaleService;
import com.humaine.collection.api.util.ErrorMessageUtils;

@Service
public class SaleServiceImpl implements SaleService {
	private static final Logger log = LogManager.getLogger(SaleServiceImpl.class);
	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private SaleRepository saleRepository;

	@Autowired
	private ProductRepository productRepository;

	@Value("${product.default.title}")
	private String defaultProductTitle;

	
	@Override
	public Sale addSale(UserEventRequest userEventRequest, OffsetDateTime timestemp, UserEvent userEvent)
			throws APIException {
		log.debug("Inside addSale");
		this.validateUserEventRequest(userEventRequest);
		Sale sale = new Sale(userEventRequest, timestemp, userEvent);
		this.saleRepository.save(sale);
		log.info("Sale Info Saved: {}", sale.toString());
		return sale;
	}

	private void validateUserEventRequest(UserEventRequest userEventRequest) {
		log.debug("Inside validate User Event Request");
		if (StringUtils.isBlank(userEventRequest.getProductID())) {
			log.error("Empty Product ID");
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.usereventrequest.productId.null",
					null, "api.error.usereventrequest.productId.null.code"));
		}

		if (userEventRequest.getProductQuantity() == null) {
			log.error("Empty ProductQuantity");
			throw new APIException(
					errorMessageUtils.getMessageWithCode("api.error.usereventrequest.productQuantity.invalid", null,
							"api.error.usereventrequest.productQuantity.invalid.code"));
		}
		if (userEventRequest.getSaleAmount() == null) {
			log.error("Empty SaleAmount");
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.usereventrequest.saleAmount.invalid",
					null, "api.error.usereventrequest.saleAmount.invalid.code"));
		}

		Product inventory = productRepository.getProductsByAccountAndId(userEventRequest.getAccountID(),
				userEventRequest.getProductID());

		if (inventory == null) {
			log.error("Product not found with Product ID: {}", userEventRequest.getProductID());
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.product.not.exist",
					new Object[] { userEventRequest.getProductID() }, "api.error.product.not.exist.code"));
		}
		
		String productId = inventory.getProductId();
		if (this.defaultProductTitle.equalsIgnoreCase(inventory.getProductName())
				&& inventory.getParentProduct() != null) {
			productId = inventory.getParentProduct().getProductId();
		}
		userEventRequest.setProductID(productId);
		if (!userEventRequest.getEventID().equals(UserEvents.BUY)) {
			userEventRequest.setCouponCode(null);
		}
	}

}
