package com.device.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.device.dto.PendingCommand;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceSimulator {

        private final AcknowledgeService acknowledgeService;

        @Async
        public void simulateAck(
                        String companyId,
                        String deviceId,
                        PendingCommand pending) {

                String payload = """
                                {
                                  "cmd":"%s",
                                  "val":"%s",
                                  "sts":"SUCCESS",
                                  "cmi":"%s",
                                  "vrs":%d
                                }
                                """.formatted(
                                pending.getCmd(),
                                pending.getVal(),
                                pending.getCmi(),
                                pending.getVrs());

                try {
                        Thread.sleep(300);
                        acknowledgeService.processAck(companyId, deviceId, payload);
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
}
