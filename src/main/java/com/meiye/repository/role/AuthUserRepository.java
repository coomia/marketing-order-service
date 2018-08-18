package com.meiye.repository.role;

import com.meiye.model.part.DishShop;
import com.meiye.model.role.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 13:08 2018/8/18
 * @Modified By:
 */
public interface AuthUserRepository extends JpaRepository<AuthUser,Long> {

    /**
     * 查找最新加入门店的员工信息
     * @return
     */
    AuthUser findFirst1OrderByServerCreateTimeDesc();

}
