package com.device.service;

import org.springframework.stereotype.Service;

import com.device.dto.CommandDto;
import com.device.dto.PendingCommand;
import com.device.helpers.DataFormatter;
import com.device.helpers.TopicFormatter;
import com.device.mapper.Mapper;
import com.device.mqtt.MqttPublisher;
import com.device.proto.DeviceCommand;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceService {
    
    private final MqttPublisher mqttPublisher;
    private final RedisService redisService;
    private final Mapper mapper;
    private final CommandIdGenerator commandIdGenerator;
    private final ShadowService shadowService;

    public void sendCommand(CommandDto request) {

            Long commandId = commandIdGenerator.nextCommandId();

            Long version = shadowService.nextVersion(
                            request.getDeviceId(),
                            request.getCmd());

            shadowService.updateDesiredState(
                            request.getDeviceId(),
                            request.getCompanyId(),
                            request.getCmd(),
                            request.getVal(),
                            version);

            PendingCommand pendingCommand = mapper.mapCoomandDtoToPendingCommand(request, commandId, version);

            redisService.savePendingCommand(request.getDeviceId(), request.getCmd(), pendingCommand);

            String topic = TopicFormatter.commandTopic(request.getCompanyId(), request.getDeviceId());

            DeviceCommand data = DataFormatter.commandValue(pendingCommand.getCmi(), pendingCommand.getVrs(), request.getCmd(),
                           request.getVal());
            
          
            mqttPublisher.publish(topic, data);

    }
    
}
