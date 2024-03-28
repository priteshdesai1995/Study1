package com.humaine.portal.api.request.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveAIUserGroupRequest {

	@NotEmpty(message = "{api.error.user-group.ids.null}{error.code.separator}{api.error.user-group.ids.null.code}")
	List<String> groupIds;
}
