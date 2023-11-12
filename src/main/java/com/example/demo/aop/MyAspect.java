package com.example.demo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class MyAspect {
//    @Pointcut("annotation(com.example.demo.annotation.MyLog)")
    @Pointcut("execution(* com.example.demo.service.impl.*.*(..))")
    private void pt() {}

    @Before("pt()")
    public void before(JoinPoint point) {
//        log.info("123");
    }

    @Around("pt()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
//        log.info("hello 1");
        String name = point.getTarget().getClass().getName();
        log.info("{}", name);

//        String signatureName = point.getSignature().getName();
//        log.info("{}", signatureName);
//
//        Object[] args = point.getArgs();
//        log.info("{}", Arrays.toString(args));

        Object result = point.proceed();
        log.info("{}", result);
        return result;
    }



}
