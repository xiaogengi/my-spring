package com.xg.demo.controller;

import com.xg.demo.server.DemoService;
import com.xg.my.annotation.XgAutowired;
import com.xg.my.annotation.XgController;
import com.xg.my.annotation.XgRequestMapping;
import com.xg.my.webmvc.XgModelAndView;

import javax.crypto.interfaces.PBEKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public String demo(HttpServletResponse response) throws IOException {
        demoService.demo();
        response.getWriter().write("hi Demo");
        return "";
    }

    @XgRequestMapping("/test")
    public XgModelAndView test(HttpServletRequest request, HttpServletResponse response){
        return out(response, "test");
    }

    @XgRequestMapping("/first.do")
    public XgModelAndView first(){

        HashMap<String, Object> map = new HashMap<>();
        map.put("name","Xg");
        map.put("data","{'sex':'男'}");
        map.put("token","898918989891839182391");

        return new XgModelAndView("/first", map);
    }


    private XgModelAndView out(HttpServletResponse resp, String str){
        try {
            resp.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
