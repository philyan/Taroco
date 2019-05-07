package cn.taroco.gateway.zuul.handler;

import cn.taroco.common.exception.DefaultError;
import cn.taroco.common.utils.JsonUtils;
import cn.taroco.common.web.Response;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义token校验失败返回信息
 *
 * @author liuht
 * 2019/5/6 10:54
 */
@Component
public class CustomerExceptionEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) throws IOException, ServletException {
        final Response resp = Response.failure(DefaultError.AUTHENTICATION_ERROR);
        resp.setErrorMessage(authException.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(JsonUtils.toJsonString(resp));
    }
}
