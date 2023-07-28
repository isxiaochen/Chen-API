package com.czq.apiinterface.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/school")
public class SchoolController {

    @PostMapping("/")
    public String postName(@RequestParam String schoolName){
        return "你的学校是"+schoolName;
    }


}
