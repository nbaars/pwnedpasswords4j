# Java client for pwnedpasswords.com

## Introduction

A Java client for checking a password against pwnedpasswords.com using the `Searching by range` API
For more details see: https://haveibeenpwned.com/API/v2#SearchingPwnedPasswordsByRange

## Pure Java client

The artifact `client` can be used in a standalone Java program and does not rely on Spring Boot
To use the checker you need to add the following library to the `pom.xml`:

```
<dependency>
  <groupId>org.pwnedpassword4j</groupId>
  <artifactId>client</artifactId>
  <version>${project.version}</version>
</dependency>

```

In the code you can check a password as follows:

```
PwnedPasswordChecker checker = PwnedPasswordChecker.standalone("My user agent")
boolean result = checker.check("password");
```

The user-agent is necessary to specify as described in the API description at haveibeenpwned.com.

## Spring Boot autoconfigure

For Spring Boot there is an autoconfigure module, to use this use the following dependency inside your project:

```
<dependency>
  <groupId>org.pwnedpassword4j</groupId>
  <artifactId>pwnedpasswords-spring-boot-starter</artifactId>
  <version>${project.version}</version>
</dependency>
``` 

In the application.properties you should add:

```
pwnedpasswords4j.user_agent=Testing   # Required as described in the documentation of haveibeenpwned.com API
pwnedpasswords4j.url=https://api.pwnedpasswords.com/range/ # Optional
```

Wire up the checker as follows: 

```
 @Autowired
 private PwnedPasswordChecker checker;
```

As an example see the demo project:

```
@RestController
public class SignupController {

    @Autowired
    private PwnedPasswordChecker checker;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody Login login) {
        if (checker.check("password")) {
            return ResponseEntity.badRequest().body("Consider changing your password");
        }
        return ResponseEntity.ok().build();
    }
}
```




