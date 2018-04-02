package com.github.nbaars.pwnedpasswords4j.spring;

import com.github.nbaars.pwnedpasswords4j.client.PwnedPasswordChecker;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class PwnedPasswordAutoConfigurationTest {

    private AnnotationConfigApplicationContext context;

    @After
    public void tearDown() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Test(expected = BeanCreationException.class)
    public void shouldGiveErrorWhenUserAgentIsMissing() {
        this.context = load(PwnedPasswordAutoConfiguration.class);

        this.context.getBean(PwnedPasswordAutoConfiguration.class);
    }

    @Test
    public void shouldWireWithDefaultURL() {
        this.context = load(PwnedPasswordAutoConfiguration.class, "pwnedpasswords4j.user_agent=UnitTest");

        PwnedPasswordAutoConfiguration bean = this.context.getBean(PwnedPasswordAutoConfiguration.class);
        assertNotNull(bean);
        assertEquals("https://api.pwnedpasswords.com/range/", bean.getProperties().getUrl());
        assertEquals("UnitTest", bean.getProperties().getUserAgent());
        assertNotNull(bean.okHttpClient());

        PwnedPasswordChecker checker = this.context.getBean(PwnedPasswordChecker.class);
        assertNotNull(checker);
    }

    @Test
    public void shouldWireWithSpecifiedURL() {
        this.context = load(PwnedPasswordAutoConfiguration.class, "pwnedpasswords4j.user_agent=UnitTest", "pwnedpasswords4j.url=http://unittest.org");

        PwnedPasswordAutoConfiguration bean = this.context.getBean(PwnedPasswordAutoConfiguration.class);
        assertNotNull(bean);
        assertEquals("http://unittest.org", bean.getProperties().getUrl());
        assertEquals("UnitTest", bean.getProperties().getUserAgent());
        assertNotNull(bean.okHttpClient());

        PwnedPasswordChecker checker = this.context.getBean(PwnedPasswordChecker.class);
        assertNotNull(checker);
    }

    private static AnnotationConfigApplicationContext load(Class<?> config, String... environment) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        EnvironmentTestUtils.addEnvironment(applicationContext, environment);
        applicationContext.register(config);
        applicationContext.refresh();
        return applicationContext;
    }
}