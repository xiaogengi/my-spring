package com.xg.my.aop;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-11 11:36
 **/
public interface XgAopProxy {
    Object getProxy();
    
    Object getProxy(ClassLoader classLoader);
}
