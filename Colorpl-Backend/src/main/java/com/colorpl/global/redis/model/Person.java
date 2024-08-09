package com.colorpl.global.redis.model;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@RedisHash("people")
public class Person {

    @Id
    String id;
    String firstname;
    String lastname;
}
