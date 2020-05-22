package com.xg.my.aop.support;

import com.xg.my.aop.XgAopConfig;
import com.xg.my.aop.aspect.XgAfterReturningAdvice;
import com.xg.my.aop.aspect.XgAfterThrowingAdvice;
import com.xg.my.aop.aspect.XgMethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-11 11:39
 **/
public class XgAdvisedSupport {

    private Class targetClass;
    private Object target;
    private Pattern pointCutClassPattern;

    private transient Map<Method, List<Object>> methodCache;

    private XgAopConfig config;

    public XgAdvisedSupport(XgAopConfig config) {
        this.config = config;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
        parse();
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Pattern getPointCutClassPattern() {
        return pointCutClassPattern;
    }

    public void setPointCutClassPattern(Pattern pointCutClassPattern) {
        this.pointCutClassPattern = pointCutClassPattern;
    }

    public Map<Method, List<Object>> getMethodCache() {
        return methodCache;
    }

    public void setMethodCache(Map<Method, List<Object>> methodCache) {
        this.methodCache = methodCache;
    }

    public XgAopConfig getConfig() {
        return config;
    }

    public void setConfig(XgAopConfig config) {
        this.config = config;
    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) throws Exception{
        List<Object> cache = methodCache.get(method);
        if(cache == null){
            Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());
            cache = methodCache.get(m);
            this.methodCache.put(m, cache);
        }
        return cache;
    }

    public boolean pointCutMatch(){
        return pointCutClassPattern.matcher(this.targetClass.toString()).matches();
    }

    private void parse(){
        String pointCut = config.getPointCut()
                .replaceAll("\\.","\\\\.")
                .replaceAll("\\\\.\\*",".*")
                .replaceAll("\\(","\\\\(")
                .replaceAll("\\)","\\\\)");
        //玩正则
        String pointCutForClassRegex = pointCut.substring(0,pointCut.lastIndexOf("\\(") - 4);
        pointCutClassPattern = Pattern.compile("class " + pointCutForClassRegex.substring(
                pointCutForClassRegex.lastIndexOf(" ") + 1));

        methodCache = new HashMap<Method, List<Object>>();

        Pattern pattern = Pattern.compile(pointCut);

        try {
            Class<?> aspectClass = Class.forName(config.getAspectClass());
            Map<String, Method> aspectMethods = new HashMap<>();
            for (Method m : aspectClass.getMethods()) {
                aspectMethods.put(m.getName(), m);
            }

            for (Method m : targetClass.getMethods()) {
                String methodString = m.toString();
                if(methodString.contains("throws")){
                    methodString = methodString.substring(0, methodString.lastIndexOf("throws")).trim();
                }
                Matcher matcher = pattern.matcher(methodString);
                if(matcher.matches()){
                    List<Object> advices = new LinkedList<>();
                    //前置通知
                    if(!(null == config.getAspectBefore() || "".equals(config.getAspectBefore().trim()))){
                        advices.add(new XgMethodBeforeAdvice(aspectMethods.get(config.getAspectBefore()), aspectClass.newInstance()));
                    }
                    // 后置通知
                    if(!(null == config.getAspectAfter() || "".equals(config.getAspectAfter().trim()))){
                        advices.add(new XgAfterReturningAdvice(aspectMethods.get(config.getAspectAfter()), aspectClass.newInstance()));
                    }
                    // 异常通知
                    if(!(null == config.getAspectAfterThrow() || "".equals(config.getAspectAfterThrow().trim()))){
                        XgAfterThrowingAdvice afterThrowingAdvice = new XgAfterThrowingAdvice(aspectMethods.get(config.getAspectAfterThrow()), aspectClass.newInstance());
                        afterThrowingAdvice.setThrowingName(config.getAspectAfterThrowingName());
                        advices.add(afterThrowingAdvice);
                    }
                    methodCache.put(m, advices);
                }
            }
        } catch (Exception e){
            e.printStackTrace();

        }

    }




}
