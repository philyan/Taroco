package cn.taroco.oauth2.server.extend.mobile;

import cn.taroco.common.vo.UserVO;
import cn.taroco.oauth2.server.feign.UserService;
import cn.taroco.oauth2.server.userdetails.MyUserDetails;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 定义手机号登录校验逻辑
 *
 * @author liuht
 * 2019/5/13 15:25
 */
public class MobileAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final MobileAuthenticationToken authenticationToken = (MobileAuthenticationToken) authentication;
        final UserVO userVO = userService.findUserByMobile((String) authenticationToken.getPrincipal());
        if (userVO == null) {
            throw new InternalAuthenticationServiceException("Mobile no: " + authenticationToken.getPrincipal() + ", is not exist");
        }
        final MyUserDetails userDetails = new MyUserDetails(userVO);
        final MobileAuthenticationToken mobileAuthenticationToken = new MobileAuthenticationToken(userDetails, userDetails.getAuthorities());
        mobileAuthenticationToken.setDetails(authenticationToken.getDetails());
        return mobileAuthenticationToken;
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }
}
