package com.yupi.springbootinit;

import com.czq.apicommon.service.ApiBackendService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * 主类测试
 *
 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    private ApiBackendService apiBackendService;

    @Resource
    private RedisTemplate<Object,Object> redisTemplate;


    @Test
    void contextLoads() {
//        apiBackendService.invokeCount(1,3);
//        User user = new User();
//        user.setId(0L);
//        user.setAccessKey("11");
//        user.setSecretKey("22");
//        user.setUserAccount("33");
//        user.setUserPassword("44");
//        user.setUnionId("55");
//        user.setMpOpenId("66");
//        user.setUserName("77");
//        user.setUserAvatar("我");
//        user.setUserProfile("哈哈哈");
//        user.setUserRole("我");
//        user.setCreateTime(new Date());
//        user.setUpdateTime(new Date());
//        user.setIsDelete(0);
//
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setStringSerializer(new StringRedisSerializer());

//        redisTemplate.opsForHash().put("test","hashKey",user);
    }


}
