package com.fourth.medical.auth.aop;

import com.fourth.medical.common.constant.AopConstant;
import com.fourth.medical.framework.query.DataRangeQuery;
import com.fourth.medical.util.DataRangeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author geekidea
 * @date 2022/4/20
 **/
@Slf4j
@Aspect
@Component
@ConditionalOnProperty(name = "login.app.enable", havingValue = "true", matchIfMissing = true)
public class AppDataRangeAop {

    @Around(AopConstant.APP_POINTCUT)
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (ArrayUtils.isEmpty(args)) {
            return joinPoint.proceed();
        }
        for (Object arg : args) {
            if (arg instanceof DataRangeQuery) {
                DataRangeQuery dataRangeQuery = (DataRangeQuery) arg;
                DataRangeUtil.handleAppLogin(dataRangeQuery);
                break;
            }
        }
        return joinPoint.proceed();
    }

}
