package com.hsuhau;

import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * SpringEL表达式，测试类
 * @author hsuhau
 * @date 2020/8/28 19:13
 */
//@SpringBootTest
//@RunWith(SpringRunner.class)
public class SpringELTest {
    @Test
    public void test(){
        String demo = "'hello:' + #userId";

        // 1.创建解析器
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(demo);

        // 2.上下文
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("userId", "hsuhau");

        // 3.解析
        String result = expression.getValue(context).toString();
        System.out.println(result);
    }

}
