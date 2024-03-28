package com.humaine.portal.api.util;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefNotAdvertisedException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.humaine.portal.api.config.GitConfig;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.AccountTracker;
import com.humaine.portal.api.rest.repository.AccountTrackerRepository;

@Component
@Scope("singleton")
public class TrackerGenerator {

	@Value("${git.clone.locaton}")
	private String location;

	@Value("${git.file}")
	private String file;

	@Value("${tracker.url}")
	private String trackerUrl;

	@Value("${git.output.file.directory}")
	private String outputDirectory;

	@Value("${git.output.file.name}")
	private String fileName;

	@Value("${git.commit.message}")
	private String commitMessage;

	@Autowired
	private GitConfig git;

	@Autowired
	private AccountTrackerRepository accountTrackerRepository;

	@Autowired
	private ServletContext servletContext;

	public void genrateTracker(Account account) throws WrongRepositoryStateException, InvalidConfigurationException,
			InvalidRemoteException, CanceledException, RefNotFoundException, RefNotAdvertisedException, NoHeadException,
			TransportException, GitAPIException {
		OffsetDateTime timestemp = DateUtils.getCurrentTimestemp();
		String inputFileName = URLUtils.getURL(location) + file;
		String output = account.getId() + "-" + UUID.randomUUID().toString() + "-" + fileName;
		String outputFileForPush = URLUtils.getURL(outputDirectory) + output;
		String outputFileName = URLUtils.getURL(location) + outputFileForPush;
		git.pull();
		String content = FileUtils.readFile(inputFileName);
		content = content.replace("\"##ACCOUNT_ID##\"", account.getId() + "");
		content = content.replace("##API_URL##", CommonUtils.formatURL(trackerUrl));
		FileUtils.writeFile(content, outputFileName);
		git.pushFile(outputFileForPush, commitMessage + ", Account:" + account.getId());
		AccountTracker accountTracker = new AccountTracker(account, output, false, "", timestemp, timestemp);
		accountTrackerRepository.save(accountTracker);
	}
}
