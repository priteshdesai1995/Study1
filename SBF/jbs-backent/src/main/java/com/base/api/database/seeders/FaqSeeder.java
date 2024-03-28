/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.database.seeders;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.api.annotations.DatabaseSeeder;
import com.base.api.entities.FAQTopicEntity;
import com.base.api.repository.FAQTopicRepository;


/**
 *  This class save the FAQ Topics to the database.
 * @author pritesh_desai
 *
 */
@DatabaseSeeder
@Component("FaqSeeder")
public class FaqSeeder implements BaseSeeder {

	@Autowired
	FAQTopicRepository faqTopicRepository;

	/**
	 * Seed.
	 *
	 * @throws Exception the exception
	 */
	@Override
	public void seed() throws Exception {
		FAQTopicEntity faqTopicEntity = new FAQTopicEntity();
		faqTopicEntity.setId(UUID.randomUUID());
		faqTopicEntity.setTopicName("test1");
		faqTopicRepository.save(faqTopicEntity);
	}

}