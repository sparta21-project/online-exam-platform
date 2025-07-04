package com.example.onlineexamplatform.config.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RedissonCustomProperties.class)
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(RedissonCustomProperties properties) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(properties.getConfig().getSingleServerConfig().getAddress())
                .setTimeout(properties.getConfig().getSingleServerConfig().getTimeout())
                .setConnectionPoolSize(properties.getConfig().getSingleServerConfig().getConnectionPoolSize())
                .setConnectionMinimumIdleSize(properties.getConfig().getSingleServerConfig().getConnectionMinimumIdleSize());

        return Redisson.create(config);
    }
}
