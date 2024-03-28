/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.base.api.dto.filter.FilterBase;
import com.base.api.entities.ModuleEntity;
import com.base.api.entities.ModuleRoleEntity;
import com.base.api.entities.Privilege;
import com.base.api.exception.APIException;
import com.base.api.gateway.util.Util;
import com.base.api.repository.PrivilegeRepository;
import com.base.api.request.dto.ModuleDTO;
import com.base.api.service.PrivilegeServices;

import lombok.extern.slf4j.Slf4j;

/**
 * This class implements services for Privileges.
 * 
 * @author minesh_prajapati
 *
 */
@Slf4j
@Service
public class PrivilegeServicesImpl implements PrivilegeServices {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    EntityManager entityManager;

    /**
     * This method return all Privilege.
     */
    @Override
    public List<Privilege> getAllPrivilege() {
        log.info("PrivilegeServicesImpl: Start getAllPrivilege");
        List<Privilege> privileges = privilegeRepository.findAll();
        if (privileges == null || privileges.isEmpty()) {
            log.error("PrivilegeServicesImpl: getAllPrivilege not found");
            throw new APIException("privilege.not.found", HttpStatus.NOT_FOUND);
        }
        log.info("PrivilegeServicesImpl: End getAllPrivilege");
        return privileges;
    }

    /**
     * This method return Privilege base on role id.
     * 
     * @param roleId uuid of role.
     * @return list of Privilege.
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ModuleDTO> getPermissionList(Map<String, String> request) {
        StringBuilder query = new StringBuilder();
        query = createCommonQueryForModule();

        FilterBase filterBase = new FilterBase();
        filterBase.setColumnName("u.id");

        String queryParam = Util.getFilterQuery(filterBase, query.toString());

        List<ModuleEntity> moduleEntities = new ArrayList<ModuleEntity>();
        moduleEntities = entityManager.createQuery(queryParam).getResultList();

        StringBuilder queryRole = new StringBuilder();
        queryRole = createCommonQueryForRole();

        if (request != null) {
            queryRole.append(" r.id = '" + request.get("role_id") + "'");
        }

        FilterBase filterBaseRole = new FilterBase();
        filterBaseRole.setColumnName("u.createdDate");
        queryParam = Util.getFilterQuery(filterBaseRole, queryRole.toString());

        List<ModuleRoleEntity> moduleRoleEntities = new ArrayList<ModuleRoleEntity>();
        moduleRoleEntities = entityManager.createQuery(queryParam).getResultList();
        List<ModuleDTO> moduleDTOs = new ArrayList<ModuleDTO>();
        
        
        List<ModuleEntity> moduleRoleParentEntities = new ArrayList<ModuleEntity>();
        moduleRoleParentEntities = moduleEntities.stream().filter((module) -> module.getParent() == null).sorted(Comparator.comparing(ModuleEntity::getText)).collect(Collectors.toList());
        
        List<ModuleEntity> moduleRoleChildEntities = new ArrayList<ModuleEntity>();
        moduleRoleChildEntities = moduleEntities.stream().filter((module) -> module.getParent() != null).sorted(Comparator.comparing(ModuleEntity::getText)).collect(Collectors.toList());

        setModuleRoleEntityDTO(moduleRoleEntities, moduleDTOs, moduleRoleParentEntities);
        setModuleRoleEntityDTO(moduleRoleEntities, moduleDTOs, moduleRoleChildEntities);

        return moduleDTOs;
    }

    /**
     * Sets the module role entity DTO.
     *
     * @param moduleRoleEntities the module role entities
     * @param moduleDTOs the module DT os
     * @param moduleRoleParentEntities the module role parent entities
     */
    private void setModuleRoleEntityDTO(List<ModuleRoleEntity> moduleRoleEntities, List<ModuleDTO> moduleDTOs,
            List<ModuleEntity> moduleRoleParentEntities) {
        for (ModuleEntity moduleEntity : moduleRoleParentEntities) {

            ModuleDTO moduleDTO = new ModuleDTO();

            moduleDTO.setId(moduleEntity.getId());
            moduleDTO.setText(moduleEntity.getText());
            moduleDTO.setType(moduleEntity.getType());
            moduleDTO.setPermission_key(moduleEntity.getPermissionKey());
            moduleDTO.setParent(
                    moduleEntity.getParent() != null ? String.valueOf(moduleEntity.getParent().getId()) : "#");

            Map<String, Boolean> state = new HashMap<String, Boolean>();
            state.put("opened", true);

            boolean isSelected = false;

            if (moduleEntity.getParent() != null) {
                for (ModuleRoleEntity moduleRoleEntity : moduleRoleEntities) {
                    if (moduleRoleEntity.getModule().equals(moduleEntity)) {
                        isSelected = true;
                        break;
                    }
                }
            }

            if (moduleEntity.getParent() != null)
                state.put("selected", isSelected);

            moduleDTO.setState(state);
            moduleDTOs.add(moduleDTO);
        }
    }

    private StringBuilder createCommonQueryForModule() {
        StringBuilder query = new StringBuilder();
        query.append("select u from ModuleEntity u");
        return query;
    }

    private StringBuilder createCommonQueryForRole() {
        StringBuilder query = new StringBuilder();
        query.append("select u from ModuleRoleEntity u join u.role r where");
        return query;
    }

    /**
     * This method return Privileges by given privilege ids.
     * 
     * @param permissionIds set of uuid of privilege.
     * @return list of Privilege.
     */
    @Override
    public List<Privilege> findAllByIds(Set<UUID> permissionIds) {
        log.info("PrivilegeServicesImpl: findAllByIds {}", permissionIds);
        List<Privilege> privileges = privilegeRepository.findAllById(permissionIds);
        if (privileges == null || privileges.isEmpty()) {
            log.error("PrivilegeServicesImpl: Error findAllByIds {} not found", permissionIds);
            throw new APIException("rprivilege.not.found", HttpStatus.NOT_FOUND);
        }
        log.info("PrivilegeServicesImpl: findAllByIds {}", permissionIds);
        return privileges;
    }


    /**
     * This method return Privilege base on role id.
     * 
     * @param roleId uuid of role.
     * @return list of Privilege.
     */
    @Override
    public List<Privilege> findByRoleID(UUID roleId) {
        log.info("PrivilegeServicesImpl: Start findByRoleID {}", roleId);
        List<Privilege> privileges = privilegeRepository.findByRoleID(roleId);
        if (privileges == null || privileges.isEmpty()) {
            log.error("PrivilegeServicesImpl: findByRoleID {} not found", roleId);
            throw new APIException("privilege.not.found", HttpStatus.NOT_FOUND);
        }
        log.info("PrivilegeServicesImpl: End findByRoleID {}", roleId);
        return privileges;
    }

}