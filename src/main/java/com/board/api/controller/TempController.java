package com.board.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempController {
    @GetMapping("/")
    public String forward() {
        return "index";
    }
    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }
}
