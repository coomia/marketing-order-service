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

    List<AuthRole> getAuthRoleByStatusFlagAndShopIdenty(Integer statusFlag,Long shopIdenty);
    AuthRole findByIdAndStatusFlagAndShopIdenty(Long id,Integer statusFlag,Long shopIdenty);

    @Modifying
    @Query(value = "update AuthRole ar set ar.statusFlag = 2 where ar.id = ?1")
    void deleteById(Long id);
}

