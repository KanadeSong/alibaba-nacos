package com.neo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName NacosProvide
 * @Description: TODO
 * @Author aaa
 * @Date 2019/10/16
 * @Version V1.0
 **/
@SpringBootApplication
@RestController
@EnableDiscoveryClient
public class NacosProvide {

    public static void main(String[] args) {
        SpringApplication.run(NacosProvide.class, args);
    }

    @GetMapping("/helloNacosa")
    public String helloNacos() {
        return "你好，Nacos！";
    }

}
