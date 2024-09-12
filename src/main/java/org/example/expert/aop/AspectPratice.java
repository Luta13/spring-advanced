package org.example.expert.aop;


import jakarta.servlet.ServletRequestAttributeEvent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class AspectPratice {

    @Pointcut("execution(public * org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..))")
    private void test1()
    {

    }
    @Pointcut("execution(public * org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    private void test2()
    {

    }

    @Around("test1() || test2()")
    public Object test(ProceedingJoinPoint joinPoint) throws Throwable
    {
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String requestUri = request.getRequestURI();
        Long userId = (Long) request.getAttribute("userId");
        String time = LocalDateTime.now().toString();
        try{
            log.info("BEFORE : " + "로깅 시간 : " + time + ", URI : " + requestUri +", 유저ID : " + userId);
            Object result = joinPoint.proceed();
            return result;
        }
        catch(Exception e)
        {
            log.info("::: AFTER THROWING :::");
            throw e;
        }
        finally{
            log.info("AFTER : " + "로깅 시간 : " + time + " URI : " + requestUri + " 유저ID : " + userId);
        }


    }


}
