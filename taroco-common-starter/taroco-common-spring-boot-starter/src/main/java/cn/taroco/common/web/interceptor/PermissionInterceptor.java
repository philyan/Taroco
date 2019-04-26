package cn.taroco.common.web.interceptor;

import cn.taroco.common.constants.CommonConstant;
import cn.taroco.common.constants.PermissionConst;
import cn.taroco.common.constants.SecurityConstants;
import cn.taroco.common.exception.DefaultError;
import cn.taroco.common.utils.JsonUtils;
import cn.taroco.common.web.Response;
import cn.taroco.common.web.annotation.RequirePermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限验证拦截器
 *
 * @author liuht
 * 2019/4/24 12:39
 */
@Slf4j
public class PermissionInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            // 非Controller方法直接放行
            return true;
        }
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        final RequirePermission requirePermission = handlerMethod.getMethodAnnotation(RequirePermission.class);
        if (requirePermission == null) {
            return true;
        }

        final String username = request.getHeader(SecurityConstants.USER_HEADER);
        if (!StringUtils.isEmpty(username) && CommonConstant.ADMIN_USER_NAME.equals(username) && requirePermission.isAdminAccess()) {
            return true;
        }

        final String pers = request.getHeader(SecurityConstants.USER_PERMISSION_HEADER);
        if (StringUtils.isEmpty(pers)) {
            doNoPermission(request, response, "NONE");
            return false;
        }
        final String value = requirePermission.value();
        if (PermissionConst.ANY.equals(value)) {
            return true;
        }
        final String[] permissions = pers.split(",");
        for (final String per : permissions) {
            if (per.equals(value)) {
                return true;
            }
        }
        doNoPermission(request, response, value);
        return false;
    }

    /**
     * 无访问权限返回
     *
     * @param request
     * @param response
     * @param permission
     */
    private void doNoPermission(final HttpServletRequest request,
                                final HttpServletResponse response,
                                final String permission) throws IOException {
        final String uri = request.getRequestURI();
        log.warn("access denied: require permission:{} for url:{}", permission, uri);
        response.setCharacterEncoding(CommonConstant.UTF8);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        Response result = Response.failure(DefaultError.ACCESS_DENIED);
        result.setErrorMessage(String.format("access denied: require permission:%s for url:%s", permission, uri));
        response.getWriter().write(JsonUtils.toJsonString(result));
    }
}
