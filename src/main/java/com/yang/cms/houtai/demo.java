package com.yang.cms.houtai;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class demo {
    @GetMapping("/hello")  //不是@RequestMapping
    public String hello(){
        System.out.println("11111111111");
        return "hello 黄老师"; //直接输出
    }
}
