package com.base.api.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.base.api.entities.Privilege;
import com.base.api.entities.UserRole;
import com.base.api.exception.PermissionException;
import com.base.api.exception.UserRoleException;
import com.base.api.repository.PrivilegeRepository;
import com.base.api.repository.UserRoleRepository;
import com.base.api.service.RolePermissionService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author preyansh_prajapati
 *
 */
@Service(value = "rolePermissionService")
@Slf4j
public class RolePermissionServiceImpl implements RolePermissionService {

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Override
	public void addPermissionToRole(String roleId, List<String> permissionIds) {
		log.info("RolePermissionServiceImpl : addPermissionToRole");

		UserRole role = getUserRoleById(roleId);
		// get existing privileges first 
		List<Privilege> existingPrivileges = new ArrayList<Privilege>(role.getPrivileges());
		
		for (String permisionId : permissionIds) {
			Privilege permission = getPermissionById(permisionId);

			if (permission == null || null == permission.getId()) {
				log.error("no permission found with id : " + permission.getId());
			}
			existingPrivileges.add(permission);
		}
		
		// remove duplicates by converting list to set 
		
		Set<Privilege> newPrivilegesSet = new HashSet<Privilege>(existingPrivileges);
		existingPrivileges.clear();
		existingPrivileges.addAll(newPrivilegesSet);
		role.setPrivileges(existingPrivileges);
		log.info("Permissions set to the role");
	}

	@Override
	public void removePermissionFromRole(String roleId, List<String> permissionIds) {
		log.info("RolePermissionServiceImpl : removePermissionFromRole");
		UserRole role = getUserRoleById(roleId);
		Collection<Privilege> existingPrivileges = role.getPrivileges();
		List<String> existingPrivilegeIds = new ArrayList<String>();
		List<Privilege> newPrivilege = new ArrayList<Privilege>();

		if (existingPrivileges.isEmpty()) {
			log.info("This role doesn't contains any permissions");
			throw new UserRoleException("this.role.doesn't.contains.any.permissions.can't.remove.any.permissions");
		}

		try {
			// get list of privilageIds
			for (Privilege existingPrivilege : existingPrivileges) {
				existingPrivilegeIds.add(existingPrivilege.getId().toString());
			}

			// now compare and remove
			for (String permissionId : permissionIds) {
				if (existingPrivilegeIds.contains(permissionId)) {
					existingPrivilegeIds.remove(permissionId);
				}
			}

			// now create new privileg list
			for (String privilegeId : existingPrivilegeIds) {
				newPrivilege.add(getPermissionById(privilegeId));
			}

			// set new privileges to the role
			role.setPrivileges(newPrivilege);
			userRoleRepository.save(role);

			log.info("RolePermissionServiceImpl : removePermissionFromRole : Permission removed successfully.");

		} catch (UserRoleException exception) {
			log.error("Somthing went wrong while removing permissions from the role : " + exception);
			throw new UserRoleException("somthing.went.wrong.while.removing.permissions.from.the.role : " + exception);
		} catch (Exception e) {
			log.error("Somthing went wrong while removing permissions from the role : " + e);
			throw new UserRoleException("somthing.went.wrong.while.removing.permissions.from.the.role : " + e);
		}
	}

	public UserRole getUserRoleById(String roleId) {

		if (roleId != null) {
			Optional<UserRole> userRole = userRoleRepository.findById(UUID.fromString(roleId));

			if (userRole.isPresent()) {
				return userRole.get();
			} else {
				log.error("No Role found with : " + roleId);
				throw new UserRoleException("no.role.found.with.this.roleid", HttpStatus.NOT_FOUND);
			}
		} else {
			log.error("Role ID is null ");
			throw new UserRoleException("roleId.can.not.be.null", HttpStatus.BAD_REQUEST);
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
