package com.igoso.me.gallery.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * created by igoso at 2018/5/29
 **/
@Aspect
@Component
public class SqlTimeLogAspect {

    private static long time;
    private static final Logger LOGGER = LoggerFactory.getLogger("SQL_TIME_LOG");

    @Pointcut("execution (public * com.igoso.me.gallery.dao.*.*(..))")
    public void log() {

    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        time = System.currentTimeMillis();
    }


    @After("log()")
    public void doAfter(JoinPoint joinPoint) {
        LOGGER.info("sql execute time:{}",System.currentTimeMillis() - time);
    }
}
