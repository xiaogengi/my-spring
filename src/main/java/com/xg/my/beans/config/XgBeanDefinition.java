package com.xg.my.beans.config;

/**
 * @program: my-spring
 * @description: 存储配置文件信息，相当于保存在内存的配置
 * @author: gzk
 * @create: 2020-05-08 17:30
 **/
public class XgBeanDefinition {

    private String beanClassName;
    private boolean lazyInit = false;
    private String factoryBeanName;

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    @Override
    public String toString() {
        return "XgBeanDefinition{" +
                "beanClassName='" + beanClassName + '\'' +
                ", lazyInit=" + lazyInit +
                ", factoryBeanName='" + factoryBeanName + '\'' +
                '}';
    }
}
