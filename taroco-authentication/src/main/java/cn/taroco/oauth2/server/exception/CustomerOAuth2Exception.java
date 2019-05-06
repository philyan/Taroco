package cn.taroco.oauth2.server.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 自定义 OAuth2异常
 *
 * @author liuht
 * 2019/5/6 10:27
 */
@JsonSerialize(using = CustomerOAuth2ExceptionSerializer.class)
public class CustomerOAuth2Exception extends OAuth2Exception {

    private static final long serialVersionUID = -1003326583561699922L;

    public CustomerOAuth2Exception(final String msg, final Throwable t) {
        super(msg, t);
    }
}
