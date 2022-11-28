package net.catena_x.btp.libraries.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Configuration
public class ObjectMapperFactoryBtp {
    public static final String EXTENDED_OBJECT_MAPPER = "extended";

    @Bean(name = EXTENDED_OBJECT_MAPPER)
    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        SimpleModule timestampSerializerModule = new SimpleModule();
        timestampSerializerModule.addSerializer(Instant.class, new TimeStampSerializer());
        objectMapper.registerModule(timestampSerializerModule);

        SimpleModule timestampDeserializerModule = new SimpleModule();
        timestampDeserializerModule.addDeserializer(Instant.class, new TimeStampDeserializer());
        objectMapper.registerModule(timestampDeserializerModule);

        return objectMapper;
    }

    @Bean(name = EXTENDED_OBJECT_MAPPER)
    public static MappingJackson2HttpMessageConverter createMessageConverter() {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(createObjectMapper());
        return converter;
    }

    @Primary
    @Bean
    public static RestTemplate createRestTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, createMessageConverter());
        return restTemplate;
    }
}
