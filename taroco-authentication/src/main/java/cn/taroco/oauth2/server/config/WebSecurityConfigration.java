package cn.taroco.oauth2.server.config;

import cn.taroco.common.config.TarocoOauth2Properties;
import cn.taroco.common.redis.template.TarocoRedisRepository;
import cn.taroco.oauth2.server.filter.MobileAuthenticationFilter;
import cn.taroco.oauth2.server.provider.MobileAuthenticationProvider;
import cn.taroco.oauth2.server.service.MobileUserDetailsService;
import cn.taroco.oauth2.server.service.UserNameUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * webSecurity 权限控制类
 *
 * @author liuht
 * @date 2018/7/24 15:58
 */
@EnableWebSecurity
public class WebSecurityConfigration extends WebSecurityConfigurerAdapter {

    @Autowired
    private TarocoOauth2Properties oauth2Properties;

    @Autowired
    private UserNameUserDetailsServiceImpl userNameUserDetailsService;

    @Autowired
    private MobileUserDetailsService mobileUserDetailsService;

    @Autowired
    private TarocoRedisRepository redisRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry =
                http
                        .addFilterAfter(mobileAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                        .formLogin()
                        .loginPage("/authentication/require").permitAll()
                        .loginProcessingUrl("/authentication/form")
                        .failureUrl("/authentication/require?error=true")
                        .and().logout().logoutUrl("/authentication/logout").permitAll().logoutSuccessUrl("/authentication/require?logout=true")
                        .and().authorizeRequests();

        final List<String> urlPermitAll = oauth2Properties.getUrlPermitAll();
        urlPermitAll.forEach(url -> registry.antMatchers(url).permitAll());
        registry.anyRequest().authenticated().and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(daoAuthenticationProvider())
                .authenticationProvider(mobileAuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**");
    }

    /**
     * 加密器 spring boot 2.x没有默认的加密器了
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 这一步的配置是必不可少的，否则SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 默认的用户名密码 AuthenticationProvider
     *
     * @return
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userNameUserDetailsService);
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    /**
     * 手机号登录过滤器
     */
    @Bean
    public MobileAuthenticationFilter mobileAuthenticationFilter() throws Exception {
        final MobileAuthenticationFilter filter = new MobileAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    /**
     * 手机号登录认证逻辑
     */
    @Bean
    public MobileAuthenticationProvider mobileAuthenticationProvider() {
        final MobileAuthenticationProvider provider = new MobileAuthenticationProvider();
        provider.setRedisRepository(redisRepository);
        provider.setUserDetailsService(mobileUserDetailsService);
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }
}
