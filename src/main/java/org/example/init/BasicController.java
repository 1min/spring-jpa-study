package org.example.init;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.ZonedDateTime;

@Controller
public class BasicController {

    @GetMapping("/")
    @ResponseBody
    public String hello(){
        return "안녕하세요";
    }

    @GetMapping("/URL")
    public String url(){
        return "index.html";
    }

    @GetMapping("/date")
    @ResponseBody
    String date() {
        return ZonedDateTime.now().toString();
    }
}
