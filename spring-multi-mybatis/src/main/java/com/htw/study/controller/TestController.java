package com.htw.study.controller;

import com.htw.study.service.FirstService;
import com.htw.study.service.SecondService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by htw on 2019/6/23.
 */
@RestController
public class TestController {
    @Autowired
    FirstService firstService;
    @Autowired
    SecondService secondService;

    @GetMapping("/t1")
    public String test1() {
        return firstService.getUserName();
    }

    @GetMapping("/t2")
    public String test2() {
        return secondService.getUserName();
    }
}
