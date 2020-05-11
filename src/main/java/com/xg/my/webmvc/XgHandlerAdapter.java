package com.xg.my.webmvc;

import com.xg.my.annotation.XgRequestMapping;
import com.xg.my.annotation.XgRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-09 11:56
 **/
public class XgHandlerAdapter {

    public boolean supports(Object handler){
        return (handler instanceof XgHandlerMapping);
    }

    public XgModelAndView handler(HttpServletRequest request, HttpServletResponse response, Object handler) throws  Exception{
        XgHandlerMapping handlerMapping = (XgHandlerMapping) handler;

        // 每一个方法都有一个参数列表，这里保存形参列表
        Map<String, Integer> paramMapping = new HashMap<>();

        Annotation[][] parameterAnnotations = handlerMapping.getMethod().getParameterAnnotations();

        for (int i = 0 ;i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if(annotation instanceof XgRequestParam){
                    String paramName = ((XgRequestParam) annotation).value();
                    if(!"".endsWith(paramName.trim())){
                        paramMapping.put(paramName, i);
                    }
                }
            }
        }

        Class<?>[] parameterTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            if(type == HttpServletRequest.class || type == HttpServletResponse.class){
                paramMapping.put(type.getName(), i);
            }
        }

        Map<String, String[]> requestParameterMap = request.getParameterMap();

        Object[]  paramValues = new Object[parameterTypes.length];

        for (Map.Entry<String,String[]>  requestParam : requestParameterMap.entrySet()){
            String value = Arrays.toString(requestParam.getValue()).replaceAll("\\[|\\]]","").replaceAll("\\s", "");

            if(!paramMapping.containsKey(requestParam.getKey())){continue;}

            int index = paramMapping.get(requestParam.getKey());

            paramValues[index] = caseStringValue(value, parameterTypes[index]);
        }

        if(paramMapping.containsKey(HttpServletRequest.class.getName())){
            Integer reqIndex = paramMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = request;
        }

        if(paramMapping.containsKey(HttpServletResponse.class.getName())){
            Integer repsIndex = paramMapping.get(HttpServletResponse.class.getName());
            paramValues[repsIndex] = response;
        }

        Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);

        if(result == null){ return null;}

        boolean isModeAndView = handlerMapping.getMethod().getReturnType() == XgModelAndView.class;

        if(isModeAndView){
            return (XgModelAndView)result;
        }else{
            return null;
        }
    }

    private Object caseStringValue(String value, Class<?> clazz) {
        //参数可以无限拓展
        if(clazz == String.class){
            return value;
        }else if(clazz == Integer.class){
            return Integer.parseInt(value);
        }else if(clazz == int.class){
            return Integer.valueOf(value).intValue();
        }else{
            return null;
        }
    }

}
