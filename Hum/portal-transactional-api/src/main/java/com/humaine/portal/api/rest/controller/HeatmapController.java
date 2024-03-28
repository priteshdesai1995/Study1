package com.humaine.portal.api.rest.controller;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.Heatmap;
import com.humaine.portal.api.request.dto.HeatmapSignUrlRequest;
import com.humaine.portal.api.response.dto.HeatMapResponse;
import com.humaine.portal.api.rest.repository.HeatmapRepository;
import com.humaine.portal.api.rest.repository.impl.AmazonS3Repository;
import com.humaine.portal.api.rest.service.AuthService;
import com.humaine.portal.api.security.config.AWSConfig;
import com.humaine.portal.api.util.DateUtils;
import com.humaine.portal.api.util.ResponseBuilder;
import com.humaine.portal.api.util.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/heatmap")
@Api(tags = "Heatmap", description = "Heatmap", authorizations = {})
public class HeatmapController {

	private static final Logger log = LogManager.getLogger(HeatmapController.class);

	@Autowired
	private HeatmapRepository heatmapRepository;

	@Autowired
	private AmazonS3Repository amazonS3Repository;

	@Autowired
	private AuthService authService;

	@GetMapping(value = "", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Heatmap Images", notes = "Get Heatmap Images", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getDemographicAttributes(HttpServletRequest httpRequest) {
		log.info("Get Heat Map Image Data: start");
		Account account = authService.getLoginUserAccount(true, httpRequest);
		List<Heatmap> heatmaps = heatmapRepository.getHeatMapImages(account.getId());
		HashMap<String, HashMap<String, Heatmap>> result = new HashMap<>();
		heatmaps.forEach(e -> {
			if (!result.containsKey(e.getCategory())) {
				result.put(e.getCategory(), new HashMap<String, Heatmap>());
			}
			result.get(e.getCategory()).put(e.getDevice(), e);
		});
		String created = null;
		if (!heatmaps.isEmpty()) {
			created = DateUtils.getFromatedFullDateTime(heatmaps.get(0).getCreatedOn());
		}
		log.info("Get Heat Map Image Data: end");
		return ResponseBuilder.buildResponse(new HeatMapResponse(result, created));
	}

	@PostMapping(value = "/signed-url", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Heatmap Image Signed URL", notes = "Get Heatmap Image Signed URL", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getSignedUrl(@Valid @RequestBody HeatmapSignUrlRequest request, HttpServletRequest httpRequest) {
		log.info("Get Heatmap signed Url: start");
		Account account = authService.getLoginUserAccount(true, httpRequest);
		List<Heatmap> heatmaps = heatmapRepository.getHeatMapImagesByCategory(account.getId(), request.getCategory());
		HashMap<String, String> result = new HashMap<>();
		heatmaps.forEach(e -> {
			String res = amazonS3Repository.getPreSignedUrl(e.getAwsPath());
			result.put(e.getDevice(), res);
		});
		log.info("Get Heatmap signed Url: end");
		return ResponseBuilder.buildResponse(result);
	}

}
