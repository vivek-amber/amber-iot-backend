package com.device.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.device.dto.AckEvent;
import com.device.dto.CommandResponse;
import com.device.dto.DeviceShadow;
import com.device.dto.PendingCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcknowledgeService {

    private final ObjectMapper objectMapper;

    private final RedisService redisService;

    private final ShadowService shadowService;

    private final DeviceStateService deviceStateService;

    private final SimpMessagingTemplate messagingTemplate;


    public void processAck(String companyId, String deviceId, String payload) {

        try {

            CommandResponse response = objectMapper.readValue(payload, CommandResponse.class);

            PendingCommand pending = redisService.getPendingCommand(
                    deviceId,
                    response.getCmd());

            if (pending == null) {
                return;
            }

            if (!pending.getCmi().equals(response.getCmi())) {
                return;
            }

            if ("SUCCESS".equals(response.getSts())) {

                shadowService.updateReportedState(
                        deviceId,
                        pending.getCompanyId(),
                        response.getCmd(),
                        response.getVal(),
                        response.getVrs()
                );

                DeviceShadow shadow = shadowService.getShadow(deviceId, pending.getCompanyId());

                if (shadow != null) {
                    
                    deviceStateService.save(shadow);
                    messagingTemplate.convertAndSend(
                        "/topic/device/"+deviceId+"/ack",
                            new AckEvent(
                            deviceId,
                             response.getCmd(),
                            response.getVal()   
                        )
                    );
                }
                redisService.deletePendingCommand(deviceId, response.getCmd());
            }

        } catch (JsonProcessingException e) {

            log.error("Failed to parse ACK payload. Device={}, Payload={}", deviceId, payload, e);
        }
    }
}
