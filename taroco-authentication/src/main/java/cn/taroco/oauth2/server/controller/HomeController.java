package cn.taroco.oauth2.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Home
 *
 * @author liuht
 * 2019/4/30 16:23
 */
@RestController
public class HomeController {

    /**
     * 根路径
     *
     * @return
     */
    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("ftl/index");
    }
}
