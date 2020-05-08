package com.xg.my.context.support;

import com.xg.my.beans.config.XgBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.Adler32;

/**
 * @program: my-spring
 * @description: 对配置文件进行查找，读取，解析
 * @author: gzk
 * @create: 2020-05-08 18:03
 **/
public class XgBeanDefinitionReader {

    private List<String> registyBeanClasses = new ArrayList<>();

    private Properties config = new Properties();

    // 固定的配置信息 相当于xml里面的规范
    private final String SCAN_PACKAGE = "scanPackage";

    public XgBeanDefinitionReader(String... locations){
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:", ""));
        try {
            config.load(is);
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            if(null != is){
                try {
                    is.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    private void doScanner(String scanPackage){
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if(file.isDirectory()){
                doScanner(scanPackage + "." + file.getName());
            }else{
                if(!file.getName().endsWith(".class")){ continue;}
                String className = (scanPackage + "." + file.getName().replace(".class",""));
                registyBeanClasses.add(className);
            }
        }

    }

    public Properties getConfig(){
        return this.config;
    }

    public List<XgBeanDefinition> loadBeanDefinitions(){
        List<XgBeanDefinition> result = new ArrayList<>();
        try {
            for (String beanName : registyBeanClasses) {
                Class<?> beanClass = Class.forName(beanName);
                if(beanClass.isInterface()){
                    continue;
                }
                result.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()),beanClass.getName()));

                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> i : interfaces) {
                    result.add(doCreateBeanDefinition(i.getName(), beanClass.getName()));
                }

            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private XgBeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
        XgBeanDefinition beanDefinition = new XgBeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }


}
