package com.meiye.service.role;

import com.meiye.bo.role.AuthUserBo;
import com.meiye.model.role.AuthUser;

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

}
