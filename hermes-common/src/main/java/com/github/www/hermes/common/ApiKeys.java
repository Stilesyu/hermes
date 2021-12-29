package com.github.www.hermes.common;


public enum ApiKeys {
    HEARTBEAT((short) 0, "Heartbeat", (short) 1);


    ApiKeys(short code, String name, short version) {
        this.code = code;
        this.name = name;
        this.version = version;
    }

    private final short code;
    private final String name;
    private final short version;

    public short getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
