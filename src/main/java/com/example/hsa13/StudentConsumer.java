package com.example.hsa13;

import com.dinstone.beanstalkc.BeanstalkClientFactory;
import com.dinstone.beanstalkc.Job;
import com.dinstone.beanstalkc.JobConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;

@Component
@EnableAsync
public class StudentConsumer {
    @Autowired
    BeanstalkClientFactory beanstalkClientFactory;
    @Autowired
    JobConsumer jobConsumer;

    public void handleMessage(Student student) {
        System.out.println("Consumer received: " + student + " time: " + Instant.now());
    }

    @Async
    @Scheduled(fixedRate = 5)
    public void consumeJob() {
        Job job = jobConsumer.reserveJob(1);
        if (job != null) {
            System.out.println("Consumer received: " + Arrays.toString(job.getData()) + " time: " + Instant.now());
            jobConsumer.deleteJob(job.getId());
            for (int i = 0; i < 100_000; i++) {
                Job job2 = jobConsumer.reserveJob(1);
                System.out.println("Consumer received: " + Arrays.toString(job2.getData()) + " time: " + Instant.now());
                jobConsumer.deleteJob(job2.getId());
            }
        }

    }
}
