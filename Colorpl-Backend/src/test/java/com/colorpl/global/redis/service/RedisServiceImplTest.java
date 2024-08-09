package com.colorpl.global.redis.service;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisServiceImplTest {

    @Autowired
    RedisService redisService;

    @Test
    void setValues() {

        String key = "key";
        String value = "value";

        redisService.setValues(key, value);

        String response = redisService.getValue(key);

        assertThat(response).isEqualTo(value);
    }

    @Test
    void testSetValues() {
    }

    @Test
    void getValue() {
    }

    @Test
    void deleteValue() {
    }
}