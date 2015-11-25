package com.mobium.client.api;

/**
 *  on 15.10.15.
 */
public class Info {

    public final String
            appVersion,
            buildId,
            codeRevision,
            platformName,
            protocolVersion,
            apiKey;
    public final boolean isDebug;

    public Info(String appVersion,
                String buildId,
                String codeRevision,
                String platformName,
                String protocolVersion, String apiKey,
                boolean isDebug) {
        this.appVersion = appVersion;
        this.buildId = buildId;
        this.codeRevision = codeRevision;
        this.platformName = platformName;
        this.protocolVersion = protocolVersion;
        this.apiKey = apiKey;
        this.isDebug = isDebug;
    }
}
