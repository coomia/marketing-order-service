package com.meiye.service.cacheDemo;

import com.meiye.bo.user.UserBo;

import java.util.List;

public interface CacheDemoService {

     void create(UserBo userBo);

     UserBo findById(Long id);

     List<UserBo> findAll();

    UserBo update(UserBo userBo);

    void delete(Long id);

}
