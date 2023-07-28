package com.czq.apiinterface.controller;


import com.czq.apiclientsdk.model.User;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/get")
    public String getName(String name,HttpServletRequest request){
        return "你的名字是"+name;
    }

    @PostMapping("/post")
    public String postName(@RequestParam(required = false) String name){
        return "你的名字是"+name;
    }

    @PostMapping("/user")
    public String jsonName(@RequestBody User user, HttpServletRequest request){
        return "你的名字是"+user.getName();
    }
}
