package com.github.nbaars.pwnedpasswords4j.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("pwnedpasswords4j")
public class PwnedPasswordProperties {

    private String userAgent;
    private String url = "https://api.pwnedpasswords.com/range/";

    public String getUserAgent() {
        return userAgent;
    }

    public String getUrl() {
        return url;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
