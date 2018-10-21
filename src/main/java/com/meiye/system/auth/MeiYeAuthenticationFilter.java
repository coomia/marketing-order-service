package com.meiye.system.auth;

import com.alibaba.fastjson.JSON;
import com.meiye.bo.system.JWTConfiguration;
import com.meiye.bo.system.PosApiResult;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.bo.user.UserBo;
import com.meiye.exception.BusinessException;
import com.meiye.service.store.StoreService;
import com.meiye.service.user.UserService;
import com.meiye.system.util.WebUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/8/12 0012.
 */
public class MeiYeAuthenticationFilter extends OncePerRequestFilter {
    UserService userService;

    StoreService storeService;
    Logger logger= LoggerFactory.getLogger(MeiYeAuthenticationFilter.class);


    private  JWTConfiguration jwtConfiguration;
    public MeiYeAuthenticationFilter (JWTConfiguration jwtConfiguration,UserService userService,StoreService storeService){
        super();
        this.jwtConfiguration=jwtConfiguration;
        this.userService=userService;
        this.storeService=storeService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        WebUtil.setRestResponseHeader(httpServletResponse);
        Authentication authentication=null;
        try {
            authentication = getAuthentication(httpServletRequest,httpServletResponse);
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }catch (ExpiredJwtException exp){
            logger.error(exp.getMessage(),exp);
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpServletResponse.getWriter().print(JSON.toJSONString(ResetApiResult.error(null,"登录过期，请重新登录!"), WebUtil.getFastJsonSerializerFeature()));
        }catch (AuthenticationCredentialsNotFoundException exp){
            logger.error(exp.getMessage(),exp);
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpServletResponse.getWriter().print(JSON.toJSONString(ResetApiResult.error(null,"登录信息不存在"), WebUtil.getFastJsonSerializerFeature()));
        }catch (BusinessException exp){
            logger.error(exp.getMessage(),exp);
            if(WebUtil.isMsApiPath(httpServletRequest)) {
                httpServletResponse.getWriter().print(JSON.toJSONString(ResetApiResult.error(null,exp.getMessage()), WebUtil.getFastJsonSerializerFeature()));
            }else if(WebUtil.isPosApiPath(httpServletRequest)) {
                httpServletResponse.getWriter().print(JSON.toJSONString(PosApiResult.error(null,exp.getMessage()), WebUtil.getFastJsonSerializerFeature()));
            }else if(WebUtil.isWechatApiPath(httpServletRequest)) {
                httpServletResponse.getWriter().print(JSON.toJSONString(PosApiResult.error(null,exp.getMessage()), WebUtil.getFastJsonSerializerFeature()));
            }else{
                httpServletResponse.getWriter().print(JSON.toJSONString(ResetApiResult.error(null,exp.getMessage()), WebUtil.getFastJsonSerializerFeature()));
            }
        } catch (Exception exp){
            logger.error(exp.getMessage(),exp);
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpServletResponse.getWriter().print(JSON.toJSONString(ResetApiResult.error(null,"系统错误!"), WebUtil.getFastJsonSerializerFeature()));
        }
    }


    private Authentication getAuthentication(HttpServletRequest request,HttpServletResponse httpServletResponse) {
        if(WebUtil.isMsApiPath(request)) {
            String token = request.getHeader(jwtConfiguration.getTokenInHeader());
            if (token == null)
                token = request.getParameter(jwtConfiguration.getTokenInHeader());

            if (token != null) {
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtConfiguration.getSecret())
                        .parseClaimsJws(token.replace(jwtConfiguration.getValidTokenStartWith(), ""))
                        .getBody();

                UserBo userBo = new UserBo();
                String userBoJson = JSON.toJSONString(((LinkedHashMap) claims.get("userBo")).get("principal"));
                userBo = JSON.parseObject(userBoJson, UserBo.class);

                List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
                Authentication authentication= userBo != null ?
                        new UsernamePasswordAuthenticationToken(userBo, null, userBo.getAuthorities()) :
                        null;
                //重置token
                if(authentication!=null){
                    String newJwtString=WebUtil.getJwtString(jwtConfiguration,authentication);
                    httpServletResponse.setHeader(jwtConfiguration.getTokenInHeader(),newJwtString);
                }
                return authentication;
            } else {
                throw new AuthenticationCredentialsNotFoundException("登录信息不存在");
            }
        }else if(WebUtil.isPosApiPath(request)){
            try {
                UserBo userBo = new UserBo();
                String msgId = request.getHeader(WebUtil.getPosRequestHeaderPrefix() + "-api-msgid");
                String deviceId = request.getHeader(WebUtil.getPosRequestHeaderPrefix() + "-api-device-id");
                Long brandId = Long.parseLong(request.getHeader(WebUtil.getPosRequestHeaderPrefix() + "-api-brand-id"));
                Long shopId = Long.parseLong(request.getHeader(WebUtil.getPosRequestHeaderPrefix() + "-api-shop-id"));
                userBo.setStoreBo(storeService.findStoreById(shopId));
                userBo.setRequestMsgId(msgId);
                userBo.setDeviceId(deviceId);
                return new UsernamePasswordAuthenticationToken(userBo, null,null);
            }catch (Exception exp){
                throw new BusinessException("参数错误");
            }
        }else if(WebUtil.isWechatApiPath(request)){
            try {
                UserBo userBo = new UserBo();
                String msgId = request.getHeader(WebUtil.getPosRequestHeaderPrefix() + "-api-msgid");
                String deviceId = request.getHeader(WebUtil.getPosRequestHeaderPrefix() + "-api-device-id");
                Long brandId = Long.parseLong(request.getHeader(WebUtil.getPosRequestHeaderPrefix() + "-api-brand-id"));
                Long shopId = Long.parseLong(request.getHeader(WebUtil.getPosRequestHeaderPrefix() + "-api-shop-id"));
                userBo.setStoreBo(storeService.findStoreById(shopId));
                userBo.setRequestMsgId(msgId);
                userBo.setDeviceId(deviceId);
                return new UsernamePasswordAuthenticationToken(userBo, null,null);
            }catch (Exception exp){
                throw new BusinessException("参数错误");
            }
        }
        return null;
    }
}
