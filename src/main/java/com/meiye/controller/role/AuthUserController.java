package com.meiye.controller.role;

import com.meiye.bo.role.AuthUserBo;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.exception.BusinessException;
import com.meiye.model.role.AuthUser;
import com.meiye.service.role.AuthUserService;
import com.meiye.util.AccountValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 13:58 2018/8/18
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/public/api/role/authUser",produces="application/json;charset=UTF-8")
public class AuthUserController {

    @Autowired
    AuthUserService authUserService;


    @GetMapping("/getLatestAuthUser")
    public ResetApiResult getLatestAuthUser(HttpServletResponse response){
        return ResetApiResult.sucess(authUserService.findLatestAuthUser());
    }

    @PostMapping("/addAuthUser")
    public ResetApiResult loadBrandTypes(@RequestBody AuthUserBo authUserBo){
        String mobile = authUserBo.getMobile();
        String identityCard = authUserBo.getIdentityCard();
        String errorMsg ="";
        if(!AccountValidatorUtil.isMobile(mobile)){
            errorMsg = "请输入正确的手机号！";
            return ResetApiResult.error("",errorMsg);
        }
        if(!AccountValidatorUtil.isIDCard(identityCard)){
            errorMsg = "请输入正确的身份证号！";
            return ResetApiResult.error("",errorMsg);
        }
        authUserService.addAuthUser(authUserBo);
        return ResetApiResult.sucess("");
    }

    @PostMapping("/getUserPage")
    public ResetApiResult getUserPageByCriteria(@RequestBody AuthUserBo authUserBo){
        try {
            Page<AuthUser> page = authUserService.getUserPageByCriteria(authUserBo.getPageNum(), authUserBo.getPageSize(), authUserBo);
            return ResetApiResult.sucess(page);
        }catch (Exception e){
            throw new BusinessException("获取员工分页失败!");
        }
    }

    @GetMapping("/load/{id}")
    public ResetApiResult load(@PathVariable Long id){
        try {
            if(authUserService.getOneById(id) ==null)
                throw new BusinessException("未找到该员工!");
            return ResetApiResult.sucess(authUserService.getOneById(id));
        }catch (Exception e){
            throw new BusinessException("获取员工失败!");
        }
    }

    @GetMapping("/delete/{id}")
    public ResetApiResult delete(@PathVariable Long id){
        try {
            authUserService.delete(id);
        }catch (Exception e){
            throw new BusinessException("删除员工失败!");
        }
        return ResetApiResult.sucess("");
    }

}
