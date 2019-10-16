package com.neo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName NacosConfigApplication
 * @Description: TODO
 * @Author aaa
 * @Date 2019/10/16
 * @Version V1.0
 **/
@SpringBootApplication
@EnableDiscoveryClient
@RestController
@RefreshScope //可以使当前类下的配置支持动态更新
public class NacosConfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosConfigApplication.class, args);
    }

    @Value("${nacos.config}")
    private String nacosConfig;

    @GetMapping("/getConfig")
    public String getConfig() {
        return nacosConfig;
    }
}
