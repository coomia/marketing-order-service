package com.meiye.repository.role;

import com.meiye.model.role.AuthPermission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2018/8/23 0023.
 */
public interface AuthPermissionRepository extends JpaRepository<AuthPermission,Long> {
}
