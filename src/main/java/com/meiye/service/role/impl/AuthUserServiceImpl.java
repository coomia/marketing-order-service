package com.meiye.service.role.impl;

import com.meiye.bo.role.AuthUserBo;
import com.meiye.exception.BusinessException;
import com.meiye.model.role.AuthUser;
import com.meiye.repository.role.AuthUserRepository;
import com.meiye.service.role.AuthRoleService;
import com.meiye.service.role.AuthUserService;
import com.meiye.system.util.WebUtil;
import com.meiye.util.Constants;
import com.meiye.util.ObjectUtil;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 13:12 2018/8/18
 * @Modified By:
 */
@Service
public class AuthUserServiceImpl implements AuthUserService {

    @Autowired
    AuthUserRepository authUserRepository;

    @Autowired
    AuthRoleService authRoleService;

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public void addAuthUser(AuthUserBo authUserBo) {
        AuthUser authUser = authUserBo.copyTo(AuthUser.class);
        boolean userNameUsedByExistUser=false;
        if(authUserBo.getId()!=null&&authUserBo.getId()>0) {
            Optional<AuthUser> authUserOptional=authUserRepository.findById(authUser.getId());
            if(!authUserOptional.isPresent())
                throw new BusinessException("员工不存在");
            AuthUser existUser=authUserOptional.get();
            authUser.setPassword(existUser.getPassword());
            authUser.setPasswordNum(existUser.getPasswordNum());
//            if(!ObjectUtils.isEmpty(authUserBo.getPassword()))
//                authUser.setPassword(new Sha1Hash(authUser.getPassword(), authUser.getName(), 100).toHex());
//            if(!ObjectUtils.isEmpty(authUserBo.getPasswordNum()))
//                authUser.setPasswordNum(new Sha1Hash(authUser.getPasswordNum(), authUser.getName(), 100).toHex());
            if(ObjectUtil.equals(authUser.getAccount(),existUser.getAccount()))
                userNameUsedByExistUser=true;
            if(authUser.getEnabledFlag()==null)
                authUser.setEnabledFlag(existUser.getEnabledFlag());
            if(authUser.getSourceFlag()==null)
                authUser.setSourceFlag(existUser.getSourceFlag());
        }else{
            authUser.setEnabledFlag(1);
            authUser.setSourceFlag(1);
        }

        if(!ObjectUtils.isEmpty(authUserBo.getPassword()))
            authUser.setPassword(new Sha1Hash(authUserBo.getPassword(), authUser.getAccount(), 100).toHex());
        if(!ObjectUtils.isEmpty(authUserBo.getPasswordNum()))
            authUser.setPasswordNum(new Sha1Hash(authUserBo.getPasswordNum(), authUser.getAccount(), 100).toHex());

        authUser.setServerUpdateTime(new Timestamp(System.currentTimeMillis()));;
        if(!userNameUsedByExistUser&&Objects.nonNull(authUserRepository.findByAccountAndShopIdenty(authUser.getAccount(), authUser.getShopIdenty()))){
            throw new BusinessException("登录名已被注册，请输入新的登陆账户！");
        }
        try {
             authUserRepository.save(authUser);
        }catch (Exception e){
            throw new BusinessException("添加员工到数据库失败!");
        }
    }

    @Override
    public AuthUserBo findLatestAuthUser() {
        AuthUser authUser = authUserRepository.findFirstByBrandIdentyAndShopIdentyOrderByServerCreateTimeDesc(WebUtil.getCurrentBrandId(),WebUtil.getCurrentStoreId());
        return authUser.copyTo(AuthUserBo.class);
    }

    @Override
    public AuthUserBo getAuthUserBo(String userName, Long shopId){
        if(userName!=null){
            AuthUser authUser=authUserRepository.findByAccountAndShopIdenty(userName,shopId);
            if(authUser.getStatusFlag()!=1 || authUser.getEnabledFlag()!=1)
                return null;
            AuthUserBo authUserBo=authUser==null?null:authUser.copyTo(AuthUserBo.class);
            if(authUserBo!=null){
                authUserBo.setRoleBo(authRoleService.getAuthRole(authUserBo.getRoleId()));
            }
            return authUserBo;
        }
        return null;
    }

    @Override
    public Page<AuthUser> getUserPageByCriteria(Integer pageNum, Integer pageSize, AuthUserBo authUserBo) {
        Pageable pageable = new PageRequest(pageNum, pageSize, Sort.Direction.DESC, "id");
        Page<AuthUser> usersPage = authUserRepository.findAll(new Specification<AuthUser>() {
            @Override
            public Predicate toPredicate(Root<AuthUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (null != authUserBo.getRoleId()) {
                    list.add(criteriaBuilder.equal(root.get("roleId").as(Long.class), authUserBo.getRoleId()));
                }
                if (null != authUserBo.getJobEmployeeType()) {
                    list.add(criteriaBuilder.equal(root.get("jobEmployeeType").as(String.class), authUserBo.getJobEmployeeType()));
                }
                if (null != authUserBo.getName() && !"".equals(authUserBo.getName())) {
                    list.add(criteriaBuilder.like(root.get("name").as(String.class), authUserBo.getName()));
                }
                if (null != authUserBo.getEnabledFlag()){
                    list.add(criteriaBuilder.equal(root.get("enableFlag").as(Long.class),authUserBo.getEnabledFlag()));
                }

                list.add(criteriaBuilder.equal(root.get("statusFlag").as(Long.class), 1));
                list.add(criteriaBuilder.equal(root.get("brandIdenty").as(Long.class),WebUtil.getCurrentBrandId()));
                list.add(criteriaBuilder.equal(root.get("shopIdenty").as(Long.class),WebUtil.getCurrentStoreId()));

                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        }, pageable);
        return usersPage;
    }

    @Override
    public AuthUserBo getOneById(Long id) {
        AuthUser byIdAndStatusFlag = authUserRepository.findByIdAndStatusFlagAndBrandIdentyAndShopIdenty(id, 1, WebUtil.getCurrentBrandId(),WebUtil.getCurrentStoreId());
        if (byIdAndStatusFlag != null){
            AuthUserBo authUserBo = byIdAndStatusFlag.copyTo(AuthUserBo.class);
            return authUserBo;
        }
        return  null;
    }

    @Override
    public AuthUserBo getOneByIdAndShopIdentity(Long id, Long shopIdentity,Long brandIdentity){
        AuthUser byIdAndStatusFlag = authUserRepository.findByIdAndStatusFlagAndBrandIdentyAndShopIdenty(id, 1,brandIdentity,shopIdentity);
        if (byIdAndStatusFlag != null){
            AuthUserBo authUserBo = byIdAndStatusFlag.copyTo(AuthUserBo.class);
            return authUserBo;
        }
        return  null;
    }

    @Override
    public void delete(Long id) {
        authUserRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public void updateEnableFlag(Long id, Integer enableFlag) {
        authUserRepository.updateEnableById(id,enableFlag);
    }

}
