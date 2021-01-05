package com.htw.signature.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by htw on 2020/12/2.
 */
@RestController
@RequestMapping("/test")
public class SignatureController {

    public static class Test{
        private String a;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(SignatureController.class);

    @RequestMapping("/get")
    public String hello(@RequestParam String a, @RequestParam String b,
//                        @RequestBody(required = false) String body
                        @RequestParam(required = false) String xx
    ) {
        LOGGER.info("a: {},b: {},body: {}", a, b,xx);
        return "hello";
    }

    @PostMapping(value = "/post")
    public Object post(@RequestParam String a, @RequestBody Test body, HttpServletRequest request) throws IOException {
        LOGGER.info(a);
//        LOGGER.info(body);

        return new HashMap<String, String>() {
            {
                put("htw", "abcd123");
                put("admin", "haha");
            }
        };
    }


}
