package com.device.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.device.service.StompJwtChannelInterceptor;



@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompJwtChannelInterceptor stompJwtChannelInterceptor;

    public WebsocketConfig(StompJwtChannelInterceptor stompJwtChannelInterceptor)
    {
        this.stompJwtChannelInterceptor =stompJwtChannelInterceptor;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.enableSimpleBroker("/topic")
        .setHeartbeatValue(new long[] { 10000, 10000 })
        .setTaskScheduler(taskScheduler());
        registry.setApplicationDestinationPrefixes("/app");
    }
    
    @Override
    public void configureClientInboundChannel(
        ChannelRegistration registration)
    {
        registration.interceptors(stompJwtChannelInterceptor);

    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("stomp-heartbeat-");
        scheduler.initialize();
        return scheduler;
    }
}
