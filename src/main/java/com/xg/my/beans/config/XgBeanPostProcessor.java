package com.xg.my.beans.config;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-08 22:57
 **/
public class XgBeanPostProcessor {

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception{
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception{
        return bean;
    }

}
