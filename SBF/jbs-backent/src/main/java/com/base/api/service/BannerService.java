package com.base.api.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.base.api.dto.filter.BannerFilter;
import com.base.api.entities.Banner;

public interface BannerService {

	List<Banner> getBannerList(BannerFilter bannerFilter);
	
	Banner getBanner(UUID bannerId);
	
	String deleteBanner(UUID bannerId);
	
	String updateBanner(UUID BannerId, BannerFilter bannerFilter);
	
	String createBanner(BannerFilter bannerFilter);
	
	String changeStatus(Map<String, String> statusReq);
	
	
}
