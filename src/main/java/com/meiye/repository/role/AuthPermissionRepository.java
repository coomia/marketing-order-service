package com.meiye.repository.role;

import com.meiye.model.role.AuthPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator on 2018/8/23 0023.
 */
public interface AuthPermissionRepository extends JpaRepository<AuthPermission,Long> {
    List<AuthPermission> findAllByStatusFlag(Integer statusFlag);
}
