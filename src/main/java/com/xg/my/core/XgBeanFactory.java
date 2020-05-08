package com.xg.my.core;

public interface XgBeanFactory {

    Object getBean(String beanName) throws Exception;

    public Object getBean(Class<?> beanClass) throws Exception;

}
