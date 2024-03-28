package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.PermissionConstants.ADD_CATEGORY;
import static com.base.api.constants.PermissionConstants.CATEGORY_TREE_VIEW;
import static com.base.api.constants.PermissionConstants.CHANGE_STATUS;
import static com.base.api.constants.PermissionConstants.DELETE_CATEGORY;
import static com.base.api.constants.PermissionConstants.GET_ALL_CATEGORY;
import static com.base.api.constants.PermissionConstants.GET_CATEGORY;
import static com.base.api.constants.PermissionConstants.PERMISSION;
import static com.base.api.constants.PermissionConstants.UPDATE_CATEGORY;
import static com.base.api.constants.PermissionConstants.UPDATE_CATEGORY_TREE_VIEW;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
import com.base.api.entities.Category;
import com.base.api.request.dto.CategoryListDTO;
import com.base.api.request.dto.CategoryManagementDTO;
import com.base.api.request.dto.CategoryTreeViewDTO;
import com.base.api.service.CategoryService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author pinky_ramnani This controller is used for CRUD operations on
 *         category.
 */
@RequestMapping("/category")
@Api(tags = "Category Management API", description = "Rest APIs for the category management")
@Slf4j
@RestController
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	@Autowired
	ResourceBundle resourceBundle;

	/**
	 * @param categoryDTO
	 * @return
	 * 
	 *         This method will execute when user performs save operation on
	 *         category.
	 * 
	 * @throws Exception
	 */
	@PostMapping(value = "/create-category")
	@ApiOperation(value = "API Endpoint to add the Category", notes = PERMISSION + ADD_CATEGORY, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { ADD_CATEGORY })
	public ResponseEntity<TransactionInfo> createCategory(@RequestBody CategoryManagementDTO categoryDTO){
		log.info("Category create call starts");
		String result = categoryService.addCategory(categoryDTO);

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {
			return ResponseBuilder.buildCRUDResponse(resourceBundle.getString("category_created"), resourceBundle.getString("category_created"),
					HttpStatus.CREATED);
		}

		return ResponseBuilder.buildInternalServerErrorResponse(result,
				resourceBundle.getString("category_create_fail"));
	}

	@GetMapping(value = "/parent-list")
	@ApiOperation(value = "API Endpoint to add the Category", notes = PERMISSION + GET_CATEGORY, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_ALL_CATEGORY })
	public ResponseEntity<TransactionInfo> getAllCategories() {
		List<CategoryListDTO> parent = categoryService.getAllCategories();
		if (parent == null || parent.size() == 0) {
			return ResponseBuilder.buildRecordNotFoundResponse(parent, resourceBundle.getString("record_not_found"));
		}
		return ResponseBuilder.buildOkResponse(parent);
	}

	@GetMapping(value = "/parent-list/{id}", headers = { "Version=V1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@Secured({ "ROLE_SUPERADMIN", "ROLE_ADMIN" })
	public ResponseEntity<TransactionInfo> parentList(@PathVariable("id") String id) {
		List<Category> categories = categoryService.getChildren(UUID.fromString(id));
		if (categories == null || categories.size() == 0) {
			return ResponseBuilder.buildRecordNotFoundResponse(categories,
					resourceBundle.getString("record_not_found"));
		}
		return ResponseBuilder.buildOkResponse(categories);
	}

	@GetMapping(value = "/getchild")
	@ApiOperation(value = "API Endpoint to get the Categories", notes = PERMISSION
			+ GET_ALL_CATEGORY, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_ALL_CATEGORY })
	public ResponseEntity<TransactionInfo> getAllChildCategories(@RequestParam("parent_id") int parent_id) {
		List<CategoryListDTO> categories = categoryService.getAllActiveCategories(parent_id);
		if (categories != null) {
			return ResponseBuilder.buildOkResponse(categories);
		}
		return ResponseBuilder.buildRecordNotFoundResponse(categories, resourceBundle.getString("record_not_found"));
		// return ResponseBuilder.buildRecordNotFoundResponse(categories,
		// "category.list.not.found");
	}

	@PutMapping(value = "/changeStatus")
	@ApiOperation(value = "API Endpoint to change the status", notes = PERMISSION + CHANGE_STATUS, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CHANGE_STATUS })
	public ResponseEntity<TransactionInfo> changeStatus(@RequestBody Map<String, String> statusReq) {

		String status = String.valueOf(statusReq.get("status"));
		Boolean result = categoryService.changeStatus(UUID.fromString(statusReq.get("id")), status);
		if (result)
			return ResponseBuilder.buildStatusChangeResponse(resourceBundle.getString("status"), resourceBundle.getString("status"));
		return ResponseBuilder.buildRecordNotFoundResponse(resourceBundle.getString("record_not_found"), resourceBundle.getString("record_not_found"));
	}

	@DeleteMapping(value = "/delete/{categoryId}")
	@ApiOperation(value = "API Endpoint to delete Category", notes = PERMISSION + DELETE_CATEGORY, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { DELETE_CATEGORY })
	public ResponseEntity<TransactionInfo> deleteCategory(@PathVariable("categoryId") String categoryId) {
		Boolean result = categoryService.deleteCategory(UUID.fromString(categoryId));
		if (result) {
			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("category_deleted"),
					HttpStatus.OK);
		}
		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}

	@GetMapping(value = "/getCategory/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to get Category", notes = PERMISSION + GET_CATEGORY, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_CATEGORY })
	public ResponseEntity<TransactionInfo> getCategory(@PathVariable("categoryId") String categoryId) {
		CategoryListDTO result = categoryService.getCategory(UUID.fromString(categoryId));
		if (result == null) {
			return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
		}
		return ResponseBuilder.buildOkResponse(result);
	}

	@PutMapping(value = "/update/{categoryId}")
	@ApiOperation(value = "API Endpoint to update the category", notes = PERMISSION
			+ UPDATE_CATEGORY, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { UPDATE_CATEGORY })
	public ResponseEntity<TransactionInfo> updateCategory(@Valid @RequestBody CategoryManagementDTO categoryDTO,
			@PathVariable("categoryId") String categoryId) {
		log.info("Category update call starts");
		Category updateCategory = categoryService.updateCategory(categoryDTO, UUID.fromString(categoryId));
		if (updateCategory != null) {
		    
			return ResponseBuilder.buildCRUDResponse(resourceBundle.getString("category_updated"), resourceBundle.getString("category_updated"),
					HttpStatus.OK);
		}
		log.info("Category update call ends");
		return ResponseBuilder.buildInternalServerErrorResponse(updateCategory,
				resourceBundle.getString("category_update_fail"));
	}

	@GetMapping(value = "/categoryTreeView")
	@ApiOperation(value = "API Endpoint for category tree view", notes = PERMISSION
			+ CATEGORY_TREE_VIEW, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CATEGORY_TREE_VIEW })
	public ResponseEntity<TransactionInfo> categoryTreeView() {
		List<CategoryTreeViewDTO> result = categoryService.getCategoryTree();
		if (result == null || result.size() == 0) {
			return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
		}
		return ResponseBuilder.buildOkResponse(result);
	}

	@PutMapping(value = "/treeview/update", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to update category tree view", notes = PERMISSION
			+ UPDATE_CATEGORY_TREE_VIEW, authorizations = {
					@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { UPDATE_CATEGORY_TREE_VIEW })
	public ResponseEntity<TransactionInfo> updateCategoryTreeView(@RequestBody List<Map<String, UUID>> categories) {
		String result = categoryService.bulkUpdate(categories);

		if (result.equals(HttpStatus.OK.name())) {

			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("category_treeview_updated"),
					HttpStatus.OK);
		} else if (result.equals(HttpStatus.NOT_FOUND.name())) {

			return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
		}

		return ResponseBuilder.buildInternalServerErrorResponse(result,
				resourceBundle.getString("category_treeview_update_fail"));
	}

}
