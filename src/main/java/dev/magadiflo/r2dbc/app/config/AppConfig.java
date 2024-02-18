package dev.magadiflo.r2dbc.app.config;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    /**
     * Este bean es para la clase GlobalExceptionHandler quien está heredando
     * de AbstractErrorWebExceptionHandler que requiere este bean.
     */
    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }
}
