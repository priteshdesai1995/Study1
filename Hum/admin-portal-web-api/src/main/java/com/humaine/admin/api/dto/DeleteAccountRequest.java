package com.humaine.admin.api.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class DeleteAccountRequest {

	@NotEmpty(message = "{api.error.ids.null}{error.code.separator}{api.error.ids.null.code}")
	List<Long> ids;
}
