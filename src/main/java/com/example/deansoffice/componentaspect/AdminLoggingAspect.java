package com.example.deansoffice.componentaspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class AdminLoggingAspect {
    private static final Logger logger = LogManager.getLogger("MyApp");

    @Pointcut("within(com.example.deansoffice.controller.AdminController)")
    public void adminControllerPointcut() {
    }

    @Before("adminControllerPointcut()")
    public void logAdminAction(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();

        System.out.println(getClass().getResource("/log4j2.xml"));

        logger.info("Admin with id {} executed method {} with arguments {}",
                args[0], methodName, Arrays.toString(Arrays.copyOfRange(args, 1, args.length)));
    }
}