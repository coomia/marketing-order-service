package com.meiye.repository.role;

import com.meiye.model.role.AuthRole;
import com.meiye.model.role.AuthRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthRolePermissionRepository extends JpaRepository<AuthRolePermission,Long> {

    @Modifying
    @Query(value = "update AuthRolePermission arp set arp.statusFlag = 2 where arp.id = ?1")
    void deleteAuthRolePermissionByRoleId(Long id);

    List<AuthRolePermission> findByRoleIdAndStatusFlag(Long roleId,Integer statusFlag);
}
