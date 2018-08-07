package com.meiye.bo.user;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * Created by Administrator on 2018/8/5 0005.
 */
@Data
public class UserBo implements UserDetails {
    private Long id;
    private Long updateId;
    private Date updateDatetime;
    private Long deleteId;
    private Date deleteDateTime;
    private Long entryId;
    private Date entryDatetime;
    private Long version;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
