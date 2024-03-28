package com.base.api.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.base.api.entities.Privilege;
import com.base.api.exception.PermissionException;
import com.base.api.repository.PrivilegeRepository;
import com.base.api.request.dto.PermissionDTO;
import com.base.api.service.PermissionService;

import lombok.extern.slf4j.Slf4j;

@Service(value = "permissionService")
@Slf4j
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Override
	public void createPermission(PermissionDTO permissionDTO) {
		log.info("PermissionServiceImpl : createPermission");

		try {
			Privilege privilege = new Privilege(permissionDTO.getPermissionName());
			privilegeRepository.save(privilege);
			log.info("permission created successfully");
		} catch (PermissionException exception) {
			log.info("Exception while creating Role : " + exception);
			throw new PermissionException("Exception while creating Role : " + exception);
		}
	}

	@Override
	public void deletePermission(List<String> permissionIds) {

		log.info("PermissionServiceImpl : deletePermissions");

		if (permissionIds.size() == 0) {
			log.error("permissions are empty");
			throw new PermissionException("permissions.are.empty", HttpStatus.BAD_REQUEST);
		}

		for (String permisionId : permissionIds) {
			Privilege permission = getPermissionById(permisionId);

			if (permission == null || null == permission.getId()) {
				log.error("no permission found with id : " + permission.getId());
			}

			privilegeRepository.deleteById(permission.getId());
			log.info("Permission deleted with Id : " + permission.getId());
		}
	}

	private Privilege getPermissionById(String permissionId) {

		if (permissionId != null) {
			Optional<Privilege> permission = privilegeRepository.findById(UUID.fromString(permissionId));

			if (permission.isPresent()) {
				return permission.get();
			} else {
				log.error("No Role found with : " + permissionId);
				throw new PermissionException("no.role.found.with.this.roleid", HttpStatus.NOT_FOUND);
			}
		} else {
			log.error("Role ID is null ");
			throw new PermissionException("roleId.can.not.be.null", HttpStatus.BAD_REQUEST);
		}
	}
}
