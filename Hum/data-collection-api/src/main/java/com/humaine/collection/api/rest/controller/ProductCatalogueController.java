package com.humaine.collection.api.rest.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humaine.collection.api.model.Account;
import com.humaine.collection.api.model.Product;
import com.humaine.collection.api.model.ProductAttribute;
import com.humaine.collection.api.model.ProductCategory;
import com.humaine.collection.api.model.ProductPrice;
import com.humaine.collection.api.request.dto.CategoryWiseProductRequest;
import com.humaine.collection.api.request.dto.ProductCatalogueReqeuest;
import com.humaine.collection.api.request.dto.ProductRequest;
import com.humaine.collection.api.rest.repository.ProductCategoryRepository;
import com.humaine.collection.api.rest.repository.ProductRepository;
import com.humaine.collection.api.security.config.Authentication;
import com.humaine.collection.api.security.filter.CustomAuthenticationFilter;
import com.humaine.collection.api.util.ErrorMessageUtils;
import com.humaine.collection.api.util.ResponseBuilder;
import com.humaine.collection.api.util.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@Api(tags = "Product Catalogue", description = "Save Product Data")
@RequestMapping("products")
public class ProductCatalogueController {

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	ProductCategoryRepository productCategoryRepository;

	@ApiOperation(value = "save product Data from from E-comm site", notes = "save product Data from from E-comm site", authorizations = {
			@Authorization(value = CustomAuthenticationFilter.HEADER) })
	@PostMapping(path = "", headers = {
			"version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TransactionInfo> savePageLoadEvent(@Valid @RequestBody ProductCatalogueReqeuest request) {

		Authentication authentication = (Authentication) SecurityContextHolder.getContext().getAuthentication();

		Account account = authentication.getAccount();
		List<ProductCategory> category = productCategoryRepository.findProductCategoryByAccount(account.getId());
		Map<String, ProductCategory> categoryMap = new HashMap<String, ProductCategory>();
		category.forEach(e -> {
			categoryMap.put(e.getCategoryId(), e);
		});

		List<Product> products = productRepository.getProductsByAccount(account.getId());
		Map<String, Product> productsMap = new HashMap<String, Product>();
		products.forEach(e -> {
			productsMap.put(e.getProductId(), e);
		});

		if (request != null) {
			request.getCategories().forEach(e -> {

				ProductCategory pc = addOrEditCategory(categoryMap, account, e);

				final ProductCategory pcat = pc;
				if (e.getProducts() != null) {
					e.getProducts().forEach(p -> {
						addEditProduct(productsMap, account, e, p, pcat);
					});
				}
			});
		}

		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils.getMessageWithCode("api.success.product.save",
				null, "api.success.product.save.code"));
	}

	private Product addEditProduct(Map<String, Product> productsMap, Account account, CategoryWiseProductRequest cat,
			ProductRequest p, ProductCategory pcat) {
		Product pro = null;
		Product parent = null;

		if (!productsMap.containsKey(p.getProductId())) {
			pro = new Product(p, account, pcat);
		} else {
			pro = productsMap.get(p.getProductId());
		}
		if (!StringUtils.isBlank(p.getParentProductId()) && !productsMap.containsKey(p.getParentProductId())) {
			parent = new Product(p.getParentProductId(), account);
			parent = productRepository.save(parent);
			productsMap.put(p.getParentProductId(), parent);
		}
		if (!StringUtils.isBlank(p.getParentProductId())) {
			parent = productsMap.get(p.getParentProductId());
		}
		pro.setParentProduct(parent);
		pro.setProductName(p.getProductName());
		pro.setProductImage(p.getProductImage());
		pro.setProductDescription(p.getProductDescription());
		pro.setProductSKU(p.getProductSKU());
		pro.setCategory(pcat);
		if (pro.getPrices() == null) {
			pro.setPrices(new HashSet<>());
		}
		if (!StringUtils.isBlank(p.getPrice())) {
			pro.getPrices().add(new ProductPrice(account, p, pro));
		}
		pro.setAttributes(new HashSet<ProductAttribute>());
		final Product proConst = pro;
		if (p.getAttributes() != null) {
			p.getAttributes().forEach(a -> {
				proConst.getAttributes().add(new ProductAttribute(a));
			});
		}
		pro = proConst;
		pro = productRepository.save(pro);
		productsMap.put(pro.getProductId(), pro);
		return pro;
	}

	private ProductCategory addOrEditCategory(Map<String, ProductCategory> categoryMap, Account account,
			CategoryWiseProductRequest e) {
		ProductCategory pc = null;
		ProductCategory parent = null;
		if (StringUtils.isBlank(e.getCategoryId())) {
			return pc;
		}
		if (!categoryMap.containsKey(e.getCategoryId())) {
			pc = new ProductCategory(e, account, null);
		} else {
			pc = categoryMap.get(e.getCategoryId());
		}
		if (!StringUtils.isBlank(e.getParentCategory()) && !categoryMap.containsKey(e.getParentCategory())) {
			parent = new ProductCategory();
			parent.setCategoryId(e.getParentCategory());
			parent.setAccount(account);
			parent = productCategoryRepository.save(parent);
			categoryMap.put(parent.getCategoryId(), parent);
		}
		if (!StringUtils.isBlank(e.getParentCategory())) {
			parent = categoryMap.get(e.getParentCategory());
		}
		pc.setParent(parent);
		pc.setCategoryName(e.getCategoryName());
		pc = productCategoryRepository.save(pc);
		categoryMap.put(pc.getCategoryId(), pc);
		pc = categoryMap.get(e.getCategoryId());
		return pc;
	}
}
