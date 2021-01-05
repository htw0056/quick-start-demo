package com.htw.signature.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * Created by htw on 2020/12/2.
 */
@RestController
@RequestMapping("/")
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/")
    public String hello(@RequestParam String s) {
        LOGGER.info(s);
        return "hello";
    }

    @RequestMapping("/aaa")
    public String post(@RequestBody(required = false) String body, @RequestParam(required = false) String s,@RequestParam(required = false) String a,@RequestParam(required = false) String test) {
        LOGGER.info(body);
        LOGGER.info(s);
        LOGGER.info(a);

        return "post";
    }

}
