//package com.reqmaster.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class HomeController {
//
//    @GetMapping("/")
//    public String home() {
//        return "redirect:/reqmaster/";
//    }
//
//    @GetMapping("/reqmaster")
//    public String reqmasterHome() {
//        // 直接返回静态页面，不进行重定向
//        return "forward:/index.html";
//    }
//}