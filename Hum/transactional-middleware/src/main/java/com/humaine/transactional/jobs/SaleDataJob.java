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
import com.humaine.transactional.jobs.listeners.SaleDataWriterListener;
import com.humaine.transactional.jobs.writer.SaleDataWriter;
import com.humaine.transactional.model.Sale;
import com.humaine.transactional.repository.SaleDataReaderRepository;
import com.humaine.transactional.util.DateUtils;

@Configuration
public class SaleDataJob {

	private static final Logger logger = LogManager.getLogger(SaleDataJob.class);

	@Value("${job.chunk.size}")
	private int chunkSize;

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Autowired
	StepBuilderFactory stepBuilderFactory;

	@Autowired
	DataSource dataSource;

	@Autowired
	SaleDataReaderRepository saleDataReaderRepository;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private final OffsetDateTime timestemp = DateUtils.getSchedularRunningOffset();

	@Bean
	@Lazy
	@StepScope
	public RepositoryItemReader<Sale> saleDataItemReader() {
		logger.info("SaleData Archive Job: Reader Called");
		Map<String, Sort.Direction> map = new HashMap<>();
		map.put("id", Sort.Direction.ASC);
		List<Object> params = new ArrayList<Object>();
		params.add(timestemp);
		RepositoryItemReader<Sale> repositoryItemReader = new RepositoryItemReader<>();
		repositoryItemReader.setRepository(saleDataReaderRepository);
		repositoryItemReader.setPageSize(chunkSize);
		repositoryItemReader.setMethodName("findArchiveRecords");
		repositoryItemReader.setArguments(params);
		repositoryItemReader.setSort(map);
		repositoryItemReader.setMaxItemCount(chunkSize);
		return repositoryItemReader;
	}

	@Bean
	@Lazy
	public Step loadSaleData(Long timestamp) {
		return stepBuilderFactory.get("SaleDataArchiveStep_"+timestamp).<Sale, Sale>chunk(chunkSize).reader(saleDataItemReader())
				.writer(new SaleDataWriter(jdbcTemplate))
				.listener(new SaleDataWriterListener(namedParameterJdbcTemplate)).build();
	}

	@Bean
	@Lazy
	public Job saleJob(Long timestamp) throws Exception {
		logger.info("SaleData Archive Job: Job Called");
		SaleDataLoopDecider loopDecider = new SaleDataLoopDecider(saleDataReaderRepository, timestemp);
		FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("SALE_DATA_FLOW");
		Step step = loadSaleData(timestamp);
		Flow flow = flowBuilder
				 .start(step)
				.next(loopDecider).on(LoopStatus.CONTINUE.value()).to(step)
				.from(loopDecider).on(LoopStatus.COMPLETED.value()).end().build();
		return jobBuilderFactory.get("SaleDataArchiveJob_"+timestamp).incrementer(new RunIdIncrementer()).start(flow).end()
				.build();
	}
}
