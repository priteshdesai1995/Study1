package com.base.api.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.api.dto.filter.BannerFilter;
import com.base.api.dto.filter.FilterBase;
import com.base.api.entities.Banner;
import com.base.api.entities.User;
import com.base.api.exception.APIException;
import com.base.api.exception.UserNotFoundException;
import com.base.api.gateway.util.Util;
import com.base.api.repository.BannerRepository;
import com.base.api.repository.UserRepository;
import com.base.api.service.BannerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "bannerService")
public class BannerServiceImpl implements BannerService {
	
    String userDirectory = new File("").getAbsolutePath();
    private final Path root = Paths.get(userDirectory + "/upload/");

	@Autowired	
	private BannerRepository bannerRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EntityManager entityManager;

	@Override
	public List<Banner> getBannerList(BannerFilter filter) {

		// first we will return all the banners if title, created by and status is not
		// avaialble		
			
//			log.info("no filter applyed");
//			List<Banner> bannerList = bannerRepository.findAll();
//			return bannerList;
		
		StringBuilder query = new StringBuilder();
		query = createCommonQuery();

		if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
			query.append(" LOWER(u.status) = '" + filter.getStatus().toLowerCase() + "' ");
		}
		if (filter.getBanner_title() != null && !filter.getBanner_title().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");
			query.append(" LOWER(u.titleEN) LIKE '%" + filter.getBanner_title().toLowerCase() + "%'");
		}
		if (filter.getCreated_by() != null && !filter.getCreated_by().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");
			query.append(" (LOWER(upe.firstName) LIKE '%" + filter.getCreated_by().toLowerCase()
					+ "%' or  LOWER(upe.lastName) LIKE '%" + filter.getCreated_by().toLowerCase() + "%')");
		}

		if (query.indexOf("where") == (query.length() - 5))
			query = new StringBuilder(query.substring(0, query.indexOf("where")));

		String columnName = filter.getSort_param();
		if (columnName.equals("banner_title")) {
			columnName = "title_en";
		} else if (columnName.equals("first_name")) {
			columnName = "upe.firstName";
		}

		FilterBase filterBase = new FilterBase();
		filterBase.setColumnName(columnName);
		filterBase.setStartRec(filter.getStartRec());
		filterBase.setEndRec(filter.getEndRec());
		filterBase.setSortingOrder(filter.getSort_type());
		filterBase.setOrder(filter.getOrder());
		String queryParam = Util.getFilterQueryForSuggestion(filterBase, query.toString());
		List<Banner> results = new ArrayList<Banner>();
		results = entityManager.createQuery(queryParam).setFirstResult(Integer.valueOf(filter.getStartRec()))
				.setMaxResults(Integer.valueOf(filter.getEndRec())).getResultList();
		return results;
	}

	private StringBuilder createCommonQuery() {
		StringBuilder query = new StringBuilder();
		query.append("select u from Banner u join u.user co join co.userProfile upe where");
		return query;
	}

	@Override
	public Banner getBanner(UUID bannerId) {
		log.info("getBanner : bannerServce"); 
		Banner banner = getSingleBanner(bannerId);
		return banner;
	}

	@Override
	public String deleteBanner(UUID bannerId) {

		Banner banner = getSingleBanner(bannerId);

		bannerRepository.delete(banner);

		return HttpStatus.OK.name();
	}

	@Override
	public String updateBanner(UUID bannerId, BannerFilter bannerFilter) {

//		Banner banner = getSingleBanner(bannerId);
//
//		banner.setStatus(bannerFilter.getStatus());
//		banner.setImage(bannerFilter.getBanner_image());
//		banner.setBannerTitle(bannerFilter.getBanner_title());
		
		
		Banner banner = bannerRepository.getById(bannerId);
		banner.setStatus(bannerFilter.getStatus());
		banner.setImage(bannerFilter.getBanner_image());
		System.out.println(bannerFilter.getBanner_title());
		Map<String, Object> title = new GsonJsonParser().parseMap(bannerFilter.getBanner_title());
		banner.setTitleEN((String) title.get("en"));
		banner.setTitleAR((String) title.get("ar"));
//		bannerRepository.save(banner);
		return HttpStatus.OK.name();
	}

	@Override
	public String createBanner(BannerFilter bannerFilter){

//		String userId = bannerFilter.getUserId();
//		User user = userRepository.getById(UUID.fromString(userId));
//
//		if (user == null) {
//			log.error("No user Id found with id = >" + userId);
//			throw new UserNotFoundException("api.error.user.not.found");
//		}
//		 
//		log.info("bannerFilter.getBanner_title() => "+ bannerFilter.getBanner_title()); 
//		
////		 JSONObject json = new JSONObject(bannerFilter.getBanner_title());
//
//
//	//	Banner banner = new Banner(json.getString("en"), "en", "ar", bannerFilter.getStatus(), bannerFilter.getBanner_image(), user);
//
//		log.info("New banner => " + banner.toString());
		
//		 try {
//	            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
//	        } catch (IOException e) {
//	            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
//	        }
		Banner banner = new Banner();
		banner.setStatus(bannerFilter.getStatus());
		banner.setImage(bannerFilter.getBanner_image());
		banner.setUser(userRepository.getById(UUID.fromString(bannerFilter.getUserId())));
		System.out.println(bannerFilter.banner_title);
		Map<String, Object> title = new GsonJsonParser().parseMap(bannerFilter.getBanner_title());
		banner.setTitleEN((String) title.get("en"));
		banner.setTitleAR((String) title.get("ar"));
		System.out.println(banner);
		bannerRepository.save(banner);
		return HttpStatus.OK.name();
	}

	/**
	 * key : value bannerId : String_value status : String_value
	 */
	@Override
	public String changeStatus(Map<String, String> statusReq) {

		UUID bannerId = UUID.fromString(statusReq.get("bannerId"));
		Banner banner = getSingleBanner(bannerId);
		banner.setStatus(statusReq.get("status"));
		bannerRepository.save(banner);
		return HttpStatus.OK.name();
	}

	private Banner getSingleBanner(UUID bannerId) {
		Banner banner = bannerRepository.getById(bannerId);

		if (banner == null) {
			log.error("Banner not found, Invalid banner id  => " + bannerId);
			throw new APIException("banner.not.found");
		} else {
			return banner;
		}
	}

}
