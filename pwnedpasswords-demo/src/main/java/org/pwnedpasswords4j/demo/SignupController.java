package org.pwnedpasswords4j.demo;

import org.pwnedpassword4j.client.PwnedPasswordChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("signup")
public class SignupController {

    @Autowired
    private PwnedPasswordChecker checker;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody Login login) {
        if (checker.check(login.getPassword())) {
            return ResponseEntity.badRequest().body("Consider changing your password");
        }
        return ResponseEntity.ok().build();
    }
}
