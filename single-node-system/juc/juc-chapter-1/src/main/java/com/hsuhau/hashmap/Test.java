package com.hsuhau.hashmap;

import java.util.HashMap;

/**
 * JDK源码学习方法更重要
 * 逻辑思维能力是梳理学习方法的基础。养成线性思维：两个或者多个概念，像一条线串起来。
 * <p>
 * 演绎推导法
 * 示例：因果推理。因为Java中网络编程之提供了BIO和NIO两种方式，所以一切框架中，涉及到网络处理的，都可以用这两个知识点去探究原理。
 * <p>
 * 归纳总结法
 * 示例：可能正确的猜想。线上10台服务器，有三台总是每天会自动重启，收集相关信息后，发现时运维在修改监控系统配置的时候，漏掉了提高这三台机器的重启阈值。
 * <p>
 * 类比法：
 * 集群概念就好像是马在拉车，一匹马拉不动的时候，就使用多匹马去拉。分布式的概念，就像是理发的过程中，洗头发和箭头是不同的人负责的。
 * <p>
 * 推理HashMap的实现
 *
 * <img src="HashMap.png">
 *
 * @author hsuhau
 * @date 2020/7/20 23:14
 */
public class Test {
    public static void main(String[] args) {
        HashMap<String, Object> map = new HashMap<>(16);
        map.put("hsuhau", "hsuhau");
        String name = (String) map.get("hsuhau");
        System.out.println(name);
    }
}
