package com.xg.my.context.support;

import com.xg.my.beans.config.XgBeanDefinition;
import com.xg.my.context.support.XgAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-08 17:55
 **/
public class XgDefaultListableBeanFactory extends XgAbstractApplicationContext {

    // 注册信息的BeanDefinition
    protected final Map<String, XgBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();




}
