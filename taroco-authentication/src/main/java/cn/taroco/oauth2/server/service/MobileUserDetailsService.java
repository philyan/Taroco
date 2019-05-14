package cn.taroco.oauth2.server.service;

import cn.taroco.common.vo.UserVO;
import cn.taroco.oauth2.server.feign.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Service;

/**
 *
 * @author liuht
 * 2019/5/14 13:56
 */
@Service
public class MobileUserDetailsService extends AbstractUserDetailService {

    @Autowired
    private UserService userService;

    @Override
    protected UserVO getUserVO(final String username) {
        final UserVO userVO = userService.findUserByMobile(username);
        if (userVO == null) {
            throw new InternalAuthenticationServiceException("Mobile no: " + username + ", is not exist");
        }
        return userVO;
    }
}
