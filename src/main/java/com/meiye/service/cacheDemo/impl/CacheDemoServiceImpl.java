package com.meiye.service.cacheDemo.impl;

import com.meiye.bo.user.UserBo;
import com.meiye.service.cacheDemo.CacheDemoService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring缓存注解@Cacheable、@CacheEvict、@CachePut使用
 * https://www.cnblogs.com/fashflying/p/6908028.html
 */


@Service
@Transactional
public class CacheDemoServiceImpl implements CacheDemoService {

    //data用来模拟做userBo数据库数据的一个map
    Map<Long,UserBo> data = new HashMap<>();


    /**
     * 缓存的key
     */
    public static final String THING_ALL_KEY   = "\"userBO_all\"";

    /**
     * value属性表示使用哪个缓存策略，缓存策略在ehcache.xml
     */
    public static final String DEMO_CACHE_NAME = "demo";

    // @CacheEvict是用来标注在需要清除缓存元素的方法或类上的,即value表示清除操作是发生在哪些Cache上的（对应Cache的名称）；key表示需要清除的是哪个key
    @CacheEvict(value = DEMO_CACHE_NAME,key = THING_ALL_KEY)
    @Override
    public void create(UserBo userBo) {
        data.put (userBo.getId(), userBo);
    }

    //@Cacheable 当标记在一个方法上时表示该方法是支持缓存的,value属性是必须指定的，其表示当前方法的返回值是会被缓存在哪个Cache上的,key属性是用来指定Spring缓存方法的返回结果时对应的key的
    @Cacheable(value = DEMO_CACHE_NAME,key = "#id")
    @Override
    public UserBo findById(Long id) {
        System.err.println ("没有走缓存！" + id);
        return data.get (id);
    }

    @Cacheable(value = DEMO_CACHE_NAME,key = THING_ALL_KEY)
    @Override
    public List<UserBo> findAll() {
        System.err.println ("没有走缓存！");
        return new ArrayList(data.values ());
    }

    //  @CachePut每次都会执行方法，并将结果存入指定的缓存中 .与@Cacheable不同的是使用@CachePut标注的方法在执行前不会去检查缓存中是否存在之前执行过的结果，而是每次都会执行该方法，并将执行结果以键值对的形式存入指定的缓存中。
    @CachePut(value = DEMO_CACHE_NAME,key = "#userBo.getId()+'userBo'")
    @CacheEvict(value = DEMO_CACHE_NAME,key = THING_ALL_KEY)
    @Override
    public UserBo update(UserBo userBo) {
        System.out.println (userBo.toString());
        data.put (userBo.getId (), userBo);
        return userBo;
    }

    //  @@CacheEvict是用来标注在需要清除缓存元素的方法.即value表示清除操作是发生在哪些Cache上的（对应Cache的名称）；key表示需要清除的是哪个key
    // allEntries是boolean类型，表示是否需要清除缓存中的所有元素。默认为false，表示不需要
    @CacheEvict(value = DEMO_CACHE_NAME, allEntries=true)
    @Override
    public void delete(Long id) {
        data.remove (id);
    }


}
