package com.base.api.service;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.web.multipart.MultipartFile;

import com.base.api.dto.filter.MediaFilter;
import com.base.api.entities.Media;
import com.base.api.request.dto.FolderRequestDTO;
import com.base.api.request.dto.MediaDTO;
import com.base.api.request.dto.MoveFolderRequestDTO;
import com.base.api.request.dto.RenameFolderRequestDTO;

public interface MediaService {
	
	String createMedia(@Valid MediaDTO mediaDTO, MultipartFile file);
	
	List<Media> searchMedia(MediaFilter filter);
	
	String deleteMedia(UUID id);

	void add(@Valid MediaDTO mediaDTO);
	
	public void init();

	String createFolder(FolderRequestDTO requestDTO);

	String renameFolderOrFile(RenameFolderRequestDTO request);

	String moveFolder(MoveFolderRequestDTO folderRequest);
}
