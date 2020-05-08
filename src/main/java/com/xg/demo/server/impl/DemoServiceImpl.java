package com.xg.demo.server.impl;

import com.xg.demo.server.DemoService;
import com.xg.my.annotation.XgService;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-08 23:38
 **/
@XgService
public class DemoServiceImpl implements DemoService {

    @Override
    public String demo() {
        return "hi~";
    }
}
