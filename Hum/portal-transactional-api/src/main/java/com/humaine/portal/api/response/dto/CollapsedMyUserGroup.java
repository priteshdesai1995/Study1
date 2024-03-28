package com.humaine.portal.api.response.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.humaine.portal.api.olap.model.OLAPUserGroup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollapsedMyUserGroup {

	String bigFive;

	Long bigFiveId;
	
	List<String> values;
	
	List<String> persuasiveStratergies;
	
	Float successMatch;
	
	List<OLAPUserGroup> groups;
	
	Long totalUserCount;
}
