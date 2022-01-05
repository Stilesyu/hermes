package com.github.www.hermes.common;


public enum ApiKeys {
    HEARTBEAT((short) 0, "Heartbeat", (short) 1, (short) 1);


    ApiKeys(short code, String name, short lowestSupportVersion, short highestSupportVersion) {
        this.code = code;
        this.name = name;
        this.lowestSupportVersion = lowestSupportVersion;
        this.highestSupportVersion = highestSupportVersion;
    }

    private final short code;
    private final String name;
    private final short lowestSupportVersion;
    private final short highestSupportVersion;

    public short getCode() {
        return code;
    }
    public String getName() {
        return name;
    }

    public short getLowestSupportVersion() {
        return lowestSupportVersion;
    }

    public short getHighestSupportVersion() {
        return highestSupportVersion;
    }
}
