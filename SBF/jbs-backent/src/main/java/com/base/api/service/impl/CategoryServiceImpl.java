package com.base.api.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.base.api.entities.Category;
import com.base.api.exception.APIException;
import com.base.api.repository.CategoryRepository;
import com.base.api.request.dto.CategoryListDTO;
import com.base.api.request.dto.CategoryManagementDTO;
import com.base.api.request.dto.CategoryTreeViewDTO;
import com.base.api.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Service(value = "categoryService")
@Slf4j
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository categoryRepository;

	@Override
	public String addCategory(@Valid CategoryManagementDTO categoryDTO) {
		log.info("Category DTO : " + categoryDTO.toString());

		try {
			Category parent = new Category();
			parent.setId(UUID.randomUUID());
			parent.setCreatedDate(LocalDateTime.now());
			parent.setStatus(categoryDTO.getStatus());
			categoryDTO.getTranslable().stream().forEach(translation -> {
				if (translation.getLocale().equals("en")) {
					parent.setName(translation.getName());
				}
			});
           Optional<Category> p = java.util.Optional.empty();
            if (categoryDTO.getParent_id() != null && !categoryDTO.getParent_id().toString().isEmpty()
                    && !categoryDTO.getParent_id().toString().isBlank() && !categoryDTO.getParent_id().equals("0")) {
                p = checkIfParentPresent(categoryDTO.getParent_id());
                parent.setCategoryTable(p.get());
            } else {
            	parent.setCategoryTable(null);
            }

//		for (int i = 0; i < categoryDTO.getTranslable().size(); i++) {
//			parent.addChild(categoryDTO.getTranslable().get(i));
//		}

            parent.setTranslations(categoryDTO.getTranslable());

            log.info("parent : " + parent.toString());
            categoryRepository.save(parent);
            return HttpStatus.OK.name();
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            return HttpStatus.INTERNAL_SERVER_ERROR.name();
        }
    }
	
    private Optional<Category> checkIfParentPresent(UUID id) {
        Optional<Category> p = categoryRepository.findById(id);
        if (p.isPresent()) {
            return p;
        } else {
           return null;
        }
//		return null;
    }

	@Override
	public List<CategoryListDTO> getAllCategories() {
		List<Category> p = new ArrayList<Category>();
		p = categoryRepository.findAll();
		List<CategoryListDTO> categoryListDTOs = new ArrayList<CategoryListDTO>();
		p.forEach(parent -> {
			CategoryListDTO dto = new CategoryListDTO();
//			if (parent.getCategoryTable() != null) {
				dto.setCreatedDate(parent.getCreatedDate());
				parent.getTranslations().stream().forEach(translation -> {
					if (translation.getLocale().equals("en")) {
						dto.setDescription(translation.getDescription());
					}
				});
				dto.setId(parent.getId());
				dto.setName(parent.getName());
				if (parent.getCategoryTable() != null) {
					dto.setParent_id(parent.getCategoryTable().getId());
					dto.setParent_name(parent.getCategoryTable().getName());
				}
				else {
					dto.setParent_name("---");
				}
				dto.setSlug("");
				dto.setStatus(parent.getStatus());

				List<Category> categoryList = categoryRepository.getChildofParent(parent.getId());
				dto.setChildren(categoryList);
				categoryListDTOs.add(dto);
//			}
		});
		return categoryListDTOs;
	}

	@Override
	public List<Category> getChildren(UUID id) {
		Optional<Category> p = Optional.empty();
		p = categoryRepository.findById(id);
		if (!p.isPresent()) {
			throw new APIException("category.not.found");
		}
		List<Category> parent = new ArrayList<Category>();
		parent = p.get().getChildren();
		return parent;
	}

	@Override
	public List<CategoryListDTO> getAllActiveCategories(int parentId) {
		List<Category> p = new ArrayList<Category>();
		p = categoryRepository.findAllActiveCategories("Active");
		List<CategoryListDTO> categoryListDTOs = new ArrayList<CategoryListDTO>();
		if (!p.isEmpty()) {
			p.forEach(parent -> {
				CategoryListDTO dto = new CategoryListDTO();
				if (parent.getCategoryTable() != null) {
					dto.setCreatedDate(parent.getCreatedDate());
					parent.getTranslations().stream().forEach(translation -> {
						if (translation.getLocale().equals("en")) {
							dto.setDescription(translation.getDescription());
						}
					});
					dto.setId(parent.getId());
					dto.setName(parent.getName());
					if (parent.getCategoryTable() != null) {
						dto.setParent_id(parent.getCategoryTable().getId());
						dto.setParent_name(parent.getCategoryTable().getName());
					}
					dto.setStatus(parent.getStatus());
					categoryListDTOs.add(dto);
				}
			});
		}
		return categoryListDTOs;
	}

	@Override
	public Boolean changeStatus(UUID id, String status) {
		Optional<Category> parent = categoryRepository.findById(id);
		if (!parent.isPresent()) {
			throw new APIException("category.not.found", HttpStatus.NOT_FOUND);
		} else {
			parent.get().setStatus(status);
			categoryRepository.save(parent.get());
			return true;
		}
	}

	@Override
	public Boolean deleteCategory(UUID categoryId) {
		Optional<Category> category = categoryRepository.findById(categoryId);
		if (!category.isPresent()) {
			throw new APIException("category.not.found", HttpStatus.NOT_FOUND);
		} else {
			categoryRepository.delete(category.get());
			return true;
		}
	}

	@Override
	public CategoryListDTO getCategory(UUID categoryId) {
		Optional<Category> parent = categoryRepository.findById(categoryId);
		CategoryListDTO categoryListDTO = new CategoryListDTO();
		if (parent.isPresent()) {
			categoryListDTO.setCreatedDate(parent.get().getCreatedDate());
			categoryListDTO.setId(parent.get().getId());
			categoryListDTO.setStatus(parent.get().getStatus());
			categoryListDTO.setName(parent.get().getName());
			if (parent.get().getCategoryTable() != null) {
				categoryListDTO.setParent_id(parent.get().getCategoryTable().getId());
				categoryListDTO.setParent_name(parent.get().getCategoryTable().getName());
			}
			parent.get().getTranslations().stream().forEach(translation -> {
				if (translation.getLocale().equals("en")) {
					categoryListDTO.setDescription(translation.getDescription());
				}
			});
			categoryListDTO.setTranslatable(parent.get().getTranslations());
		}
		return categoryListDTO;
	}

	@Override
	public Category updateCategory(@Valid CategoryManagementDTO categoryDTO, UUID id) {
		Optional<Category> parent = categoryRepository.findById(id);
		if (parent.isPresent()) {
			parent.get().setStatus(categoryDTO.getStatus());
			categoryDTO.getTranslable().stream().forEach(translation -> {
				if (translation.getLocale().equals("en")) {
					parent.get().setName(translation.getName());
				}
			});
			if(categoryDTO.getParent_id()!=null) {
			Optional<Category> p = checkIfParentPresent(categoryDTO.getParent_id());
			parent.get().setCategoryTable(p.get());
			} else {
				parent.get().setCategoryTable(null);	
			}
			parent.get().setTranslations(categoryDTO.getTranslable());
			return categoryRepository.save(parent.get());
		}
		return null;
	}

	@Override
	public List<CategoryTreeViewDTO> getCategoryTree() {
		List<Category> category = new ArrayList<Category>();
		category = categoryRepository.findAll();
		
		List<Category> list = category.stream().filter(c -> c.getId() != null).collect(Collectors.toList());
		
		log.info("Category List is : " + list.toString());

		List<CategoryTreeViewDTO> categoryListDTOs = new ArrayList<CategoryTreeViewDTO>();
		for (int i = 0; i < list.size(); i++) {
			CategoryTreeViewDTO categoryTreeViewDTO = new CategoryTreeViewDTO();
			categoryTreeViewDTO.setId(category.get(i).getId());

			List<Category> categoryList = new ArrayList<Category>();
			List<Category> categoryList1 = categoryRepository.getChildofParent(category.get(i).getId());
			categoryList.addAll(categoryList1);
			if(category.get(i).getCategoryTable()  == null || category.get(i).getChildren() == null)
				categoryTreeViewDTO.setName(category.get(i).getName());

			for (Category cl : categoryList1) {
				if (cl.getId() != null) {
					categoryList.addAll(categoryRepository.getChildofParent(cl.getId()));
				}
			}
			if(category.get(i).getCategoryTable()  == null) {
			categoryTreeViewDTO.setName(category.get(i).getName());
			categoryTreeViewDTO.setChildren(categoryList);
			categoryListDTOs.add(categoryTreeViewDTO);
			}
		}
		return categoryListDTOs;

	}

	@Override
	public String bulkUpdate(List<Map<String, UUID>> categories) {

		try {
			for (Map<String, UUID> category : categories) {
				UUID id = category.get("id");
				UUID parentId = category.get("parent_id");
				Optional<Category> parent = categoryRepository.findById(id);
				if (parent.isPresent()) {
					if(parentId!=null) {
						Optional<Category> p = checkIfParentPresent(parentId);
						parent.get().setCategoryTable(p.get());
					}
					categoryRepository.save(parent.get());
				} else {
					return HttpStatus.NOT_FOUND.name();
				}
			}
			return HttpStatus.OK.name();
		} catch (DataIntegrityViolationException e) {
			return HttpStatus.INTERNAL_SERVER_ERROR.name();
		}
	}
}
