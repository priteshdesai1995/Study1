package com.humaine.collection.api.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.humaine.collection.api.es.projection.model.EnddedSession;
import com.humaine.collection.api.es.repository.impl.ESPageLoadDataRespository;
import com.humaine.collection.api.es.repository.impl.ESUserEventRepository;
import com.humaine.collection.api.model.UserEvent;
import com.humaine.collection.api.rest.repository.UserEventRepository;
import com.humaine.collection.api.rest.repository.UserSessionRepository;
import com.humaine.collection.api.rest.service.UserSessionService;
import com.humaine.collection.api.util.DateUtils;

@Component
@EnableScheduling
public class Scheduler {

	@Autowired
	UserSessionService userSessionService;

	@Autowired
	private UserSessionRepository userSessionRepository;

	@Autowired
	private UserEventRepository userEventRepository;

	@Autowired
	private ESUserEventRepository esUserEventRepository;

	@Autowired
	private ESPageLoadDataRespository esPageLoadDataRespository;

	private static final Logger logger = LogManager.getLogger(Scheduler.class);

	private Boolean jobRunning = false;

	@Value("${elasticsearch.index.preserve.size}")
	int preserveElement;

	@Scheduled(cron = "${session.expired.scheduler.cron.run.expression}")
	public void endUserSessionScheduler() {
		if (this.jobRunning == true) {
			logger.warn("Open Session End Scheduler: Scheduler Already Running. Canceling next execution",
					DateUtils.getCurrentTimestemp());
			return;
		}
		this.jobRunning = true;
		logger.info("Open Session End Scheduler: start: start at-> {}", DateUtils.getCurrentTimestemp());
		List<EnddedSession> endedSessionList = userSessionRepository.getEndedUserSessions();
		logger.info("Total Unclose Sessions: -> {}", endedSessionList.size());
		AtomicInteger counter = new AtomicInteger(0);
		List<UserEvent> userEvents = new ArrayList<UserEvent>();
		if (!endedSessionList.isEmpty()) {
			endedSessionList.forEach(e -> {
				logger.info("{}: Close session: -> {}", counter.incrementAndGet(), e);
				UserEvent ev = userSessionService.saveEndEvent(e);
				userEvents.add(ev);
			});
			logger.info("Elastic Indexing start: -> {}",
					userEvents.stream().map(e -> e.getId()).collect(Collectors.toList()));
			esUserEventRepository.indexUserEvents(userEvents.stream().map(e -> e.getId()).collect(Collectors.toList()));
		}
		this.jobRunning = false;
		logger.info("Open Session End Scheduler Scheduler:end: complete at-> {}", DateUtils.getCurrentTimestemp());
	}

	@Scheduled(cron = "${session.scheduler.cron.run.expression.delete.indices}")
	public void deleteIndices() {
		esUserEventRepository.deleteUserEventIndices(preserveElement);
		esPageLoadDataRespository.deletePageLoadIndices(preserveElement);
	}

}
