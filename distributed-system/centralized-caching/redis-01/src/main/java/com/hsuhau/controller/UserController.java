package com.hsuhau.controller;

import com.hsuhau.pojo.User;
import com.hsuhau.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/findUserById/{userId}")
    public User findUserById(@PathVariable String userId) {
        return userService.findUserById(userId);
    }

    @GetMapping("findUsernameById/{userId}")
    public String findUsernameById(@PathVariable String userId) {
        return userService.findUsernameById(userId);
    }
}