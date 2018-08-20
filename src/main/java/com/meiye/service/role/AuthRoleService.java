package com.meiye.service.role;

import com.meiye.bo.role.AuthRoleBo;

import java.util.List;

public interface AuthRoleService {

    void save (AuthRoleBo authRoleBo);
    void update(AuthRoleBo authRoleBo);
    void delete(Long id);
    List<AuthRoleBo> findAll();
    AuthRoleBo findOneById(Long id);

}
