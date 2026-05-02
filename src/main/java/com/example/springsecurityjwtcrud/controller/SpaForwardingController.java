package com.example.springsecurityjwtcrud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaForwardingController {

    @GetMapping({"/login", "/register", "/dashboard"})
    public String forwardClientRoutes() {
        return "forward:/index.html";
    }
}
