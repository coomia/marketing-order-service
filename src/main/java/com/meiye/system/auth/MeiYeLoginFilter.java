package com.meiye.system.auth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meiye.bo.system.JWTConfiguration;
import com.meiye.bo.system.LoginBo;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.bo.user.UserBo;
import com.meiye.system.util.WebUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import sun.reflect.annotation.ExceptionProxy;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/8/12 0012.
 */
public class MeiYeLoginFilter extends AbstractAuthenticationProcessingFilter {

    JWTConfiguration jwtConfiguration;
    public MeiYeLoginFilter(String url, AuthenticationManager authenticationManager, JWTConfiguration jwtConfiguration){
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authenticationManager);
        this.jwtConfiguration=jwtConfiguration;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        WebUtil.setRestResponseHeader(httpServletResponse);
        LoginBo loginBo = new LoginBo();
//        if (httpServletRequest.getParameter("username") != null && httpServletRequest.getParameter("password") != null && httpServletRequest.getParameter("storeid") != null && httpServletRequest.getParameter("verifycode") != null) {
        if (httpServletRequest.getParameter("username") != null && httpServletRequest.getParameter("password") != null) {
            loginBo.setUserName(httpServletRequest.getParameter("username"));
            loginBo.setPassword(httpServletRequest.getParameter("password"));
            loginBo.setStoreId(httpServletRequest.getParameter("storeid"));
            loginBo.setVerifyCode(httpServletRequest.getParameter("verifycode"));
        } else {
            String loginBody = "";
            try {
                String line = "";
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpServletRequest.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    loginBody += line;
                }
                if (!StringUtils.isEmpty(loginBody)) {
                    loginBo = JSON.parseObject(loginBody, LoginBo.class, Feature.values());
                }
            } catch (Exception exp) {
                exp.printStackTrace();
                httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpServletResponse.getWriter().print(JSON.toJSONString(ResetApiResult.error(null, "用户名密码不存在!"), WebUtil.getFastJsonSerializerFeature()));
                return null;
            }
            if (StringUtils.isEmpty(loginBody) || StringUtils.isEmpty(loginBo.getUserName())) {
                try {
                    UserBo creds = new ObjectMapper().readValue(httpServletRequest.getInputStream(), UserBo.class);
                    loginBo.setUserName(creds.getUsername());
                    loginBo.setPassword(creds.getPassword());
                } catch (Exception exp) {
                    exp.printStackTrace();
                    httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    httpServletResponse.getWriter().print(JSON.toJSONString(ResetApiResult.error(null, "用户名密码不存在!"), WebUtil.getFastJsonSerializerFeature()));
                    return null;
                }
            }
        }
        // 返回一个验证令牌
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginBo.getUserName(),
                        loginBo.getPassword()
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        addAuthentication(response,authResult);
//        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(JSON.toJSONString(ResetApiResult.authFailed(null,failed.getMessage()),WebUtil.getFastJsonSerializerFeature()));
    }


    private void addAuthentication(HttpServletResponse response, Authentication authResult) {

        String JWT =jwtConfiguration.getValidTokenStartWith() + Jwts.builder()
                .claim("userBo", authResult)
                .setSubject(authResult.getName())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfiguration.getTimeOut()))
                .signWith(SignatureAlgorithm.HS512, jwtConfiguration.getSecret())
                .compact();
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader(jwtConfiguration.getTokenInHeader(),JWT);
            HashMap<String,Object> objectHashMap=new HashMap<String,Object>();
            objectHashMap.put("user",authResult.getPrincipal());
            objectHashMap.put("token",JWT);
            response.getWriter().println(JSON.toJSONString(ResetApiResult.sucess(objectHashMap), WebUtil.getFastJsonSerializerFeature()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
