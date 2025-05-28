package cz.skodaauto.ekkv.technologymanagement.features.sampleprintouts;

import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@ControllerAdvice
public class CreateSamplePrintoutRequestBodyAdvice implements RequestBodyAdvice {

    @Override
    public boolean supports(
            @NonNull MethodParameter methodParameter,
            Type targetType, Class<? extends HttpMessageConverter<?>> converterType
    ) {
        return targetType.getTypeName().endsWith("CreateSamplePrintoutSpecification");
    }

    @Override
    public HttpInputMessage beforeBodyRead(
            HttpInputMessage inputMessage,
            MethodParameter parameter,
            Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType
    ) throws IOException {
        var body = new String(inputMessage.getBody().readAllBytes(), StandardCharsets.UTF_8);

        var fixedBody = body.replaceAll("\"ticket-data\"", "\"ticketData\"");

        var newInputStream = new ByteArrayInputStream(fixedBody.getBytes(StandardCharsets.UTF_8));
        return new HttpInputMessage() {
            @Override
            public InputStream getBody() {
                return newInputStream;
            }

            @Override
            public org.springframework.http.HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }
        };
    }

    @Override
    public Object afterBodyRead(
            Object body,
            HttpInputMessage inputMessage,
            MethodParameter parameter,
            Type targetType, Class<? extends HttpMessageConverter<?>> converterType
    ) {
        return body;
    }

    @Override
    public Object handleEmptyBody(
            Object body,
            HttpInputMessage inputMessage,
            MethodParameter parameter, Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType
    ) {
        return body;
    }
}
