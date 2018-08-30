package com.meiye.service.role;

import com.meiye.bo.role.AuthUserBo;
import com.meiye.model.role.AuthUser;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 13:12 2018/8/18
 * @Modified By:
 */
public interface AuthUserService {
    /**
     * 添加员工
     * @param authUserBo
     */
    void addAuthUser(AuthUserBo authUserBo);


    /**
     * 查找最新加入门店的员工信息
     * @return
     */
    AuthUserBo findLatestAuthUser();

    AuthUserBo getAuthUserBo(String userName);

    Page<AuthUser> getUserPageByCriteria(Integer pageNum, Integer pageSize, AuthUserBo authUserBo);

    AuthUserBo getOneById(Long id);

    void delete(Long id);
}
