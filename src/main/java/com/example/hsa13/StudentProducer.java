package com.example.hsa13;

import com.dinstone.beanstalkc.BeanstalkClientFactory;
import com.dinstone.beanstalkc.JobConsumer;
import com.dinstone.beanstalkc.JobProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class StudentProducer {
    @Autowired
    private RedisTemplate<String, Student> redisTemplate;
    @Autowired
    JobProducer jobProducer;

    public void sendMessage(Student student) {
        //System.out.println("Sending Student details: " + student);
        redisTemplate.convertAndSend("students", student);
    }

    public void sendMessageBeanstalkd(Student student) {
        jobProducer.putJob(1, 0, 5, student.toString().getBytes());
    }

}
