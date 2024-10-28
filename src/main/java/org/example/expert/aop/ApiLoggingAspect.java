package org.example.expert.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.expert.config.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ApiLoggingAspect {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Pointcut("@annotation(org.example.expert.aop.CustomLogging)")
    public void loggingMethods() {
    }

    @Around("loggingMethods()")
    public Object logApiRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime now = LocalDateTime.now();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String requestURL = request.getRequestURL().toString();

        String tokenValue = request.getHeader("Authorization");

        String userId = jwtUtil.getSubject(tokenValue);

        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg != null) {
                String value = objectMapper.writeValueAsString(arg);
                log.info("Parameters = {}", value);
            }
        }

        log.info("User ID = {}", userId);
        log.info("Request Time = {}", now);
        log.info("Request URL = {}", requestURL);

        return joinPoint.proceed();
    }

}
