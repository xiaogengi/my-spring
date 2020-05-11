package com.xg.my.webmvc;

import java.util.Map;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-09 11:57
 **/
public class XgModelAndView {

    private String viewName;

    private Map<String, ?> model;

    public XgModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public XgModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }
}
