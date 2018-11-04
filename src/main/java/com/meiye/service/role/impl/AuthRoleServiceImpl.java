package com.meiye.service.role.impl;

import com.meiye.bo.role.AuthPermissionBo;
import com.meiye.bo.role.AuthRoleBo;
import com.meiye.bo.role.AuthRolePermissionBo;
import com.meiye.model.role.AuthPermission;
import com.meiye.model.role.AuthRole;
import com.meiye.model.role.AuthRolePermission;
import com.meiye.repository.role.AuthPermissionRepository;
import com.meiye.repository.role.AuthRolePermissionRepository;
import com.meiye.repository.role.AuthRoleRepository;
import com.meiye.service.role.AuthRoleService;
import com.meiye.system.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthRoleServiceImpl implements AuthRoleService {

    @Autowired
    AuthRoleRepository authRoleRepository;

    @Autowired
    AuthRolePermissionRepository authRolePermissionRepository;

    @Autowired
    AuthPermissionRepository authPermissionRepository;

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public void save(AuthRoleBo authRoleBo) {
        if(authRoleBo != null){
            //save AuthRole
            AuthRole authRole = authRoleBo.copyTo(AuthRole.class);
            authRoleRepository.save(authRole);
            //save permissopn
            if(authRoleBo.getAuthRolePermissions().size()>0){
                authRoleBo.getAuthRolePermissions().forEach(authRolePermissionBo ->{
                    AuthRolePermission authRolePermission = authRolePermissionBo.copyTo(AuthRolePermission.class);
                    authRolePermission.setRoleId(authRole.getId());
                    authRolePermissionRepository.save(authRolePermission);
                });
            }
        }
    }

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public void update(AuthRoleBo authRoleBo){
        if(authRoleBo != null){
            AuthRole authRole = authRoleBo.copyTo(AuthRole.class);
            authRoleRepository.save(authRole);
            //先删除再保存
            authRolePermissionRepository.deleteAuthRolePermissionByRoleId(authRoleBo.getId());
            //save permissopn
            if(authRoleBo.getAuthRolePermissions().size()>0){
                authRoleBo.getAuthRolePermissions().forEach(authRolePermissionBo ->{
                    AuthRolePermission authRolePermission = authRolePermissionBo.copyTo(AuthRolePermission.class);
                    authRolePermission.setRoleId(authRole.getId());
                    authRolePermissionRepository.save(authRolePermission);
                });
            }
        }
    }

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public void delete(Long id){
        if(id != null){
            authRoleRepository.deleteById(id);
            authRolePermissionRepository.deleteAuthRolePermissionByRoleId(id);
        }
    }

    @Override
    public List<AuthRoleBo> findAll() {
        List<AuthRole> authRoles = authRoleRepository.getAuthRoleByStatusFlagAndShopIdenty(1, WebUtil.getCurrentStoreId());
        if(authRoles != null && authRoles.size()>0){
            List<AuthRoleBo> AuthRoleBos = new ArrayList<>();
            authRoles.forEach(authRole ->{
                AuthRoleBo authRoleBo = findOneById(authRole.getId());
                AuthRoleBos.add(authRoleBo);
            });
            return AuthRoleBos;
        }
        return null;
    }

    @Override
    public AuthRoleBo findOneById(Long id) {
        AuthRole authRole = authRoleRepository.findByIdAndStatusFlagAndShopIdenty(id, 1, WebUtil.getCurrentStoreId());
        if(authRole != null){
            AuthRoleBo authRoleBo = authRole.copyTo(AuthRoleBo.class);
            List<AuthRolePermission> authRolePermissions = authRolePermissionRepository.findByRoleIdAndStatusFlag(id, 1);
            if(authRolePermissions != null  && authRolePermissions.size()>0){
                List<AuthRolePermissionBo> authRolePermissionBos = new ArrayList<>();
                authRolePermissions.forEach(authRolePermission->{
                    AuthRolePermissionBo authRolePermissionBo = authRolePermission.copyTo(AuthRolePermissionBo.class);
                    AuthPermission authPermission=authPermissionRepository.findById(authRolePermission.getPermissionId()).get();
                    authRolePermissionBo.setAuthPermissionBo(authPermission==null?null:authPermission.copyTo(AuthPermissionBo.class));
                    authRolePermissionBos.add(authRolePermissionBo);
                });
                authRoleBo.setAuthRolePermissions(authRolePermissionBos);
            }
            return authRoleBo;
        }
        return null;
    }
}
