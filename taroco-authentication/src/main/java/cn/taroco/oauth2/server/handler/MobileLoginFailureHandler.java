package cn.taroco.oauth2.server.handler;

import cn.taroco.common.exception.DefaultError;
import cn.taroco.common.utils.JsonUtils;
import cn.taroco.common.web.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 手机登录失败返回
 *
 * @author liuht
 * 2019/5/16 9:37
 */
@Component
@Slf4j
public class MobileLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final AuthenticationException exception) throws IOException, ServletException {
        final Response resp = Response.failure(DefaultError.AUTHENTICATION_ERROR);
        resp.setErrorMessage(exception.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(JsonUtils.toJsonString(resp));
    }
}
