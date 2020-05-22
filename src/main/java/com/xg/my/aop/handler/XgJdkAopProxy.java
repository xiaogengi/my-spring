package com.xg.my.aop.handler;

import com.xg.my.aop.XgAopProxy;
import com.xg.my.aop.intercept.XgMethodInvocation;
import com.xg.my.aop.support.XgAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;


/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-11 11:38
 **/
public class XgJdkAopProxy implements XgAopProxy, InvocationHandler {

    private XgAdvisedSupport config;

    public XgJdkAopProxy(XgAdvisedSupport config) {
        this.config = config;
    }

    @Override
    public Object getProxy() {
        return this.getProxy(this.config.getTargetClass().getClassLoader());
    }

//    @Override
//    public Object getProxy(ClassLoader classLoader) {
//        return Proxy.newProxyInstance(classLoader, this.config.getTargetClass().getInterfaces(),this);
//    }
//
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        List<Object> interceptorsAndDynamicInterceptionAdvice = config.getInterceptorsAndDynamicInterceptionAdvice(method, this.config.getTargetClass());
//        XgMethodInvocation invocation = new XgMethodInvocation(proxy, this.config.getTarget(), method, args, this.config.getTargetClass(), interceptorsAndDynamicInterceptionAdvice);
//        return invocation.proceed();
//    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader,this.config.getTargetClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> interceptorsAndDynamicMethodMatchers = this.config.getInterceptorsAndDynamicInterceptionAdvice(method,this.config.getTargetClass());
        XgMethodInvocation invocation = new XgMethodInvocation(proxy,this.config.getTarget(),method,args,this.config.getTargetClass(),interceptorsAndDynamicMethodMatchers);
        return invocation.proceed();
    }
}
