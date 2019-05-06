package cn.taroco.oauth2.server.exception;

import cn.taroco.common.exception.DefaultError;
import cn.taroco.common.utils.JsonUtils;
import cn.taroco.common.web.Response;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义授权失败返回信息
 *
 * @author liuht
 * 2019/5/6 11:02
 */
@Component
public class CustomerAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response, final AccessDeniedException accessDeniedException) throws IOException, ServletException {
        final Response resp = Response.failure(DefaultError.ACCESS_DENIED);
        resp.setErrorMessage(accessDeniedException.getMessage());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(JsonUtils.toJsonString(resp));
    }
}
