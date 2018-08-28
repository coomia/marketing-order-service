package com.meiye.service.role;

import com.meiye.model.role.AuthPermission;

import java.util.List;

/**
 * @Author: Shawns
 * @Date: 2018/8/27 23:51
 * @Version 1.0
 */
public interface AuthPermissionService {

    List<AuthPermission> getAll();
}
