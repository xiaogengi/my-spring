package com.xg.my.aop.handler;

import com.xg.my.aop.XgAopProxy;
import com.xg.my.aop.support.XgAdvisedSupport;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-11 11:37
 **/
public class XgCglibAopProxy implements XgAopProxy {

    private XgAdvisedSupport advisedSupport;

    public XgCglibAopProxy(XgAdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
