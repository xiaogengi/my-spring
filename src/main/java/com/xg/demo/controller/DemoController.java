package com.xg.demo.controller;

import com.xg.demo.server.DemoService;
import com.xg.my.annotation.XgAutowired;
import com.xg.my.annotation.XgController;
import com.xg.my.annotation.XgRequestMapping;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-08 23:36
 **/
@XgController
public class DemoController {

    @XgAutowired
    private DemoService demoService;

    @XgRequestMapping("/demo")
    public String demo(){
        return demoService.demo();
    }

}
