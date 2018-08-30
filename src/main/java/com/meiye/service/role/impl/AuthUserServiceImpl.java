package com.meiye.service.role.impl;

import com.meiye.bo.role.AuthRoleBo;
import com.meiye.bo.role.AuthUserBo;
import com.meiye.bo.user.UserBo;
import com.meiye.model.part.DishShop;
import com.meiye.model.role.AuthUser;
import com.meiye.repository.role.AuthUserRepository;
import com.meiye.service.role.AuthRoleService;
import com.meiye.service.role.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
        authUserRepository.save(authUser);
    }

    @Override
    public AuthUserBo findLatestAuthUser() {
        AuthUser authUser = authUserRepository.findFirstByOrderByServerCreateTimeDesc();
        return authUser.copyTo(AuthUserBo.class);
    }

    @Override
    public AuthUserBo getAuthUserBo(String userName){
        if(userName!=null){
            AuthUser authUser=authUserRepository.findFirstByAccount(userName);
            AuthUserBo authUserBo=authUser==null?null:authUser.copyTo(AuthUserBo.class);
            if(authUserBo!=null){
                authUserBo.setRoleBo(authRoleService.findOneById(authUserBo.getRoleId()));
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

                list.add(criteriaBuilder.equal(root.get("statusFlag").as(Long.class), 1));
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        }, pageable);
        return usersPage;
    }

    @Override
    public AuthUserBo getOneById(Long id) {
        AuthUser byIdAndStatusFlag = authUserRepository.findByIdAndStatusFlag(id, 1);
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

}
