package com.xg.my.aop.aspect;

import com.xg.my.aop.intercept.XgMethodInterceptor;
import com.xg.my.aop.intercept.XgMethodInvocation;

import java.lang.reflect.Method;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-11 16:02
 **/
public class XgAfterThrowingAdvice extends XgAbstractAspectJAdvice implements XgAdvice, XgMethodInterceptor {

    private String throwingName;
    private XgMethodInvocation mi;

    public void setThrowingName(String throwingName) {
        this.throwingName = throwingName;
    }

    public XgAfterThrowingAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(XgMethodInvocation mi) throws Throwable {
        try {
            return mi.proceed();
        } catch (Throwable ex){
            ex.printStackTrace();
            super.invokeAdviceMethod(mi, null, ex.getCause());
            throw ex;
        }
    }
}
