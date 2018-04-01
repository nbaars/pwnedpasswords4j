package com.github.nbaars.pwnedpasswords4j.spring;

import com.github.nbaars.pwnedpasswords4j.client.PwnedPasswordChecker;
import com.github.nbaars.pwnedpasswords4j.client.PwnedPasswordClient;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PwnedPasswordProperties.class)
public class PwnedPasswordAutoConfiguration {

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

    @Bean
    public PwnedPasswordChecker pwnedPasswordChecker(OkHttpClient okHttpClient, PwnedPasswordProperties properties) {
        return new PwnedPasswordChecker(new PwnedPasswordClient(okHttpClient, properties.getUrl(), properties.getUserAgent()));
    }
}
