package com.xg.my.aop.aspect;

import java.lang.reflect.Method;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-11 15:46
 **/
public abstract class XgAbstractAspectJAdvice implements XgAdvice{

    private Method aspectMethod;
    private Object aspectTarget;

    public XgAbstractAspectJAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }

    protected Object invokeAdviceMethod(XgJoinPoint joinPoint, Object returnValue, Throwable ex) throws Throwable{
        Class<?>[] parameterTypes = this.aspectMethod.getParameterTypes();
        if(null == parameterTypes || parameterTypes.length == 0){
            return this.aspectMethod.invoke(aspectTarget);
        }else{
            Object[] args = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                if(parameterTypes[i] == XgJoinPoint.class){
                    args[i] = joinPoint;
                }else if(parameterTypes[i] == Throwable.class){
                    args[i] = ex;
                }else if(parameterTypes[i] == Object.class){
                    args[i] = returnValue;
                }
            }
            return this.aspectMethod.invoke(aspectTarget, args);
        }
    }

}
