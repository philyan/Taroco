package cn.taroco.oauth2.server.config;

import cn.taroco.common.constants.SecurityConstants;
import cn.taroco.oauth2.server.userdetails.MyUserDetails;
import com.xiaoleilu.hutool.collection.CollectionUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 自定义token的转换
 * 默认使用DefaultAccessTokenConverter 这个装换器
 * DefaultAccessTokenConverter有个UserAuthenticationConverter，这个转换器作用是把用户的信息放入token中，
 * 默认只是放入username, 重写这个方法放入额外的信息
 *
 * @author liuht
 * 2019/4/25 10:44
 */
public class CustomerAccessTokenConverter extends DefaultAccessTokenConverter {

    public CustomerAccessTokenConverter() {
        super.setUserTokenConverter(new CustomerUserAuthenticationConverter());
    }

    private class CustomerUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

        @Override
        public Map<String, ?> convertUserAuthentication(Authentication authentication) {
            final Map<String, Object> response = new LinkedHashMap<>();
            response.put(USERNAME, authentication.getName());
            response.put(SecurityConstants.USER_HEADER, authentication.getName());
            if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
                response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
                response.put(SecurityConstants.USER_ROLE_HEADER, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
            }
            if (authentication.getPrincipal() instanceof MyUserDetails) {
                final MyUserDetails user = (MyUserDetails) authentication.getPrincipal();
                response.put(SecurityConstants.HEADER_LABEL, user.getLabel());
                response.put(SecurityConstants.USER_PERMISSION_HEADER, CollectionUtil.join(user.getPermissions(), ","));
            }
            return response;
        }
    }
}
