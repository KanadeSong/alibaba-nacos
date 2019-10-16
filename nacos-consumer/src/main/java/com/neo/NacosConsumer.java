package com.neo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.print.attribute.standard.MediaSize;

/**
 * @ClassName NacosConsumer
 * @Description: TODO
 * @Author aaa
 * @Date 2019/10/16
 * @Version V1.0
 **/
@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class NacosConsumer {
    public static void main(String[] args) {
        SpringApplication.run(NacosConsumer.class, args);
    }

    @Autowired
    private RestTemplate restTemplate;

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @GetMapping("/consumer")
    public String consumer(){
        String result = restTemplate.getForObject("http://nacos-provide/helloNacos",String.class);
        return "Return: "+result;
    }
}
