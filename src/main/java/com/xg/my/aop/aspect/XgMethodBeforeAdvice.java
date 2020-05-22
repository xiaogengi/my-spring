package com.xg.my.aop.aspect;

import com.xg.my.aop.intercept.XgMethodInterceptor;
import com.xg.my.aop.intercept.XgMethodInvocation;

import java.lang.reflect.Method;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-11 15:55
 **/
public class XgMethodBeforeAdvice extends XgAbstractAspectJAdvice implements XgAdvice, XgMethodInterceptor {

    private XgJoinPoint joinPoint;

    public XgMethodBeforeAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }


    public void before(Method method, Object[] args, Object target) throws Throwable{
        super.invokeAdviceMethod(this.joinPoint, null, null);
    }


    @Override
    public Object invoke(XgMethodInvocation mi) throws Throwable {
        this.joinPoint = mi;
        this.before(mi.getMethod(), mi.getArguments(), mi.getThis());
        return mi.proceed();
    }
}
