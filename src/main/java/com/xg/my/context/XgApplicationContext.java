package com.xg.my.context;

import com.xg.my.annotation.XgAutowired;
import com.xg.my.annotation.XgController;
import com.xg.my.annotation.XgService;
import com.xg.my.aop.XgAopConfig;
import com.xg.my.aop.XgAopProxy;
import com.xg.my.aop.handler.XgCglibAopProxy;
import com.xg.my.aop.handler.XgJdkAopProxy;
import com.xg.my.aop.support.XgAdvisedSupport;
import com.xg.my.beans.XgBeanWrapper;
import com.xg.my.beans.config.XgBeanDefinition;
import com.xg.my.beans.config.XgBeanPostProcessor;
import com.xg.my.context.support.XgBeanDefinitionReader;
import com.xg.my.context.support.XgDefaultListableBeanFactory;
import com.xg.my.core.XgBeanFactory;
import org.omg.CORBA.OBJ_ADAPTER;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-08 17:58
 **/
public class XgApplicationContext extends XgDefaultListableBeanFactory implements XgBeanFactory {

    private String[] configLocations;

    private XgBeanDefinitionReader reader;

    //用来保存注册单例的容器
    private Map<String,Object> singletonBeanCacheMap = new HashMap<>();

    //存储所有被代理过的对象
    private Map<String, XgBeanWrapper> beanWrapperMap = new ConcurrentHashMap<>();

    public XgApplicationContext(String ... configLocations){
        this.configLocations = configLocations;
        try {
            refresh();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Object getBean(String beanName) throws Exception {
        try {
            //生成通知事件
            XgBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
            XgBeanPostProcessor beanPostProcessor = new XgBeanPostProcessor();
            Object instance = instantiateBean(beanDefinition);
            if(instance == null){return  null;}
            //初始化bean前调用一次
            beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            XgBeanWrapper beanWrapper = new XgBeanWrapper(instance);
            this.beanWrapperMap.put(beanName, beanWrapper);
            //初始化bean后调用一次
            beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            populateBean(beanName, beanWrapper);
            return this.beanWrapperMap.get(beanName).getWrapperInstance();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void refresh() throws Exception {
        //定位配置文件
        reader = new XgBeanDefinitionReader(this.configLocations);

        //加载配置文件，扫描相关的类，帮他们封装成BeanDefinition
        List<XgBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

        //注册到BeanDefinition 放到伪IOC中
        doRegisterBeanDefinition(beanDefinitions);

        //把不是延迟加载的类提前初始化
        doXgAutowired();

    }

    private void doXgAutowired() {
        try {
            super.beanDefinitionMap.forEach((k,v) ->{
                if(!v.isLazyInit()){
                    try {
                        getBean(k);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }

//        for (Map.Entry<String, XgBeanDefinition> beanDefinition : super.beanDefinitionMap.entrySet()) {
//            String beanName = beanDefinition.getKey();
//            if(!beanDefinition.getValue().isLazyInit()){
//                try {
//                    getBean(beanName);
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    private void doRegisterBeanDefinition(List<XgBeanDefinition> beanDefinitions) throws Exception {
        for (XgBeanDefinition beanDefinition : beanDefinitions){
            if(super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())){
                throw new Exception("The '" + beanDefinition.getFactoryBeanName() + "' is exists! ");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
        }
    }

    private void populateBean(String beanName, XgBeanWrapper beanWrapper) {
        Class<?> clazz = beanWrapper.getWrapperClass();
        Object instance = beanWrapper.getWrapperInstance();
        //如果没有Controller 没有Service 就不注入
        if(!(clazz.isAnnotationPresent(XgController.class) || clazz.isAnnotationPresent(XgService.class))){
            return;
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields){
            if(!(field.isAnnotationPresent(XgAutowired.class))){continue;}
            XgAutowired autowired = field.getAnnotation(XgAutowired.class);
            String autowiredBeanName = autowired.value().trim();
            if("".endsWith(autowiredBeanName)){
                autowiredBeanName = field.getType().getName();
            }
            field.setAccessible(true);
            try {
                field.set(instance, this.beanWrapperMap.get(autowiredBeanName).getWrapperInstance());
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private Object instantiateBean(XgBeanDefinition beanDefinition) {
        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        try {

            if(this.singletonBeanCacheMap.containsKey(className)){
                instance = this.singletonBeanCacheMap.get(className);
            }else{
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();


                XgAdvisedSupport config = instantionAopConfig(beanDefinition);
                config.setTargetClass(clazz);
                config.setTarget(instance);

                if(config.pointCutMatch()){
                    instance = createProxy(config).getProxy();
                }

                this.singletonBeanCacheMap.put(className, instance);
                this.singletonBeanCacheMap.put(beanDefinition.getFactoryBeanName(), instance);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return instance;
    }

    private XgAopProxy createProxy(XgAdvisedSupport config) {
        Class targetClass = config.getTargetClass();
        if(targetClass.getInterfaces().length > 0){
            return new XgJdkAopProxy(config);
        }
        return new XgCglibAopProxy(config);
    }

    private XgAdvisedSupport instantionAopConfig(XgBeanDefinition beanDefinition) {
        XgAopConfig aopConfig = new XgAopConfig();

        aopConfig.setPointCut(reader.getConfig().getProperty("pointCut"));
        aopConfig.setAspectClass(reader.getConfig().getProperty("aspectClass"));
        aopConfig.setAspectBefore(reader.getConfig().getProperty("aspectBefore"));
        aopConfig.setAspectAfter(reader.getConfig().getProperty("aspectAfter"));
        aopConfig.setAspectAfterThrow(reader.getConfig().getProperty("aspectAfterThrow"));
        aopConfig.setAspectAfterThrowingName(reader.getConfig().getProperty("aspectAfterThrowingName"));

        return new XgAdvisedSupport(aopConfig);
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return this.getBean(beanClass.getName());
    }

    public String[] getBeanDefinitionNames(){
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanWrapperMap.size()]);
    }

    public int getBeanDefinitionSize(){
        return this.beanDefinitionMap.size();
    }

    public Properties getConfig(){
        return this.reader.getConfig();
    }

}
