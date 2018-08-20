package com.meiye.controller.role;


import com.meiye.bo.role.AuthRoleBo;
import com.meiye.bo.system.ResetApiResult;
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
        return ResetApiResult.sucess(authRoleService.findAll());
    }

    @GetMapping("/find/{id}")
    public ResetApiResult getOne(@PathVariable Long id){
        return ResetApiResult.sucess(authRoleService.findOneById(id));
    }

    @PostMapping("/save")
    public ResetApiResult save(AuthRoleBo authRoleBo){
        authRoleService.save(authRoleBo);
        return ResetApiResult.sucess("");
    }

    @PostMapping("/update")
    public ResetApiResult update(AuthRoleBo authRoleBo){
        authRoleService.update(authRoleBo);
        return ResetApiResult.sucess("");
    }

    @GetMapping("/delete/{id}")
    public ResetApiResult delete(@PathVariable Long id){
        authRoleService.delete(id);
        return ResetApiResult.sucess("");
    }


}
