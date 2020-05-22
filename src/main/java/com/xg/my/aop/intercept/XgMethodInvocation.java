package com.xg.my.aop.intercept;

import com.xg.my.aop.aspect.XgJoinPoint;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-11 15:00
 **/
public class XgMethodInvocation implements XgJoinPoint {

    private Object proxy;
    private Method method;
    private Object target;
    private Class<?> targetClass;
    private Object[] arguments;
    private List<Object> interceptorsAndDynamicMethodMatchers;

    private int currentInterceptorIndex= -1;

    public XgMethodInvocation(Object proxy, Object target, Method method, Object[] arguments, Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers){
        this.proxy = proxy;
        this.method = method;
        this.target = target;
        this.targetClass = targetClass;
        this.arguments = arguments;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }

    public Object proceed() throws  Throwable{
        if(this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() -1 ){
            return this.method.invoke(this.target, this.arguments);
        }
        Object interceptorAndDynamicMethodMatchers = this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
        if(interceptorAndDynamicMethodMatchers instanceof XgMethodInvocation){
            XgMethodInterceptor mi = (XgMethodInterceptor) interceptorAndDynamicMethodMatchers;
            return mi.invoke(this);
        }else{
            return proceed();
        }


    }


    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Object getThis() {
        return this.target;
    }
}
