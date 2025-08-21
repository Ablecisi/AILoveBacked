package com.ablecisi.ailovebacked.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.controller <br>
 * RootController.java <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/18
 * 星期三
 * 12:17
 **/
@RestController
@RequestMapping("/")
public class RootController {
    @GetMapping("/")
    public String home() {
        return "Welcome to AILoveBacked API Service";
    }
}
