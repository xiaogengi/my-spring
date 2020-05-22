package com.xg.demo.aspect;

import com.xg.my.aop.aspect.XgJoinPoint;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @program: my-spring
 * @description:
 * @author: gzk
 * @create: 2020-05-11 11:33
 **/
@Slf4j
public class LogAspect {

    public void before(XgJoinPoint joinPoint){
        log.info("Invoke Before Method !! " +
                "\nTargetObject: " + joinPoint.getThis() +
                "\n Args : " + Arrays.toString(joinPoint.getArguments()));
    }

    public void after(XgJoinPoint joinPoint){
        log.info("Invoke After Method!!" +
                "\nTargetObject: " + joinPoint.getThis() +
                "\nArgs : " + Arrays.toString(joinPoint.getArguments()));
    }

    public void afterThrowing(XgJoinPoint joinPoint){
        log.info("Invoke AfterThrowing Method!!" +
                "\nTargetObject: " + joinPoint.getThis() +
                "\nArgs : " + Arrays.toString(joinPoint.getArguments()));
    }



}
