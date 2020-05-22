package com.xg.my.aop.aspect;

import java.lang.reflect.Method;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-11 15:00
 **/
public interface XgJoinPoint {

    Method getMethod();

    Object[] getArguments();

    Object getThis();


}
