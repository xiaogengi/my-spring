package com.xg.my.aop.aspect;

import com.xg.my.aop.intercept.XgMethodInterceptor;
import com.xg.my.aop.intercept.XgMethodInvocation;

import java.lang.reflect.Method;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-11 15:58
 **/
public class XgAfterReturningAdvice extends XgAbstractAspectJAdvice implements XgAdvice, XgMethodInterceptor {

    private XgJoinPoint joinPoint;

    public XgAfterReturningAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(XgMethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        this.joinPoint = mi;
        this.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
        return retVal;
    }

    public void afterReturning(Object returnVal, Method method, Object[] args, Object target) throws Throwable{
        super.invokeAdviceMethod(joinPoint, returnVal, null);
    }

}
