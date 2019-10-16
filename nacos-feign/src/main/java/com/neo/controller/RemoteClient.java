package com.neo.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName controller
 * @Description: TODO
 * @Author aaa
 * @Date 2019/10/16
 * @Version V1.0
 **/

@FeignClient(name = "nacos-provide", fallback = RemoteClientHystrix.class)
public interface RemoteClient {

    @GetMapping("/helloNacos")
    String helloNacos();

}
