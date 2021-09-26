package com.example.restapiclient.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("comments")
@Data
public class CommentsRestClientConfig {
    private String baseUrl;
}
