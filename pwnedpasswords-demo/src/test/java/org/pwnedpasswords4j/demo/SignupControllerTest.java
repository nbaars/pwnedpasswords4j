package org.pwnedpasswords4j.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignupControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void wrongPasswordShouldGiveError() {
        Login login = new Login("username", "password");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/signup/async", login, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("true");
    }

    @Test
    public void complexPasswordShouldNotBeFound() {
        Login login = new Login("username", "#234Ffds!##!@#F#3432sddg");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/signup", login, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}