package com.example.demo.common.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/4 14:50
 */
@Controller
public class IndexController {

    @RequestMapping({"/index","/"})
    public String index() {
        return "index";
    }
    @RequestMapping("/login")
    public String login() {
        return "login";
    }
}
