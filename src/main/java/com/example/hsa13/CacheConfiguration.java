package com.example.hsa13;

import com.dinstone.beanstalkc.BeanstalkClientFactory;
import com.dinstone.beanstalkc.JobConsumer;
import com.dinstone.beanstalkc.JobProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class CacheConfiguration {

    @Bean
    RedisTemplate<String, Student> redisTemplate(RedisConnectionFactory connectionFactory,
                                                 Jackson2JsonRedisSerializer<Student> serializer) {
        RedisTemplate<String, Student> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setDefaultSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    @Bean
    public Jackson2JsonRedisSerializer<Student> jackson2JsonRedisSerializer() {
        return new Jackson2JsonRedisSerializer<>(Student.class);
    }

    @Bean
    public RedisMessageListenerContainer listenerContainer(MessageListenerAdapter listenerAdapter,
                                                           RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("students"));
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(StudentConsumer consumer) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(consumer);
        messageListenerAdapter.setSerializer(new Jackson2JsonRedisSerializer<>(Student.class));
        return messageListenerAdapter;
    }

    @Bean
    public BeanstalkClientFactory beanstalkClientFactory() {
        // set beanstalkd service host and port and other connetion config,
        // then create job producer or consumer by this config.
        com.dinstone.beanstalkc.Configuration config = new com.dinstone.beanstalkc.Configuration();
        config.setServiceHost("localhost");
        config.setServicePort(11300);
        config.setConnectTimeout(20000);
        config.setReadTimeout(30000);

        BeanstalkClientFactory factory = new BeanstalkClientFactory(config);
        return factory;
    }

    @Bean
    public JobProducer jobProducer(BeanstalkClientFactory factory) {
        return factory.createJobProducer("students");
    }

    @Bean
    public JobConsumer jobConsumer(BeanstalkClientFactory factory) {
        return factory.createJobConsumer("students");
    }
}
