package com.meiye;

import com.meiye.StoreApplication;
import com.meiye.bo.role.AuthRoleBo;
import com.meiye.bo.role.AuthRolePermissionBo;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.service.role.AuthRoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthRoleTest {

    @Autowired
    AuthRoleService authRoleService;

    @Test
    public void findAll(){
        System.out.println("xxxxx");
        List<AuthRoleBo> all = authRoleService.findAll();
        System.out.println(all);
    }

    @Test
    public void getOne(){
        AuthRoleBo oneById = authRoleService.findOneById(1L);
        System.out.println(oneById);
    }

    @Test
    public void save(){
        AuthRoleBo authRoleBo = new AuthRoleBo();
        authRoleBo.setName("总监");
        authRoleBo.setBrandIdenty(new Long(1));
        authRoleBo.setSourceFlag(1);

        AuthRolePermissionBo authRolePermissionBo = new AuthRolePermissionBo();
        authRolePermissionBo.setPermissionId(1L);
        authRolePermissionBo.setBrandIdenty(new Long(1));
        authRoleBo.getAuthRolePermissions().add(authRolePermissionBo);
        authRoleService.save(authRoleBo);

    }

    @Test
    public void update(){

    }
}
