package com.humaine.portal.api.response.dto;

import java.util.HashMap;

import com.humaine.portal.api.model.Heatmap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HeatMapResponse {

	HashMap<String, HashMap<String, Heatmap>> pages;

	String createdOn;
}
