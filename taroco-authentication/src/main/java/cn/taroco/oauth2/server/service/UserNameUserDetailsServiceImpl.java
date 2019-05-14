package cn.taroco.oauth2.server.service;

import cn.taroco.common.constants.CommonConstant;
import cn.taroco.common.vo.UserVO;
import cn.taroco.oauth2.server.feign.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户信息获取
 *
 * @author liuht
 * @date 2018/7/24 17:06
 */
@Service
public class UserNameUserDetailsServiceImpl extends AbstractUserDetailService {

    @Autowired
    private UserService userService;

    @Override
    protected UserVO getUserVO(final String username) {
        // 查询用户信息,包含角色列表
        UserVO userVo = userService.findUserByUsername(username);
        if (userVo == null) {
            throw new UsernameNotFoundException("用户名/密码错误");
        }
        if (CommonConstant.DEL_FLAG.equals(userVo.getDelFlag())) {
            throw new DisabledException("用户: " + username + " 不可用");
        }

        return userVo;
    }

}
