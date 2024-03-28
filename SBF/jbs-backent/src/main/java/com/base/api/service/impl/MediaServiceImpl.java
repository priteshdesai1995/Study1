package com.base.api.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.api.dto.filter.MediaFilter;
import com.base.api.entities.Media;
import com.base.api.repository.MediaRepository;
import com.base.api.request.dto.FolderRequestDTO;
import com.base.api.request.dto.MediaDTO;
import com.base.api.request.dto.MoveFolderRequestDTO;
import com.base.api.request.dto.RenameFolderRequestDTO;
import com.base.api.service.MediaService;
import com.base.api.utils.Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MediaServiceImpl implements MediaService {

    @Value("${file.upload.path: ''}")
    private String uploadPath;

    String userDirectory = new File("").getAbsolutePath();
    private final Path root = Paths.get(userDirectory + "/upload/");

    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    EntityManager entityManager;

    @Override
    public String createMedia(@Valid MediaDTO mediaDTO, MultipartFile file) {

        log.info("MediaServiceImpl : createMedia");
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        Media media = new Media();
        media = mapDTOToEntity(mediaDTO, media);
        media.setCreatedby(Util.getFullName());
        mediaRepository.save(media);
        return HttpStatus.OK.name();
    }

    private Media mapDTOToEntity(@Valid MediaDTO mediaDto, Media media) {

        log.info(mediaDto.toString());
        media.setCreatedby(mediaDto.getCreatedby());
        media.setDisk(mediaDto.getDisk());
        log.info(mediaDto.getName().toString());
        media.setName(mediaDto.getName());
        if (mediaDto.getFolderPath().isEmpty() || mediaDto.getFolderPath() == null) {
            media.setFolderpath("");
        } else {
            media.setFolderpath(mediaDto.getFolderPath());
        }
        media.setSize(mediaDto.getSize());
        media.setType(mediaDto.getType());
        media.setMedia_url(mediaDto.getMedia_url());
        media.setMimetype(mediaDto.getMimetype());
        media.setRelative_path(mediaDto.getFolderPath() + mediaDto.getName());
        return media;
    }

    @Override
    public List<Media> searchMedia(MediaFilter filter) {

        log.info("MediaServiceImpl : searchMedia()");
        List<Media> list = new ArrayList<Media>();
        StringBuilder query = new StringBuilder();
        query = createCommonQuery();
        if (filter.getSearchBy() != null && !filter.getSearchBy().isEmpty()) {
            query.append(" and UPPER(m.name) LIKE UPPER('%" + filter.getSearchBy() + "%') ");
        }
        if (filter.getFolderPath() != null && !filter.getFolderPath().isEmpty()) {
            query.append(" and m.folder_path LIKE '" + filter.getFolderPath() + "'");
        }
        if (filter.getFilterType() != null && !filter.getFilterType().isEmpty()) {
            query.append(" and split_part(mime_type , '/', 1) IN ('" + filter.getFilterType() + "','folder')");
        }
        if (filter.getOrderBy() != null && !filter.getOrderBy().isEmpty()) {
            query.append(" ORDER BY " + filter.getSortBy() + " " + filter.getOrderBy() + "");
        }
        List<Media> rows = new ArrayList<Media>();
        Query natQuery = entityManager.createNativeQuery(query.toString(), Media.class);
        rows = natQuery.getResultList();
        return rows;
    }

    private StringBuilder createCommonQuery() {
        log.info("createCommonQuery");
        StringBuilder query = new StringBuilder();
        query.append("select * from media m");
        query.append(" where m.id is not null");
        return query;
    }

//	public static List<Map<String, Object>> convertTuplesToMap(List<Tuple> tuples) {
//		log.info("MediaServiceImpl : convertTurplesToMap");
//		List<Map<String, Object>> result = new ArrayList<>();
//		for (Tuple single : tuples) {
//			Map<String, Object> tempMap = new HashMap<>();
//			for (TupleElement<?> key : single.getElements()) {
//				tempMap.put(key.getAlias(), single.get(key));
//			}
//			result.add(tempMap);
//		}
//		return result;
//	}

    public String deleteMedia(UUID id) {
        log.info("MediaServiceImpl : deleteMedia()");
        Optional<Media> media = Optional.empty();
        media = mediaRepository.findById(id);

        if (media != null) {
            mediaRepository.delete(media.get());
            List<Media> children = new ArrayList<Media>();
            children = mediaRepository.findByParentId(id);
            if (children != null)
                mediaRepository.deleteAll(children);
            return HttpStatus.OK.name();
        } else {
            return HttpStatus.NOT_FOUND.name();
        }
    }

    @Override
    public void add(@Valid MediaDTO mediaDTO) {
        log.info("MediaServiceImpl : add()");
        Media mediaEntity = new Media();
        mediaEntity = mapDTOToEntity(mediaDTO, mediaEntity);
        mediaRepository.save(mediaEntity);
    }

    @Override
    public void init() {
        log.info("MediaServiceImpl : init()");
        try {
//            if (root == null) {
                Files.createDirectory(root);
//            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public String createFolder(FolderRequestDTO requestDTO) {
        log.info("MediaServiceImpl : createFolder");
        Media media = new Media();
        media.setName(requestDTO.getName());

        if (requestDTO.getFolderPath().isEmpty() || requestDTO.getFolderPath() == null) {
            media.setFolderpath("");
        } else {
            media.setFolderpath(requestDTO.getFolderPath());
        }
        media.setParent_id(requestDTO.getParentId());
        media.setCreatedby(Util.getFullName());
        media.setDisk("local");
        media.setType("folder");
        media.setMimetype("folder");
        media.setRelative_path(requestDTO.getFolderPath() + requestDTO.getName());
        mediaRepository.save(media);
        return HttpStatus.OK.name();
    }

    @Override
    public String renameFolderOrFile(RenameFolderRequestDTO request) {
        log.info("MediaServiceImpl : renameFolderOrFile()");
        Optional<Media> entity = Optional.empty();
        entity = mediaRepository.findById(request.getId());
        if (entity != null) {
            entity.get().setName(request.getName());
            entity.get().setRelative_path(entity.get().getFolderpath() + entity.get().getName());
        }
        List<Media> entities = new ArrayList<Media>();
        entities = mediaRepository.findByParentId(entity.get().getId());
        log.info(entities.toString());
        if (entities != null) {

        }
        mediaRepository.save(entity.get());
        return HttpStatus.OK.name();
    }

    @Override
    public String moveFolder(MoveFolderRequestDTO folderRequest) {
        log.info("MediaServiceImpl : move()");
        Optional<Media> entity = Optional.empty();
        entity = mediaRepository.findById(folderRequest.getSourceId());
        if (folderRequest.getSpecificFolder()) {
            Optional<Media> destinationEntity = Optional.empty();
            destinationEntity = mediaRepository.findById(folderRequest.getDestinationId());
            entity.get().setFolderpath(destinationEntity.get().getRelative_path() + "/");
            entity.get().setRelative_path(destinationEntity.get().getRelative_path() + "/" + entity.get().getName());
        } else {
            String folderpath = entity.get().getFolderpath();
            String path[] = folderpath.split("/");
            String lastPath = path[path.length - 1];
            folderpath = StringUtils.remove(folderpath, lastPath + "/");
            entity.get().setFolderpath(folderpath);
            entity.get().setRelative_path(folderpath + entity.get().getName());
        }
        entity.get().setParent_id(folderRequest.getDestinationId());
        mediaRepository.save(entity.get());
        return HttpStatus.OK.name();
    }

}
