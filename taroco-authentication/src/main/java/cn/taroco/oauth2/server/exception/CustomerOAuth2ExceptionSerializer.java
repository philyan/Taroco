package cn.taroco.oauth2.server.exception;

import cn.taroco.common.exception.DefaultError;
import cn.taroco.common.web.Response;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * CustomerOAuth2Exception 序列化实现
 *
 * @author liuht
 * 2019/5/6 10:29
 */
public class CustomerOAuth2ExceptionSerializer extends StdSerializer<CustomerOAuth2Exception> {

    public CustomerOAuth2ExceptionSerializer() {
        super(CustomerOAuth2Exception.class);
    }

    @Override
    public void serialize(final CustomerOAuth2Exception e, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("errorCode", DefaultError.AUTHENTICATION_ERROR.getErrorCode());
        jsonGenerator.writeStringField("errorMessage", e.getOAuth2ErrorCode() + ":" + e.getMessage());
        jsonGenerator.writeStringField("status", Response.Status.FAILED.name());
        jsonGenerator.writeEndObject();
    }
}
