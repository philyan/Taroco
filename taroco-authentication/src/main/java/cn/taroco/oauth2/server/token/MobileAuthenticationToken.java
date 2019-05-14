package cn.taroco.oauth2.server.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 定义手机号登录令牌
 *
 * @author liuht
 * 2019/5/13 15:20
 * @see UsernamePasswordAuthenticationToken
 */
public class MobileAuthenticationToken extends MyAuthenticationToken {

    private static final long serialVersionUID = -9192173797915966518L;

    public MobileAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public MobileAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
