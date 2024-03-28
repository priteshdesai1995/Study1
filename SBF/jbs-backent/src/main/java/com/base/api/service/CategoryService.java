package com.base.api.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import com.base.api.entities.Category;
import com.base.api.request.dto.CategoryListDTO;
import com.base.api.request.dto.CategoryManagementDTO;
import com.base.api.request.dto.CategoryTreeViewDTO;

public interface CategoryService {

	String addCategory(@Valid CategoryManagementDTO categoryDTO);

	List<CategoryListDTO> getAllCategories();

	List<Category> getChildren(UUID id);

	List<CategoryListDTO> getAllActiveCategories(int parent_id);

	Boolean deleteCategory(UUID categoryId);

	Boolean changeStatus(UUID id, String status);

	CategoryListDTO getCategory(UUID categoryId);

	Category updateCategory(@Valid CategoryManagementDTO categoryDTO, UUID categoryId);

	List<CategoryTreeViewDTO> getCategoryTree();

	String bulkUpdate(List<Map<String, UUID>> categories);

}
