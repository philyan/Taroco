package cn.taroco.oauth2.server.filter;

import cn.taroco.common.constants.SecurityConstants;
import cn.taroco.common.exception.DefaultError;
import cn.taroco.common.utils.JsonUtils;
import cn.taroco.common.web.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

/**
 * 自定义AuthenticationFilter
 * 当我们的客户端信息不正确时服务端不会发送错误json信息而是让你重新登录，在一些app中是不能使用网页的，所以我们定义一个自己filter来处理客户端认证逻辑
 *
 * @author liuht
 * 2019/5/6 13:51
 * @see org.springframework.security.web.authentication.www.BasicAuthenticationFilter
 */
@Component
@Slf4j
public class CustomerAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!request.getRequestURI().equals(SecurityConstants.OAUTH_TOKEN_URL) ||
                !request.getParameter(SecurityConstants.GRANT_TYPE).equals(SecurityConstants.PASSWORD)) {
            filterChain.doFilter(request, response);
            return;
        }

        String[] clientDetails = this.isHasClientDetails(request);

        if (clientDetails == null) {
            log.warn("No clients info in request header");
            final Response resp = Response.failure(DefaultError.CLIENT_EXCEPTION);
            resp.setErrorMessage("No clients info in request header");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(JsonUtils.toJsonString(resp));
            return;
        }

        this.handle(request, response, clientDetails, filterChain);
    }

    private void handle(HttpServletRequest request, HttpServletResponse response, String[] clientDetails, FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            ClientDetails details = this.clientDetailsService.loadClientByClientId(clientDetails[0]);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(details.getClientId(), details.getClientSecret(), details.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(token);
            filterChain.doFilter(request, response);
        } catch (NoSuchClientException e) {
            log.warn("No client with requested id:" + clientDetails[0]);
            final Response resp = Response.failure(DefaultError.CLIENT_EXCEPTION);
            resp.setErrorMessage("no clients info in request header");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(JsonUtils.toJsonString(resp));
        }
    }

    /**
     * 判断请求头中是否包含client信息，不包含返回null
     * @param request
     * @return
     */
    private String[] isHasClientDetails(HttpServletRequest request) {

        String[] params = null;

        String header = request.getHeader(SecurityConstants.AUTHORIZATION);

        if (header != null) {

            String basic = header.substring(0, 5);

            if (basic.toLowerCase().contains(SecurityConstants.BASIC)) {

                String tmp = header.substring(6);
                String defaultClientDetails = new String(Base64.getDecoder().decode(tmp));

                String[] clientArrays = defaultClientDetails.split(":");

                if (clientArrays.length != 2) {
                    return null;
                } else {
                    params = clientArrays;
                }

            }
        }

        String id = request.getParameter(SecurityConstants.CLIENT_ID);
        String secret = request.getParameter(SecurityConstants.CLIENT_SECRET);

        if (header == null && id != null) {
            params = new String[] {id, secret};
        }

        return params;
    }

    public ClientDetailsService getClientDetailsService() {
        return clientDetailsService;
    }

    public void setClientDetailsService(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }
}
