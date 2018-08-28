package com.meiye.service.role.impl;

import com.meiye.model.role.AuthPermission;
import com.meiye.repository.role.AuthPermissionRepository;
import com.meiye.service.role.AuthPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Shawns
 * @Date: 2018/8/27 23:52
 * @Version 1.0
 */
@Service
public class AuthPermissionServiceImpl implements AuthPermissionService {

    @Autowired
    AuthPermissionRepository authPermissionRepository;

    @Override
    public List<AuthPermission> getAll() {
        return authPermissionRepository.findAllByStatusFlag(1);
    }
}
