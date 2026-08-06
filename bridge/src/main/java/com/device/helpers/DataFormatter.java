package com.device.helpers;

public final class DataFormatter {
    

    private DataFormatter() {
    }
    
    public static String commandValue(Long cmdId,Long version,String cmd,Object val) {

        return String.format("{\"cmi\":%d,\"vrs\":%d,\"cmd\":\"%s\",\"val\":\"%s\"}",
            cmdId,version,cmd,val);
    }
}
