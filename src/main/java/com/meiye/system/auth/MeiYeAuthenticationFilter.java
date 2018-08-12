package com.meiye.system.auth;

import com.alibaba.fastjson.JSON;
import com.meiye.bo.system.JWTConfiguration;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.bo.user.UserBo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
        Authentication authentication=null;
        try {
            authentication = getAuthentication(httpServletRequest);
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }catch (ExpiredJwtException exp){
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            exp.printStackTrace();
            httpServletResponse.getWriter().print(ResetApiResult.authFailed(null,exp.getMessage()));
        }catch (Exception exp){
            exp.printStackTrace();
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpServletResponse.getWriter().print(ResetApiResult.error(null,exp.getMessage()));
        }
    }


    private Authentication getAuthentication(HttpServletRequest request) {
        // 从Header中拿到token
        String token = request.getHeader(jwtConfiguration.getTokenInHeader());

        if (token != null) {
                // 解析 Token
                Claims claims = Jwts.parser()
                        // 验签
                        .setSigningKey(jwtConfiguration.getSecret())
                        // 去掉 Bearer
                        .parseClaimsJws(token.replace(jwtConfiguration.getValidTokenStartWith(), ""))
                        .getBody();

                // 拿用户名
                UserBo userBo=new UserBo();
                BeanUtils.copyProperties(((LinkedHashMap)claims.get("userBo")).get("principal"),userBo);
                List<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();

                // 返回验证令牌
                return userBo != null ?
                        new UsernamePasswordAuthenticationToken(userBo, null, userBo.getAuthorities()) :
                        null;
        }
        return null;
    }
}
