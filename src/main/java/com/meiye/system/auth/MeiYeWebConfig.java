package com.meiye.system.auth;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.meiye.bo.system.JWTConfiguration;
import com.meiye.service.store.StoreService;
import com.meiye.service.user.UserService;
import com.meiye.system.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.nio.charset.Charset;

/**
 * Created by Administrator on 2018/8/12 0012.
 */
@Configuration
@EnableWebSecurity
public class MeiYeWebConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    JWTConfiguration jwtConfiguration;
    @Autowired
    UserService userService;

    @Autowired
    StoreService storeService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new MeiYeAuthenticationProvider(userService));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭csrf验证
        http.csrf().disable()
                // 对请求进行认证
                .authorizeRequests()
                // 所有 /login 的POST请求 都放行
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.GET,"/verifyCode/**").permitAll()
                .antMatchers(HttpMethod.POST,"/pay/callback/**").permitAll()
                // 所有请求需要身份认证
                .anyRequest().authenticated()
                .and()
                // 添加一个过滤器 所有访问 /login 的请求交给 JWTLoginFilter 来处理 这个类处理所有的JWT相关内容
                .addFilterBefore(new MeiYeLoginFilter("/login", authenticationManager(),jwtConfiguration),
                        UsernamePasswordAuthenticationFilter.class)
                // 添加一个过滤器验证其他请求的Token是否合法
                .addFilterBefore(new MeiYeAuthenticationFilter(jwtConfiguration,userService,storeService),
                        UsernamePasswordAuthenticationFilter.class);
    }
}
