package com.device.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.device.dto.CommandResultDto;
import com.device.dto.DeviceShadow;
import com.device.dto.PendingCommand;
import com.device.enums.CommandStatus;
import com.device.proto.DeviceAck;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcknowledgeService {

    private final RedisService redisService;

    private final ShadowService shadowService;

    private final DeviceStateService deviceStateService;

    private final SimpMessagingTemplate messagingTemplate;


    public void processAck(String companyId, String deviceId, DeviceAck ack) {

        try {

            PendingCommand pending = redisService.getPendingCommand(
                    deviceId,
                    ack.getCmd());

            if (pending == null) {
                return;
            }

            if (!pending.getCmi().equals(ack.getCmi())) {
                return;
            }

            if (Boolean.TRUE.equals(ack.getSts())) {
                shadowService.updateReportedState(
                        deviceId,
                        pending.getCompanyId(),
                        ack.getCmd(),
                        ack.getVal(),
                        ack.getVrs());

                DeviceShadow shadow = shadowService.getShadow(deviceId, pending.getCompanyId());

                if (shadow != null) {

                    deviceStateService.save(shadow);

                    CommandResultDto res = CommandResultDto.builder()
                    .deviceId(deviceId)
                    .cmd(ack.getCmd())
                    .value(ack.getVal())
                    .status(CommandStatus.SUCCESS)
                    .build();
                    
                    messagingTemplate.convertAndSend("/topic/company/" + companyId + "/device/" + deviceId + "/ack", res);
                }
                redisService.deletePendingCommand(deviceId, ack.getCmd());
            }

        } catch (Exception e) {

            log.error("Failed to parse ACK payload. Device={}, Payload={}", deviceId, e);
        }
    }
    
}
