package com.imooc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public Object hello(){
        return "hello world";
    }

    @GetMapping("/setSession")
    public Object setSession(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", "new User");
        session.setMaxInactiveInterval(3600);
        session.getAttribute("userInfo");
        session.removeAttribute("userInfo");
        return "ok";
    }

}
