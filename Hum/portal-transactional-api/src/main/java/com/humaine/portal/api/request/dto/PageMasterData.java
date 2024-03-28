package com.humaine.portal.api.request.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.humaine.portal.api.model.Page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageMasterData {

	@NotNull(message = "{api.error.user.registration.page.pageUrl.null}{error.code.separator}{api.error.user.registration.page.pageUrl.null.code}")
	private String pageURL;

	@NotBlank(message = "{api.error.user.registration.page.pageName.null}{error.code.separator}{api.error.user.registration.page.pageName.null.code}")
	private String pageName;

	public PageMasterData(Page page) {
		this.pageURL = page.getPageUrl();
		this.pageName = page.getPageName();
	}
}
