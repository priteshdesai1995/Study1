package com.humaine.transactional.jobs;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.humaine.transactional.model.UserEvent;
import com.humaine.transactional.repository.UserEventReaderRepository;
import com.humaine.transactional.util.DateUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBatchTest
public class UserEventJobTest {

	@MockBean
	private UserEventReaderRepository userEventReaderRepository;

	@MockBean
	private JdbcTemplate jdbcTemplate;

	@Value("${job.chunk.size}")
	public int chunkSize;

	@MockBean
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private UserEventJob job;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private JobRepositoryTestUtils jobRepositoryTestUtils;

	private JobExecution jobExecution;

	private OffsetDateTime timestemp = DateUtils.getCurrentTimestemp();

	Page<UserEvent> pageResp = new PageImpl<UserEvent>(new ArrayList<>() {
		{
			add(new UserEvent(1L, "101", 1L, "START", "SESSION", "PROD", "", timestemp));
			add(new UserEvent(2L, "101", 1L, "START", "SESSION", "PROD", "", timestemp));
			add(new UserEvent(3L, "101", 1L, "START", "SESSION", "PROD", "", timestemp));
			add(new UserEvent(4L, "101", 1L, "START", "SESSION", "PROD", "", timestemp));
			add(new UserEvent(5L, "101", 1L, "START", "SESSION", "PROD", "", timestemp));
		}
	});

	@Before
	public void setup() throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException, Exception {
		MockitoAnnotations.initMocks(this);

		Mockito.when(userEventReaderRepository.findArchiveRecords(Mockito.any(OffsetDateTime.class),
				Mockito.any(Pageable.class))).thenAnswer(new Answer<Page<UserEvent>>() {
					@Override
					public Page<UserEvent> answer(InvocationOnMock invocation) throws Throwable {
						Pageable p = invocation.getArgument(1);
						if (p.getPageNumber() < pageResp.getTotalPages()) {
							return pageResp;
						}
						return new PageImpl<UserEvent>(new ArrayList<UserEvent>());
					}

				});
		Mockito.when(userEventReaderRepository.getArchiveRecordsCount(Mockito.any(OffsetDateTime.class)))
				.thenAnswer(new Answer<Long>() {
					@Override
					public Long answer(InvocationOnMock invocation) throws Throwable {
						return 0L;
					}
				});

		Mockito.when(
				jdbcTemplate.batchUpdate(Mockito.any(String.class), Mockito.any(BatchPreparedStatementSetter.class)))
				.thenAnswer(new Answer<int[]>() {
					@Override
					public int[] answer(InvocationOnMock invocation) throws Throwable {
						return new int[] { 1, 1, 1, 1 };
					}
				});
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.any(String.class), Mockito.any(Map.class)))
				.thenAnswer(new Answer<Integer>() {

					@Override
					public Integer answer(InvocationOnMock invocation) throws Throwable {
						return pageResp.getSize();
					}

				});
	}

	@Test
	public void testSchedular() throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException, Exception {
		Map<String, JobParameter> confMap = new HashMap<String, JobParameter>();
		JobParameters jobParameters = new JobParameters(confMap);
		Long timestamp = System.currentTimeMillis();
		jobLauncherTestUtils.setJob(job.job(timestamp));
		jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
	}

}
