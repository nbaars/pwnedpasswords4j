package org.pwnedpasswords4j.spring;

import okhttp3.OkHttpClient;
import org.pwnedpasswords4j.client.PwnedPasswordChecker;
import org.pwnedpasswords4j.client.PwnedPasswordClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.WebClientAutoConfiguration.RestTemplateConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PwnedPasswordProperties.class)
@ConditionalOnClass(RestTemplateConfiguration.class)
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
