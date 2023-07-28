package com.czq.apithirdpartyservices;

import cn.hutool.json.JSONUtil;
import com.czq.apicommon.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
class ApiThirdPartyServicesApplicationTests {

    @Resource
    private RedisTemplate<Object,Object> redisTemplate;


    @Test
    void contextLoads() {
        Object object = redisTemplate.opsForHash().get("spring:session:sessions:e48b8cc3-bc7a-42ad-8e7c-cba8aade30f5",
                "sessionAttr:user_login");

        String test = JSONUtil.toJsonStr(object);

        User user = JSONUtil.toBean(test, User.class);
        System.out.println(user);
    }

}
