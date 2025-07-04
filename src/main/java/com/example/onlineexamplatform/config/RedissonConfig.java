package com.example.onlineexamplatform.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${REDIS_NODES}")
    private String redisNodes;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();

        // 1번만 호출
        var clusterConfig = config.useClusterServers();

        // add all nodes
        String[] nodes = redisNodes.split(",");
        for (String node : nodes) {
            clusterConfig.addNodeAddress("redis://" + node.trim());
        }

        // 나머지 설정
        clusterConfig.setScanInterval(2000)
                .setIdleConnectionTimeout(10000)
                .setConnectTimeout(10000)
                .setTimeout(3000)
                .setRetryAttempts(3)
                .setRetryInterval(1500)
                .setFailedSlaveReconnectionInterval(3000)
                .setFailedSlaveCheckInterval(60000);

        return Redisson.create(config);
    }
}