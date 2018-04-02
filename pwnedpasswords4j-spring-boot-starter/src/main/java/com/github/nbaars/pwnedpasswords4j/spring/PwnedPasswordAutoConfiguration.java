package com.github.nbaars.pwnedpasswords4j.spring;

import com.github.nbaars.pwnedpasswords4j.client.PwnedPasswordChecker;
import com.github.nbaars.pwnedpasswords4j.client.PwnedPasswordClient;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@EnableConfigurationProperties(PwnedPasswordProperties.class)
public class PwnedPasswordAutoConfiguration {

    private final PwnedPasswordProperties properties;

    public PwnedPasswordAutoConfiguration(PwnedPasswordProperties properties) {
        Objects.requireNonNull(properties.getUrl(), "Please specify pwnedpasswords4j.url in application.properties");
        Objects.requireNonNull(properties.getUserAgent(), "Please specify pwnedpasswords4j.user_agent in application.properties");

        this.properties = properties;
    }

    public PwnedPasswordProperties getProperties() {
        return properties;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

    @Bean
    public PwnedPasswordChecker pwnedPasswordChecker(OkHttpClient okHttpClient, PwnedPasswordProperties properties) {
        return new PwnedPasswordChecker(new PwnedPasswordClient(okHttpClient, properties.getUrl(), properties.getUserAgent()));
    }
}
