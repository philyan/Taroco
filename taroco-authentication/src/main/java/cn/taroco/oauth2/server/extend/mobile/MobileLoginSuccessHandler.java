package cn.taroco.oauth2.server.extend.mobile;

import cn.taroco.common.constants.SecurityConstants;
import cn.taroco.common.utils.JsonUtils;
import com.xiaoleilu.hutool.codec.Base64;
import com.xiaoleilu.hutool.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 手机号登录成功处理
 *
 * @author liuht
 * 2019/5/13 15:52
 */
@Component
@Slf4j
public class MobileLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String header = request.getHeader(SecurityConstants.AUTHORIZATION);

        if (header == null || !header.startsWith(SecurityConstants.BASIC_HEADER)) {
            throw new UnapprovedClientAuthenticationException("请求头中client信息为空");
        }

        try {
            final String[] tokens = extractAndDecodeHeader(header);
            assert tokens.length == 2;
            final String clientId = tokens[0];

            final ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
            final TokenRequest tokenRequest = new TokenRequest(MapUtil.newHashMap(), clientId, clientDetails.getScope(), "mobile");
            final OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

            final OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
            final OAuth2AccessToken oAuth2AccessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
            log.info("手机号登录成功, accessToken:{}", oAuth2AccessToken.getValue());
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            final PrintWriter printWriter = response.getWriter();
            printWriter.append(JsonUtils.toJsonString(oAuth2AccessToken));
        } catch (IOException e) {
            throw new BadCredentialsException(
                    "Failed to decode basic authentication token");
        }
    }

    private String[] extractAndDecodeHeader(final String header) throws IOException {

        final byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(
                    "Failed to decode basic authentication token");
        }

        final String token = new String(decoded, StandardCharsets.UTF_8);

        final int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[] {token.substring(0, delim), token.substring(delim + 1)};
    }
}
