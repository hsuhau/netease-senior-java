package com.hsuhau.annotation.aop;

import com.hsuhau.annotation.NeteaseCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;

/**
 * cache注解，AOP实现
 */
@Aspect
@Component
public class CacheAspect {

    @Autowired
    private RedisTemplate redisTemplate;

    @Pointcut("@annotation(com.hsuhau.annotation.NeteaseCache)")
    public void cachePoint() {

    }

    /**
     * 定义相应的事件
     *
     * @param joinPoint
     * @return
     */
    @Around("cachePoint()")
    public Object doCache(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取key value
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method[] methods = joinPoint.getTarget().getClass().getMethods();
        Method method = joinPoint.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getMethod().getParameterTypes());
        NeteaseCache neteaseCache = method.getAnnotation(NeteaseCache.class);
        String keyEL = neteaseCache.key();
        String prefix = neteaseCache.value();

        // springEL表达式
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression expression = spelExpressionParser.parseExpression(keyEL);
        EvaluationContext context = new StandardEvaluationContext();

        // 添加参数
        Object[] args = joinPoint.getArgs();
        DefaultParameterNameDiscoverer discover = new DefaultParameterNameDiscoverer();
        String[] parameterNames = discover.getParameterNames(method);
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        // 解析
        String key = prefix + "::" + expression.getValue(context).toString();
        Object value = redisTemplate.opsForValue().get(key);

        if (!ObjectUtils.isEmpty(value)) {
            System.out.println("从缓存中读取到值：" + value);
            return value;
        }

        // 2.不存在则执行方法
        value = joinPoint.proceed();

        // 3.同步存储value到缓存
        redisTemplate.opsForValue().set(key, value);
        return value;
    }
}
