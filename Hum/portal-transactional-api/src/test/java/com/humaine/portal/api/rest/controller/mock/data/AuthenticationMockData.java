package com.humaine.portal.api.rest.controller.mock.data;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import com.humaine.portal.api.enums.AccountStatus;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.BusinessInformation;
import com.humaine.portal.api.model.Page;
import com.humaine.portal.api.util.DateUtils;

public class AuthenticationMockData {

	public static OffsetDateTime timestemp = DateUtils.getCurrentTimestemp();

	public static BusinessInformation businessInfo = new BusinessInformation(1L, "Test Company", "www.test.com",
			"1122334455", "new street road", "New York", "1000$", "Magento", true, "Magento", "10", "500", "IT", "USA",
			"Need user visit solution", null, "New York", "New York", "USA");

	public static Account account = new Account(1L, "wwww.test.com", AccountStatus.confirmed, "test", "email@test.com",
			businessInfo, 1000, timestemp, timestemp, Arrays.asList(new String[] { "IT", "SEO" }),
			Arrays.asList(new String[] { "ABC ANALYTICS" }), new ArrayList<Page>());
}
