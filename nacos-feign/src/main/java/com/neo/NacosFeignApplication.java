package com.neo;

import com.neo.controller.RemoteClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName NacosFeignApplication
 * @Description: TODO
 * @Author aaa
 * @Date 2019/10/16
 * @Version V1.0
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@RestController
public class NacosFeignApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosFeignApplication.class, args);
    }

    @Autowired
    private RemoteClient remoteClient;

    @GetMapping("/feign")
    public String feign(){
        return remoteClient.helloNacos();
    }
}
