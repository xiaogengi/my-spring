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


    public Object getWrapperInstance() {
        return wrapperInstance;
    }

    public void setWrapperInstance(Object wrapperInstance) {
        this.wrapperInstance = wrapperInstance;
    }

    public Class<?> getWrapperClass() {
        return this.wrapperInstance.getClass();
    }

    public void setWrapperClass(Class<?> wrapperClass) {
        this.wrapperClass = wrapperClass;
    }

    @Override
    public String toString() {
        return "XgBeanWrapper{" +
                "wrapperInstance=" + wrapperInstance +
                ", wrapperClass=" + wrapperClass +
                '}';
    }
}
