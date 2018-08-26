package com.meiye.repository.role;

import com.meiye.model.role.AuthRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthRoleRepository extends JpaRepository<AuthRole,Long> {

    List<AuthRole> getAuthRoleByStatusFlag(Integer statusFlag);
    AuthRole findByIdAndStatusFlag(Long id,Integer statusFlag);

    @Modifying
    @Query(value = "update AuthRole ar set ar.statusFlag = 2 where ar.id = id")
    void deleteById(@Param(value = "id")Long id);
}

