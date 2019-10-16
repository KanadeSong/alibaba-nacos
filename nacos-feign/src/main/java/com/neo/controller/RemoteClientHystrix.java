package com.neo.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

/**
 * @ClassName RemoteClientHystrix
 * @Description: TODO
 * @Author aaa
 * @Date 2019/10/16
 * @Version V1.0
 **/
@Component
public class RemoteClientHystrix implements RemoteClient {
    @Override
    public String helloNacos() {
        return "请求超时了";
    }
}
