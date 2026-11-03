package com.device.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import com.device.dto.DeviceErrorEvent;



@Configuration
@EnableKafka
public class DeviceErrorKafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, DeviceErrorEvent> deviceErrorConsumerFactory() {

        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);

        props.put(ConsumerConfig.GROUP_ID_CONFIG,"device-error-service");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);

        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,JacksonJsonDeserializer.class);

        props.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE,DeviceErrorEvent.class.getName());

        props.put(JacksonJsonDeserializer.USE_TYPE_INFO_HEADERS,false);

        props.put(JacksonJsonDeserializer.TRUSTED_PACKAGES,"*");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean(name = "kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, DeviceErrorEvent> deviceErrorKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, DeviceErrorEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(deviceErrorConsumerFactory());

        return factory;
    }
}