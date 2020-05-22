package com.xg.my.beans;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-08 17:32
 **/
public class XgBeanWrapper {

    private Object wrapperInstance;
    private Class<?> wrapperClass;

    public XgBeanWrapper(Object wrapperInstance){
        this.wrapperInstance = wrapperInstance;
    }
    public Object getWrapperInstance(){
        return this.wrapperInstance;
    }

    public Class<?> getWrapperClass(){
        return this.getWrapperInstance().getClass();
    }
}
