package com.device.mapper;

import org.springframework.stereotype.Component;

import com.device.dto.CommandDto;
import com.device.dto.PendingCommand;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Mapper {

    public PendingCommand mapCoomandDtoToPendingCommand(CommandDto request, Long commandId, Long version) {

        PendingCommand pendingCommand = new PendingCommand();
        pendingCommand.setCmi(commandId);
        pendingCommand.setVrs(version);
        pendingCommand.setCmd(request.getCmd());
        pendingCommand.setVal(request.getVal());
        pendingCommand.setSentAt(System.currentTimeMillis());
        pendingCommand.setRetryCount(0);
        pendingCommand.setMaxRetry(3);
        pendingCommand.setCompanyId(request.getCompanyId());
        pendingCommand.setDeviceId(request.getDeviceId());

        return pendingCommand;
    }
}
