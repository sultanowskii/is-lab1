package com.lab1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootWebController {
    @GetMapping("/")
    public String redirect() {
        return "redirect:index";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
