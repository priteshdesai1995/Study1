package com.humaine.portal.api.response.dto;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.humaine.portal.api.model.Buying;
import com.humaine.portal.api.model.Persuasive;
import com.humaine.portal.api.model.Values;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CognitiveAttributes {

	private Set<Values> values = new LinkedHashSet<Values>();

	private Set<Persuasive> persuasiveStrategies = new LinkedHashSet<Persuasive>();

	private Set<Buying> motivationToBuy = new LinkedHashSet<Buying>();
	
	Values valuesEmpty;
	
	Persuasive persuasiveStrategiesEmpty;
	
	Buying motivationToBuyEmpty;
}
