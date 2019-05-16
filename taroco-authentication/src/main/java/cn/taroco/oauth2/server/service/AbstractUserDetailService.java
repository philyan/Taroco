package cn.taroco.oauth2.server.service;

import cn.taroco.common.vo.UserVO;
import cn.taroco.oauth2.server.userdetails.MyUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 抽象UserDetailsService 满足不同的登录方式
 *
 * @author liuht
 * 2019/5/14 11:36
 */
@Slf4j
public abstract class AbstractUserDetailService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserVO userVO = getUserVO(username);
        return new MyUserDetails(userVO);
    }

    /**
     * 获取 UserVO 对象
     *
     * @param username 用户名
     * @return
     */
    protected abstract UserVO getUserVO(String username);
}
