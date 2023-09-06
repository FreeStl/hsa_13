package com.example.hsa13;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Student implements Serializable {
    private String key;
    private String name;
}
