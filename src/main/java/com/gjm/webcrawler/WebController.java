package com.gjm.webcrawler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping({"/", "/index", "/home"})
    public String home() {
        return "home";
    }
}
