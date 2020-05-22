package com.xg.demo.server.impl;

import com.xg.demo.server.IDemoService;
import com.xg.my.annotation.XgService;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-08 23:38
 **/
@XgService
public class DemoService implements IDemoService {

    @Override
    public String demo() throws Exception {
        //throw new Exception("demo NullPointerException");
        System.out.println("DemoServiceImpl demo ......");
        return "";
    }
}
