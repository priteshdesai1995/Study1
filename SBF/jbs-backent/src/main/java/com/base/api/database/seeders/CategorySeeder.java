/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.database.seeders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.api.annotations.DatabaseSeeder;
import com.base.api.entities.Category;
import com.base.api.repository.CategoryRepository;

/**
 * This class save the Category to the database.
 * @author pritesh_desai
 *
 */
@DatabaseSeeder
@Component("CategorySeeder")
public class CategorySeeder implements BaseSeeder {

	@Autowired
	CategoryRepository parentRepository;

	/**
	 * Seed.
	 *
	 * @throws Exception the exception
	 */
	@Override
	public void seed() throws Exception {
//		Category parent = new Category(UUID.randomUUID(), "test", "Active", LocalDateTime.now());
//		parentRepository.save(parent);
//		
//		Category childCategory = new Category(UUID.randomUUID(), "test child", "Active", LocalDateTime.now(), Arrays.asList(parent));
//        parentRepository.save(childCategory);
	}

}