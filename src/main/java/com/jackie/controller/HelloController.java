package com.jackie.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("h")
public class HelloController {

    @RequestMapping("/hello")
    public String sayHello() {
        return "hello! joker-chat";
    }

    @RequestMapping("/bye")
    private String sayGoodBye() {
        return null;
    }
}
