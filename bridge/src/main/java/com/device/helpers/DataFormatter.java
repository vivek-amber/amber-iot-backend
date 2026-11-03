package com.device.helpers;

import com.device.proto.DeviceCommand;

public final class DataFormatter {
    

    private DataFormatter() {
    }
    
    public static DeviceCommand commandValue(Long cmdId,Long version,String cmd,Integer val) {

        DeviceCommand.Builder builder = DeviceCommand.newBuilder()
                .setCmi(cmdId)
                .setVrs(version)
                .setCmd(cmd)
                .setVal(val);

        return builder.build();
    }
}
