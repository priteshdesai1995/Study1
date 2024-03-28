package com.humaine.transactional.jobs;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.humaine.transactional.enums.LoopStatus;
import com.humaine.transactional.jobs.listeners.UserEventWriterListener;
import com.humaine.transactional.jobs.writer.UserEventWriter;
import com.humaine.transactional.model.UserEvent;
import com.humaine.transactional.repository.UserEventReaderRepository;
import com.humaine.transactional.util.DateUtils;

@Configuration
public class UserEventJob {

	private static final Logger logger = LogManager.getLogger(UserEventJob.class);

	@Value("${job.chunk.size}")
	private int chunkSize;

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Autowired
	StepBuilderFactory stepBuilderFactory;

	@Autowired
	DataSource dataSource;

	@Autowired
	UserEventReaderRepository eventRepository;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private final OffsetDateTime timestemp = DateUtils.getSchedularRunningOffset();

	@Bean
	@StepScope
	@Lazy
	public RepositoryItemReader<UserEvent> userEventItemReader() {
		logger.info("UserEvent Archive Job: Reader Called");
		Map<String, Sort.Direction> map = new HashMap<>();
		map.put("id", Sort.Direction.ASC);
		List<Object> params = new ArrayList<Object>();
		System.out.println(timestemp);
		params.add(timestemp);
		RepositoryItemReader<UserEvent> repositoryItemReader = new RepositoryItemReader<>();
		repositoryItemReader.setRepository(eventRepository);
		repositoryItemReader.setPageSize(chunkSize);
		repositoryItemReader.setMethodName("findArchiveRecords");
		repositoryItemReader.setArguments(params);
		repositoryItemReader.setSort(map);
		repositoryItemReader.setMaxItemCount(chunkSize);
		return repositoryItemReader;
	}

	@Bean
	@Lazy
	public Step loadUserEvents(Long timestamp) {
		return stepBuilderFactory.get("UserEventsArchiveStep_"+timestamp).<UserEvent, UserEvent>chunk(chunkSize)
				.reader(userEventItemReader()).writer(new UserEventWriter(jdbcTemplate))
				.listener(new UserEventWriterListener(namedParameterJdbcTemplate)).build();
	}

	@Bean
	@Lazy
	public Job job(Long timestamp) throws Exception {
		logger.info("UserEvent Archive Job: Job Called");
		UserEventLoopDecider loopDecider = new UserEventLoopDecider(eventRepository, timestemp);
		FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("USER_EVENT_FLOW");
		Step eventStep = loadUserEvents(timestamp);
		Flow flow = flowBuilder
				.start(eventStep)
				.next(loopDecider).on(LoopStatus.CONTINUE.value()).to(eventStep)
				.from(loopDecider).on(LoopStatus.COMPLETED.value()).end().build();
		return jobBuilderFactory.get("UserEventsArchiveJob_"+timestamp).incrementer(new RunIdIncrementer()).start(flow).end()
				.build();
	}

}
