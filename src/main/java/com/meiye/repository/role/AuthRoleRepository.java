package com.meiye.repository.role;

import com.meiye.model.role.AuthRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthRoleRepository extends JpaRepository<AuthRole,Long> {

    List<AuthRole> getAuthRoleByStatusFlag(Integer statusFlag);
    AuthRole findByIdAndStatusFlag(Long id,Integer statusFlag);
}
