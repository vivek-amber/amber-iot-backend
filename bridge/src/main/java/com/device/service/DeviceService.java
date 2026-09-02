package com.device.service;

import org.springframework.stereotype.Service;

import com.device.dto.CommandDto;
import com.device.dto.PendingCommand;
import com.device.helpers.DataFormatter;
import com.device.helpers.TopicFormatter;
import com.device.mapper.Mapper;
import com.device.mqtt.MqttPublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceService {
    
    private final MqttPublisher mqttPublisher;
    private final RedisService redisService;
    private final Mapper mapper;
    private final CommandIdGenerator commandIdGenerator;
    private final ShadowService shadowService;
    private final DeviceSimulator deviceSimulator;

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

        String data = DataFormatter.commandValue(pendingCommand.getCmi(), pendingCommand.getVrs(), request.getCmd(),
                request.getVal());

        mqttPublisher.publish(topic, data);

        // For testing only
        deviceSimulator.simulateAck(request.getCompanyId(), request.getDeviceId(), pendingCommand);
    }
    
}
