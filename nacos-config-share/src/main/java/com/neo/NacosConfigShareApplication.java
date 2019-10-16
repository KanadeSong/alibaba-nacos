package com.neo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName NacosConfigShareApplication
 * @Description: TODO
 * @Author aaa
 * @Date 2019/10/16
 * @Version V1.0
 **/
@SpringBootApplication
@EnableDiscoveryClient
@RestController
@RefreshScope
public class NacosConfigShareApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosConfigShareApplication.class, args);
    }

    @Value("${nacos.share}")
    private String share;

    @Value("${share.config1}")
    private String shareConfig1;

    @Value("${share.config2}")
    private String shareConfig2;

    @Value("${share.config3}")
    private String shareConfig3;

    @Value("${share.config4}")
    private String shareConfig4;

    @GetMapping("/getConfig")
    public String getConfig() {
        return share;
    }

    @GetMapping("/getShare1")
    public String getShareConfig1() {
        return shareConfig1;
    }

    @GetMapping("/getShare2")
    public String getShareConfig2() {
        return shareConfig2;
    }

    @GetMapping("/getShare3")
    public String getShareConfig3() {
        return shareConfig3;
    }

    @GetMapping("/getShare4")
    public String getShareConfig4() {
        return shareConfig4;
    }
}
