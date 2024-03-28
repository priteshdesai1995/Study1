package com.humaine.portal.api.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefNotAdvertisedException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class GitConfig {

	@Value("${git.url}")
	String url;

	@Value("${git.branch}")
	String branch;

	@Value("${git.clone.locaton}")
	String cloneLocation;

	@Value("${git.username}")
	String username;

	@Value("${git.password}")
	String password;

	private Git git;

	private CredentialsProvider credentials;

	@PostConstruct
	public void cloneRepo() throws InvalidRemoteException, TransportException, GitAPIException, IOException {
		this.credentials = new UsernamePasswordCredentialsProvider(username, password);

		try {
			Path repoPath = Paths.get(cloneLocation);
			if (repoPath.toFile().exists()) {
				cleanRepo();
			}
			this.git = Git.cloneRepository().setURI(url).setDirectory(repoPath.toFile()).setBranch(branch)
					.setCredentialsProvider(this.credentials).call();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@PreDestroy
	public void cleanRepo() throws IOException {
		FileUtils.deleteDirectory(new File(cloneLocation));
	}

	public void pull() throws WrongRepositoryStateException, InvalidConfigurationException, InvalidRemoteException,
			CanceledException, RefNotFoundException, RefNotAdvertisedException, NoHeadException, TransportException,
			GitAPIException {
		git.pull().setCredentialsProvider(this.credentials).call();
	}

	public void pushFile(String filepattern, String message) throws NoFilepatternException, GitAPIException {
		if (git.status().call().isClean() == false) {
			git.add().addFilepattern(filepattern).call();
			git.add().setUpdate(true);
			git.commit().setMessage(message).call();

			PushCommand pushCommand = git.push();
			pushCommand.setCredentialsProvider(this.credentials);
			pushCommand.call();
		}
	}
}
