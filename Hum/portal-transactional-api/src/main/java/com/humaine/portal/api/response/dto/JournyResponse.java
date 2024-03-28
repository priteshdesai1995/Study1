package com.humaine.portal.api.response.dto;

import com.humaine.portal.api.model.TestJourney;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JournyResponse {

	TestJourney journey;

	String groupName;

	String bigFive;

	Float successMatch;

	String journeyTime;

	public JournyResponse(TestJourney journey, String groupName, String bigFive) {
		super();
		this.journey = journey;
		this.groupName = groupName;
		this.bigFive = bigFive;
	}

}
