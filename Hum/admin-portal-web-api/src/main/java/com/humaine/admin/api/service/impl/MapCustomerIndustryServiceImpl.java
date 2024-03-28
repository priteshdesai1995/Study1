package com.humaine.admin.api.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humaine.admin.api.dto.CustomerDTO;
import com.humaine.admin.api.model.CustomerIndustry;
import com.humaine.admin.api.repository.CustomerRepository;
import com.humaine.admin.api.service.MapCustomerIndustryService;

@Service
public class MapCustomerIndustryServiceImpl implements MapCustomerIndustryService {

	@Autowired
	CustomerRepository customerRepository;

	@Override
	public void getIndustries(List<CustomerDTO> response) {
		List<Long> accountIds = response.stream().map(p -> p.getAccountId()).collect(Collectors.toList());
		List<CustomerIndustry> industries = customerRepository.mapCustomerIndustry(accountIds);
		Map<Long, CustomerIndustry> map = new HashMap<>();
		industries.stream().forEach(e -> {
			map.put(e.getAccountId(), e);
		});
		response.stream().forEach(e -> {
			if (map.containsKey(e.getAccountId())) {
				e.setIndustry(map.get(e.getAccountId()).getIndustries());
			}
		});
	}

}
