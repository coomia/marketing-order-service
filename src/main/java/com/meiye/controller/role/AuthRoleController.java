package com.meiye.controller.role;


import com.meiye.bo.role.AuthRoleBo;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.exception.BusinessException;
import com.meiye.service.role.AuthRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/public/api/role/authRole",produces="application/json;charset=UTF-8")
public class AuthRoleController {

    @Autowired
    AuthRoleService authRoleService;

    @GetMapping("/findAll")
    public ResetApiResult findAll(){
        List<AuthRoleBo> roleBos;
        try{
            roleBos = authRoleService.findAll();
            if(roleBos == null || roleBos.size()<1){
                throw new BusinessException("未找到角色!");
            }
        }catch (Exception e){
            throw new BusinessException("获取所有角色失败!");
        }
        return ResetApiResult.sucess(roleBos);
    }

    @GetMapping("/find/{id}")
    public ResetApiResult getOne(@PathVariable Long id){
        try {
            AuthRoleBo oneById = authRoleService.findOneById(id);
            if(oneById == null){
                throw new BusinessException("未找到角色!");
            }
            return ResetApiResult.sucess(oneById);
        }catch (Exception e){
            throw new BusinessException("获取角色失败!");
        }
    }

    @PostMapping("/save")
    public ResetApiResult save(@RequestBody AuthRoleBo authRoleBo){
        try {
            authRoleService.save(authRoleBo);
        }catch (Exception e){
            throw new BusinessException("添加角色失败!");
        }
        return ResetApiResult.sucess("");
    }

    @PostMapping("/update")
    public ResetApiResult update(@RequestBody AuthRoleBo authRoleBo){
        try {
            authRoleService.update(authRoleBo);
        }catch (Exception e){
            throw new BusinessException("更新角色失败!");
        }
        return ResetApiResult.sucess("");
    }

    @GetMapping("/delete/{id}")
    public ResetApiResult delete(@PathVariable Long id){
        try {
            authRoleService.delete(id);
        }catch (Exception e){
            throw new BusinessException("删除角色失败!");
        }
        return ResetApiResult.sucess("");
    }
}
