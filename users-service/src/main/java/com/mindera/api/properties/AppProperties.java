package com.mindera.api.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.school")
public class AppProperties {
    private Redis redis;
    private Rabbit rabbit;

    @Getter
    @Setter
    public static class Redis {
        private Integer ttlInMillis = 30000;
    }

    @Getter
    @Setter
    public static class Rabbit {
        private String url;
        private Integer port;
        private String username;
        private String password;
        private Integer connectionTimeout = 100;
        private List<String> queues;
        private Integer maxConcurrentConsumer;
        private Integer retryInterval;
    }
}
