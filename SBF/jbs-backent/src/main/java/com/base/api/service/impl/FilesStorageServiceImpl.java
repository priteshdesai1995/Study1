/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.api.exception.APIException;
import com.base.api.service.FilesStorageService;

import lombok.extern.slf4j.Slf4j;

/**
 * This class implements services for FilesStorageService.
 * 
 * @author minesh_prajapati
 *
 */
@Slf4j
@Service
public class FilesStorageServiceImpl implements FilesStorageService {

	@Override
	public void init(String dir) {
		log.info("FilesStorageServiceImpl: Start init {}", dir);
		File directory = new File(dir);
		if (!directory.exists()) {
			// directory.mkdir();
			// If you require it to make the entire directory path including parents,
			// use directory.mkdirs(); here instead.
			directory.mkdirs();
		} else {
			log.warn("FilesStorageServiceImpl: init Directory already exists {}", dir);
		}
		log.info("FilesStorageServiceImpl: End init {}", dir);
	}

	private Path createDirectory(String path) {
		log.info("FilesStorageServiceImpl: Start createDirectory {}", path);
		Path dirPath = Paths.get(path);
		try {
			// dirPath = Files.createDirectory(dirPath);
			dirPath = Files.createDirectories(dirPath);
			log.info("FilesStorageServiceImpl: End createDirectory {}", path);
			return dirPath;
		} catch (FileAlreadyExistsException e) {
			log.error("FilesStorageServiceImpl: createDirectory already exists " + dirPath, e);
			return dirPath;
		} catch (IOException e) {
			log.error("FilesStorageServiceImpl: createDirectory fail " + dirPath, e);
			// throw new APIException("Could not initialize folder for upload!");
			throw new APIException("directory.create.fail");
		}
	}

	@Override
	public void save(MultipartFile file, String dirPath, String fileName) {
		Path path = Paths.get(dirPath);
		log.info("FilesStorageServiceImpl: Start save {} {}", dirPath, fileName);
		try {
			Files.copy(file.getInputStream(), path.resolve(fileName));
		} catch (Exception e) {
			log.error("FilesStorageServiceImpl: save fail " + dirPath + " " + fileName, e);
			// throw new APIException("Could not store the file. Error: " + e.getMessage());
			throw new APIException("file.save.fail");
		}
		log.info("FilesStorageServiceImpl: End save {} {}", dirPath, fileName);
		// return path.resolve(StringUtil.removeWhiteSpace(fileName)).toString();
	}

	@Override
	public Resource load(String dirPath, String filename) {
		log.info("FilesStorageServiceImpl: Start load {} {}", dirPath, filename);
		try {
			Path path = Paths.get(dirPath);
			Path file = path.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				log.info("FilesStorageServiceImpl: End load {} {}", dirPath, filename);
				return resource;
			} else {
				log.error("FilesStorageServiceImpl: load fail {} {}", dirPath, filename);
				// throw new APIException("Could not read the file!");
				throw new APIException("file.not.found");
			}
		} catch (MalformedURLException e) {
			log.error("FilesStorageServiceImpl: load fail " + dirPath + " " + filename, e);
			throw new APIException("file.service.error");
		}
	}
}
