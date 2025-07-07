package com.example.onlineexamplatform.config.redisson;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "redisson")
@Getter
@Setter
public class RedissonCustomProperties {

    private ConfigProperties config = new ConfigProperties();

    @Getter
    @Setter
    public static class ConfigProperties {
        private SingleServerConfigProperties singleServerConfig = new SingleServerConfigProperties();
    }

    @Getter
    @Setter
    public static class SingleServerConfigProperties {
        private String address;
        private int timeout;
        private int connectionPoolSize;
        private int connectionMinimumIdleSize;
    }
}
