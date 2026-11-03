package com.device.service;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.device.dto.DeviceStatusRequest;
import com.device.dto.StatusEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor 
public class StompSubscribeListener {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final PresenceService presenceService;

    @EventListener 
    public void handleSubscribe(SessionSubscribeEvent event) {
        
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination();

        if (destination == null) {
            return;
        }

        if (destination.endsWith("/status")) {

            String[] parts = destination.split("/");

            String companyId = parts[3];
            String deviceId = parts[5];

            DeviceStatusRequest request = DeviceStatusRequest
            .builder()
            .deviceId(deviceId)
            .companyId(companyId)
            .build();
            
            StatusEvent status = presenceService.getStatus(request).getData();

            messagingTemplate.convertAndSend(destination,status);
        }
    }
}
