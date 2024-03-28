package com.humaine.portal.api.rest.olap.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.UserGroup;
import com.humaine.portal.api.olap.model.OLAPManualUserGroup;
import com.humaine.portal.api.rest.olap.repository.OLAPManualUserGroupRepository;
import com.humaine.portal.api.rest.olap.service.OLAPManualUserGroupService;
import com.humaine.portal.api.util.DateUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OLAPManualUserGroupServiceImplTest {

	@Autowired
	private OLAPManualUserGroupService manualUserGroupService;

	@MockBean
	private OLAPManualUserGroupRepository manualUserGroupRepository;

	private OffsetDateTime timestemp = DateUtils.getCurrentTimestemp();

	private List<OLAPManualUserGroup> manualUserGroups = new ArrayList<>() {
		{
			add(new OLAPManualUserGroup(1L, 1L, 1L, 10f, timestemp));
			add(new OLAPManualUserGroup(2L, 1L, 2L, 75f, timestemp));
			add(new OLAPManualUserGroup(3L, 2L, 3L, 75f, timestemp));
		}
	};

	@Before
	public void setup() {
		when(manualUserGroupRepository.getGroupListByAccountAndIds(Mockito.any(Long.class), Mockito.any(List.class)))
				.then(new Answer<List<OLAPManualUserGroup>>() {

					@Override
					public List<OLAPManualUserGroup> answer(InvocationOnMock invocation) throws Throwable {
						List<Long> ids = invocation.getArgument(1);
						return manualUserGroups.stream().filter(
								e -> e.getAccount().equals(invocation.getArgument(0)) && ids.contains(e.getGroupId()))
								.collect(Collectors.toList());
					}
				});
	}

	/*
	 * Test FillSuccessMatch when List of Groups null
	 */
	@Test
	public void fillSuccessMatch_with_Null_Groups() {
		manualUserGroupService.fillSuccessMatch(null, 1L);
	}

}
