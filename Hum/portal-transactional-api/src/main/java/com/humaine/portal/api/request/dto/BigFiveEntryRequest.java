package com.humaine.portal.api.request.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BigFiveEntryRequest {

	String bigFive;

	String motivation;

	String strategy;

	String values;

	List<String> goals = new ArrayList<>();

	List<String> frustrations = new ArrayList<>();

	List<String> personality = new ArrayList<>();
}
