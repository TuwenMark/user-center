package com.dongdong.usercenter.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;

/**
 * LocalDateTime转换为时间戳的序列化配置
 * TODO :还不明白如何转换为时间戳
 *
 * @author Mr.Ye
 */
@Configuration
public class JacksonConfig implements Jackson2ObjectMapperBuilderCustomizer {
    @Override
    public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        jacksonObjectMapperBuilder.serializerByType(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE);
    }
}
