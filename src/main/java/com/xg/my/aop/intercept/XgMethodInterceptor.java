package com.xg.my.aop.intercept;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-11 15:12
 **/
public interface XgMethodInterceptor {

    Object invoke(XgMethodInvocation mi) throws Throwable;

}
