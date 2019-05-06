package cn.taroco.oauth2.server.config;

import cn.taroco.common.config.TarocoOauth2Properties;
import cn.taroco.common.constants.SecurityConstants;
import cn.taroco.oauth2.server.exception.CustomerAccessDeniedHandler;
import cn.taroco.oauth2.server.exception.CustomerExceptionEntryPoint;
import cn.taroco.oauth2.server.exception.CustomerWebResponseExceptionTranslator;
import cn.taroco.oauth2.server.filter.CustomerAuthenticationFilter;
import cn.taroco.oauth2.server.userdetails.MyUserDetails;
import com.xiaoleilu.hutool.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.Map;

/**
 * 认证服务器配置抽象
 *
 * @author liuht
 * @date 2018/7/24 16:10
 */
@Configuration
@EnableConfigurationProperties( {TarocoOauth2Properties.class})
@EnableAuthorizationServer
public class AuthorizationServerConfigration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TarocoOauth2Properties oauth2Properties;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomerWebResponseExceptionTranslator exceptionTranslator;

    @Autowired
    private CustomerExceptionEntryPoint exceptionEntryPoint;

    @Autowired
    private CustomerAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CustomerAuthenticationFilter customerAuthenticationFilter;

    @Autowired
    private DataSource dataSource;

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * JWT Token 生成转换器（加密方式以及加密的Token中存放哪些信息）
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory
                (oauth2Properties.getKeyStore().getLocation(), oauth2Properties.getKeyStore().getSecret().toCharArray())
                .getKeyPair(oauth2Properties.getKeyStore().getAlias());
        converter.setKeyPair(keyPair);
        converter.setAccessTokenConverter(new CustomerAccessTokenConverter());
        return converter;
    }

    /**
     * jwt 生成token 定制化处理
     * <p>
     * 额外信息（这部分信息不关乎加密方式）, 添加到随token一起的additionalInformation当中
     *
     * @return TokenEnhancer
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            final Map<String, Object> additionalInfo = accessToken.getAdditionalInformation();
            final Object principal = authentication.getUserAuthentication().getPrincipal();
            MyUserDetails user;
            if (principal instanceof MyUserDetails) {
                user = (MyUserDetails) principal;
            } else {
                final String username = (String) principal;
                user = (MyUserDetails) userDetailsService.loadUserByUsername(username);
            }
            additionalInfo.put(SecurityConstants.LICENSE_KEY, SecurityConstants.LICENSE);
            additionalInfo.put(SecurityConstants.USER_HEADER, user.getUsername());
            additionalInfo.put(SecurityConstants.HEADER_LABEL, user.getLabel());
            additionalInfo.put(SecurityConstants.USER_ROLE_HEADER, CollectionUtil.join(user.getAuthorities(), ","));
            additionalInfo.put(SecurityConstants.USER_PERMISSION_HEADER, CollectionUtil.join(user.getPermissions(), ","));
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }

    @Bean
    public ClientDetailsService clientDetailsService() {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
        clientDetailsService.setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);
        return clientDetailsService;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService());
    }

    /**
     * 授权服务器端点配置，如令牌存储，令牌自定义，用户批准和授权类型，不包括端点安全配置
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setAccessTokenValiditySeconds(oauth2Properties.getAccessTokenValiditySeconds());
        defaultTokenServices.setRefreshTokenValiditySeconds(oauth2Properties.getRefreshTokenValiditySeconds());

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter(), tokenEnhancer()));
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);

        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenServices(defaultTokenServices)
                .exceptionTranslator(exceptionTranslator);
    }

    /**
     * 授权服务器端点的安全配置
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .tokenKeyAccess("isAuthenticated()")
                .checkTokenAccess("permitAll()")
                .authenticationEntryPoint(exceptionEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .addTokenEndpointAuthenticationFilter(customerAuthenticationFilter);
    }
}
