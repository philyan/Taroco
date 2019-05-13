package cn.taroco.oauth2.server.extend.mobile;

import cn.taroco.oauth2.server.feign.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 扩展手机登录配置
 *
 * @author liuht
 * 2019/5/13 16:19
 */
@Component
public class MobileSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private MobileLoginSuccessHandler mobileLoginSuccessHandler;

    @Autowired
    private UserService userService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        final MobileAuthenticationFilter mobileAuthenticationFilter = new MobileAuthenticationFilter();
        mobileAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        mobileAuthenticationFilter.setAuthenticationSuccessHandler(mobileLoginSuccessHandler);

        final MobileAuthenticationProvider mobileAuthenticationProvider = new MobileAuthenticationProvider();
        mobileAuthenticationProvider.setUserService(userService);
        http.authenticationProvider(mobileAuthenticationProvider)
                .addFilterAfter(mobileAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
