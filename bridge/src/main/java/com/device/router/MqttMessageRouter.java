package com.device.router;

import org.springframework.stereotype.Service;

import com.device.enums.MqttTopicType;
import com.device.helpers.TopicFormatter;
import com.device.proto.DeviceAck;
import com.device.proto.DeviceError;
import com.device.proto.DeviceState;
import com.device.proto.DeviceStatus;
import com.device.service.AcknowledgeService;
import com.device.service.DeviceErrorService;
import com.device.service.DeviceStateService;
import com.google.protobuf.InvalidProtocolBufferException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MqttMessageRouter {

    private final AcknowledgeService acknowledgeService;
    private final DeviceStateService deviceStateService;
    private final DeviceErrorService deviceErrorService;

    public void route(String topic, byte[] payload) {
        
        String companyId = TopicFormatter.extractCompanyId(topic);
        String deviceId = TopicFormatter.extractDeviceId(topic);

        try {
            switch (MqttTopicType.fromTopic(topic)) {

                case CMD -> {}

                case ACK -> {
                    DeviceAck ack = DeviceAck.parseFrom(payload);
                    acknowledgeService.processAck(companyId, deviceId, ack);
                }

                case STATE -> {
                    DeviceState state = DeviceState.parseFrom(payload);
                    deviceStateService.processState(companyId, deviceId, state);
                }

                case STATUS -> {
                    DeviceStatus status = DeviceStatus.parseFrom(payload);
                    deviceStateService.processStatus(companyId, deviceId, status);
                }
                case ERROR -> {

                    DeviceError error = DeviceError.parseFrom(payload);

                    deviceErrorService.processError(
                            companyId,
                            deviceId,
                            error);
                }
            }
        } catch (InvalidProtocolBufferException e) {
            log.error("Error while parsing protobuf", e);
        }

    }
}
