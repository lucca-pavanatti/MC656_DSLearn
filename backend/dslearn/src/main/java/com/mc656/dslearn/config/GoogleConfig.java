package com.mc656.dslearn.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "google")
@Data
public class GoogleConfig {

    @Value("${google.clientId}")
    private String clientId;

}