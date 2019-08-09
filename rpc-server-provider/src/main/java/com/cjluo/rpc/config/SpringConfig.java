package com.cjluo.rpc.config;

import com.cjluo.rpc.remote.nio.PublishService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author cj.luo
 * @date 2019/4/10
 */
@Configuration
@ComponentScan("com.cjluo.rpc.remote")
public class SpringConfig {

    /**
     * 默认端口8080
     */
    private static final int PORT = 8080;

    @Bean
    public PublishService getPublishService() {
        return new PublishService(PORT);
    }
}
