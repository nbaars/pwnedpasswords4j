package com.github.nbaars.pwnedpasswords4j.demo;

import com.github.nbaars.pwnedpasswords4j.client.PwnedPasswordChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

@RestController
public class SignupController {

    @Autowired
    private PwnedPasswordChecker checker;

    @PostMapping("/signup")
    public boolean login(@RequestBody Login login) {
        return checker.check(login.getPassword());
    }

    @PostMapping("/signup/async")
    public Future<Boolean> asynLogin(@RequestBody Login login) {
        return checker.asyncCheck(login.getPassword());
    }
}
