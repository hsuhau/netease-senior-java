package com.hsuhau.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;


/**
 * key 的生成策略，缓存名+缓存key（支持spring EL表达式）
 *
 * @author hsuhau
 * @date 2020/7/23 1:32
 */
@Component
public class DefaultKeyGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultKeyGenerator.class);

    /**
     * 用于SpELl表达式解析
     */
    private SpelExpressionParser parser = new SpelExpressionParser();

    /**
     * 用于获取方法参数的定义名称
     */
    private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * 缓存key的生成
     *
     * @param point
     * @param cacheName
     * @param cacheKey
     * @return
     * @throws Throwable
     */
    @Bean
    public String generateKey(ProceedingJoinPoint point, String cacheName, String cacheKey) throws Throwable {
        if (StringUtils.isEmpty(cacheKey)) {
            throw new NullPointerException("cacheKey不能为空");
        }
        Signature signature = point.getSignature();
        if (cacheName == null) {
            cacheName = signature.getDeclaringTypeName() + "." + signature.getName();
        }
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        if (!(signature instanceof MethodSignature)) {
            throw new NullPointerException("这个注解只能用在方法上");
        }
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = point.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getMethod().getParameterTypes());
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Object[] args = point.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            evaluationContext.setVariable(parameterNames[i], args[i]);
        }
        String result = "CacheName_" + cacheName + "_CacheKey_" + parser.parseExpression(cacheKey).getValue(evaluationContext, String.class);
        LOGGER.info("*******************>>>{}", result);
        return result;
    }
}
