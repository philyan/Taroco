package cn.taroco.oauth2.server.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

/**
 * 自定义授权界面
 *
 * @author liuht
 * 2019/5/17 9:56
 * @see WhitelabelApprovalEndpoint
 * @see AuthorizationEndpoint
 */
@Controller
@SessionAttributes("authorizationRequest")
public class ConfirmAccessController {
    /**
     * 授权页面
     * @param model
     * @return
     */
    @RequestMapping("/oauth/confirm_access")
    public ModelAndView authorizePage(Map<String, Object> model, HttpServletRequest request) {
        // 获取用户名
        String userName = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();
        model.put("userName", userName);
        @SuppressWarnings("unchecked")
        Map<String, String> scopes = (Map<String, String>) (model.containsKey("scopes") ? model.get("scopes") : request
                .getAttribute("scopes"));
        model.put("scopes", new ArrayList<>(scopes.keySet()));
        return new ModelAndView("authorize", model);
    }
}
