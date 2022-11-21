package com.amnesie.reggie.config;

import org.springframework.cache.jcache.config.JCacheConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author : Amnesie
 * @Date : 2022/11/21 15:15
 * @Description : 修改了默认Redis序列化器
 **/
@Configuration
public class RedisConfig extends JCacheConfigurerSupport {
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connection) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
        //默认的key序列化器为： JdkSerializationRedisSerializer
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(connection);
        return redisTemplate;
    }
}
