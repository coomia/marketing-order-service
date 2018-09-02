package com.meiye.bo.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.ParentBo;
import com.meiye.bo.role.AuthUserBo;
import com.meiye.bo.store.CommercialBo;
import com.meiye.bo.store.StoreBo;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by Administrator on 2018/8/5 0005.
 */
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserBo extends ParentBo implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private Collection<SimpleGrantedAuthority> authorities;
    @JSONField(serialize=false)
    private boolean accountNonExpired;
    @JSONField(serialize=false)
    private boolean accountNonLocked;
    @JSONField(serialize=false)
    private boolean credentialsNonExpired;
    @JSONField(serialize=false)
    private boolean enabled;
    private CommercialBo storeBo=new CommercialBo();
    //Pos去请求专用
    private String requestMsgId="";
    private String deviceId="";

    @Override
    public String toString() {
        return "UserBo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", authorities=" + authorities +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", enabled=" + enabled +
                '}';
    }
}
