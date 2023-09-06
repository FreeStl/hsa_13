package com.example.hsa13;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CacheController {
    @Autowired
    StudentProducer studentProducer;

    @PostMapping("/redis")
    @ResponseStatus(HttpStatus.OK)
    public void postData(@RequestBody Student request) {
        studentProducer.sendMessage(request);
    }

    @PostMapping("/beanstalkd")
    @ResponseStatus(HttpStatus.OK)
    public void postDataBeanstalkd(@RequestBody Student request) {
        studentProducer.sendMessageBeanstalkd(request);
    }

}
