package com.hsuhau.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author hsuhau
 * @date 2020/7/18 16:12
 */
@Controller
public class MainController {
    @GetMapping("/hello")
    @ResponseBody
    public String toMain() {
        return "Hello Spring Boot!";
    }
}
