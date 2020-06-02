package br.com.codenation.app.infrastructure.config;

import br.com.codenation.app.exception.FeignWrapperException;
import br.com.codenation.app.exception.ResponseReceiveWithException;
import feign.Contract;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.FormEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Slf4j
@Configuration
public class ClientConfig {

    @Bean
    public Contract feignContract() {
        return new Contract.Default();
    }

    @Bean
    public Encoder encoder() {
        return new FormEncoder();
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            try {
                log.error("Response to {}: {}", response.request().url(), response.body().asInputStream().toString());
                return new FeignWrapperException(response.status());
            } catch (IOException e) {
                log.error("IOException {} ", e.getMessage());
                throw new ResponseReceiveWithException(e);
            }
        };
    }

}