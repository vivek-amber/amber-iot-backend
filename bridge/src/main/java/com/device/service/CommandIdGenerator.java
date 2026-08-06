package com.device.service;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

@Component
public class CommandIdGenerator {

    private final AtomicLong commandId = new AtomicLong(0);

    private final AtomicLong version = new AtomicLong(0);

    public Long nextCommandId() {
        return commandId.incrementAndGet();
    }

    public Long nextVersion() {
        return version.incrementAndGet();
    }
}