package cn.taroco.oauth2.server.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Home
 *
 * @author liuht
 * 2019/4/30 16:23
 */
@RestController
public class HomeController {

    /**
     * 根路径, 未从客户端跳转直接登陆会显示
     *
     * @return
     */
    @GetMapping("/")
    public ModelAndView index(Map<String, Object> model, Authentication authentication) {
        // 获取用户名
        model.put("userName", ((UserDetails)authentication.getPrincipal()).getUsername());
        return new ModelAndView("index", model);
    }
}
