/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * This interface is use to create directory and save the file.
 * 
 * @author minesh_prajapati
 *
 */
public interface FilesStorageService {

	/**
	 * use to create the parent or child directory if not exist
	 * 
	 * @param dir directory path with name.
	 */
	public void init(String dir);

	/**
	 * use to save file to given directory.
	 * 
	 * @param file     multipart file.
	 * @param dirPath  directory path.
	 * @param fileName file name.
	 */
	public void save(MultipartFile file, String dirPath, String fileName);

	/**
	 * use to get the file by given name and path.
	 * 
	 * @param dirPath  directory path of file.
	 * @param fileName file name.
	 * @return object of resource.
	 */
	public Resource load(String dirPath, String fileName);

}
