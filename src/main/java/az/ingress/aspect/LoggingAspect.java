package az.ingress.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* az.ingress.controller.*.*(..))")
    public void controllerMethods() {
    }

    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Called controller method : {} with args : {}", joinPoint.getSignature().getName(),
                Arrays.stream(joinPoint.getArgs())
                        .map(arg -> arg instanceof String s && s.length() > 50 ? "[TRUNCATED]" : arg)
                        .toList());
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Called controller method : {} returned with status : {}", joinPoint.getSignature().getName(),
                result instanceof ResponseEntity<?> r ? r.getStatusCode() : result);
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        log.error("Exception in method : {}", joinPoint.getSignature().getName(), ex);
    }
}
