package com.device.router;

import org.springframework.stereotype.Service;

import com.device.enums.MqttTopicType;
import com.device.helpers.TopicFormatter;
import com.device.service.AcknowledgeService;
import com.device.service.DeviceStateService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class MqttMessageRouter {

    private final AcknowledgeService acknowledgeService;
    private final DeviceStateService deviceStateService;


    public void route(String topic, String payload) {
        
        String companyId = TopicFormatter.extractCompanyId(topic);
        String deviceId = TopicFormatter.extractDeviceId(topic);

        switch (MqttTopicType.fromTopic(topic)) {
            
            case ACK -> acknowledgeService.processAck(companyId, deviceId, payload);
            
            case STATUS -> deviceStateService.processStatus(companyId, deviceId, payload);
  
        }
    }
}
