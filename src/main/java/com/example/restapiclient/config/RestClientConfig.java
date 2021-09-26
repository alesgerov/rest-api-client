package com.example.restapiclient.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("apiclient")
@Data
public class RestClientConfig {
    private String commentsBaseUrl;
    private String todosBaseUrl;
}
