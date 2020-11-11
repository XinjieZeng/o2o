package com.example.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/o2o/frontend")
public class FrontendController {
    @GetMapping("/index")
    private String index() {
        return "frontend/index";
    }

}
