package com.humaine.portal.api.schedular;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.humaine.portal.api.enums.GroupFlag;
import com.humaine.portal.api.model.DailyProductIntelligence;
import com.humaine.portal.api.projection.model.BigFiveWiseUserIds;
import com.humaine.portal.api.projection.model.ProductIntelligenceObject;
import com.humaine.portal.api.rest.olap.repository.OLAPUserGroupRepository;
import com.humaine.portal.api.rest.repository.AccountRepository;
import com.humaine.portal.api.rest.repository.DailyProductIntelligenceRepository;
import com.humaine.portal.api.rest.repository.SaleRepository;
import com.humaine.portal.api.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * This is the schedular, the code inside the schedular run on specific time
 * autometically
 * 
 * In this schedular we will get the data from few DB queries and save that data
 * in data base
 * 
 * this schedular is scheduled for every mid night and befor inserting any data
 * in DB we need to clear the
 * 
 * privious data.
 */
@Component
@EnableScheduling
@Slf4j
public class AllAccountProductSchedular {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private OLAPUserGroupRepository olapUserGroupRepository;

	@Autowired
	private SaleRepository saleRepository;

	@Value("${product.default.title}")
	private String defaultProductTitle;
	
	@Autowired
	private DailyProductIntelligenceRepository dailyProductIntelligenceRepository;

	/**
	 * This is the scheduler method to log the product of all accounts at mid night
	 */
	@Scheduled(cron = "${log.product.info.of.all.accounts.scheduler.cron.run.expression}")
	public void logProductsAtMidnight() {
		log.info("logProductsAtMidnight schedular starts");
		
		List<Long> accountIds = accountRepository.getIdsOfAllAccounts();
		log.info("total ids => " + accountIds.size());

		if (accountIds.isEmpty()) {
			log.info("no account available");
			return;
		}

		for (Long accountId : accountIds) {
			
			
			List<ProductIntelligenceObject> resultList = saleRepository.getProductIntelligence(accountId,
					defaultProductTitle, null);
			
			if(resultList.isEmpty()) {
				continue;
			}else {
				// delete the privious records whos resultList is available other wise not 
				dailyProductIntelligenceRepository.deleteDailyProductIntelligenceByAccountId(accountId);
				log.info("All privious record of account "+ accountId + " is deleted.");
			}
			
			log.info("account Id => "+ accountId);
			
			List<BigFiveWiseUserIds> bigFiveWiseUserList = olapUserGroupRepository.getBigFiveWiseUsers(accountId,
					GroupFlag.MY_USER_GROUP.value());
			
			Map<String, List<String>> userIdBigFiveMapping = new HashMap<String, List<String>>();

			bigFiveWiseUserList.forEach(e -> {
				if (!userIdBigFiveMapping.containsKey(e.getUserId())) {
					userIdBigFiveMapping.put(e.getUserId(), new ArrayList<>());
				}
				userIdBigFiveMapping.get(e.getUserId()).addAll(e.getBigFives());
			});
						
			Long totalUsers = 0L;
			if (resultList.size() > 0) {
				totalUsers = resultList.get(0).getTotalUserCount();
			}
			final Long countUsers = totalUsers;
			
			for (ProductIntelligenceObject result : resultList) {
				DailyProductIntelligence productIntelligence = new DailyProductIntelligence(result);
				productIntelligence.setAccountId(accountId);
				
				Map<String, Long> bigFiveCount = new HashMap<>();
				Map<String, Double> finalPercentage = new HashMap<>();
				
				List<String> userIds = result.getUserIds();
				
				for (String  userId : userIds) {
					if(userIdBigFiveMapping.containsKey(userId)) {
						List<String> bigFives = userIdBigFiveMapping.get(userId);
						
						for (String bigFive : bigFives) {
							if(!bigFiveCount.containsKey(getBigFiveKey(bigFive))) {
								bigFiveCount.put(getBigFiveKey(bigFive), 0L);
							}
							Long count = bigFiveCount.get(getBigFiveKey(bigFive));
							if (count == null)
								count = 0L;
							
							bigFiveCount.put(getBigFiveKey(bigFive), count + 1);
						}
					}
				}
				
				for (String bigFiveKey : bigFiveCount.keySet()) {
					if(countUsers > 0) {
						Double res = bigFiveCount.get(bigFiveKey) * 100 / countUsers.doubleValue();
						finalPercentage.put(bigFiveKey, CommonUtils.formatDouble(res));
					}else {
						finalPercentage.put(bigFiveKey, 0D);
					}
				} 
				productIntelligence.setCount(finalPercentage);
				
				dailyProductIntelligenceRepository.save(productIntelligence);
			}
			log.info("Total "+ resultList.size() + " products saved in DB");
		}
		
		log.info("Schedular job end.");
	}
	
	private String getBigFiveKey(String val) {
		if (val == null)
			val = "";
		return String.join("-", val.toLowerCase().split(" "));
	}
}
