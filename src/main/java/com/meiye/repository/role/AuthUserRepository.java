package com.meiye.repository.role;

import com.meiye.model.part.DishShop;
import com.meiye.model.role.AuthUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 13:08 2018/8/18
 * @Modified By:
 */
@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser,Long>,JpaSpecificationExecutor<AuthUser> {

    /**
     * 查找最新加入门店的员工信息
     * @return
     */
    AuthUser findFirstByOrderByServerCreateTimeDesc();

    AuthUser findFirstByAccount(String account);

    AuthUser findByIdAndStatusFlag(Long id, Integer  statusFlag);

    AuthUser findByAccountAndStatusFlag(String accout, Integer  statusFlag);

    @Modifying
    @Query(value = "update AuthUser au set au.statusFlag = 2 where au.id = ?1")
    void deleteById(Long id);
}
