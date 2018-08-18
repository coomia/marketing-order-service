package com.meiye.system.auth;

import com.alibaba.fastjson.JSON;
import com.meiye.bo.system.JWTConfiguration;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.bo.user.UserBo;
import com.meiye.system.util.WebUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.BeanUtils;
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

    private  JWTConfiguration jwtConfiguration;
    public MeiYeAuthenticationFilter (JWTConfiguration jwtConfiguration){
        super();
        this.jwtConfiguration=jwtConfiguration;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        WebUtil.setRestResponseHeader(httpServletResponse);
        Authentication authentication=null;
        try {
            authentication = getAuthentication(httpServletRequest);
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }catch (ExpiredJwtException exp){
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpServletResponse.getWriter().print(JSON.toJSONString(ResetApiResult.error(null,"登录过期，请重新登录!"), WebUtil.getFastJsonSerializerFeature()));
        }catch (AuthenticationCredentialsNotFoundException exp){
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpServletResponse.getWriter().print(JSON.toJSONString(ResetApiResult.error(null,"登录信息不存在"), WebUtil.getFastJsonSerializerFeature()));
        } catch (Exception exp){
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpServletResponse.getWriter().print(JSON.toJSONString(ResetApiResult.error(null,"系统错误!"), WebUtil.getFastJsonSerializerFeature()));
        }
    }


    private Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(jwtConfiguration.getTokenInHeader());
        if(token==null)
            token=request.getParameter(jwtConfiguration.getTokenInHeader());

        if (token != null) {
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtConfiguration.getSecret())
                        .parseClaimsJws(token.replace(jwtConfiguration.getValidTokenStartWith(), ""))
                        .getBody();

                UserBo userBo=new UserBo();
                BeanUtils.copyProperties(((LinkedHashMap)claims.get("userBo")).get("principal"),userBo);
                List<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
                return userBo != null ?
                        new UsernamePasswordAuthenticationToken(userBo, null, userBo.getAuthorities()) :
                        null;
        }else{
            throw new AuthenticationCredentialsNotFoundException("登录信息不存在");
        }
    }
}
