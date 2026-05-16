package az.ingress.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExecutionTrackerAspect {

    @Around("@annotation(az.ingress.aspect.ExecutionTracker) || " +
            "@within(az.ingress.aspect.ExecutionTracker)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            log.info("Method {} executed in {} ms",
                    joinPoint.getSignature().getName(),
                    System.currentTimeMillis() - startTime);
            return result;
        } catch (Throwable ex) {
            log.warn("Method {} failed after {} ms",
                    joinPoint.getSignature().getName(),
                    System.currentTimeMillis() - startTime);
            throw ex;
        }
    }
}
