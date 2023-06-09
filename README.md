# DevJWT
DevJWT Test Project

### Architecture of JWT Project
<img src="./image/DevJWT.gif" width="1000"></img>

###### java version 확인

```
java --version
```

### Spring Project
<img src="./image/Spring_Initializr.png" width="1000"></img>

> * [Spring initializr](https://start.spring.io/)
> * [Application Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html)

### JSON Web Tokens (JWT)

> * [JWT.io](https://jwt.io/)
> * [JJWT - Github](https://github.com/jwtk/jjwt)

### JWT Dependency

###### Maven
```
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

###### Gradle
```
dependencies {
    implementation 'io.jsonwebtoken:jjwt-api:0.11.5'
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.11.5',
    'io.jsonwebtoken:jjwt-jackson:0.11.5'
}
```

### Quickstart

```
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
String jws = Jwts.builder().setSubject("Joe").signWith(key).compact();
```

### Signed JWT

###### Header

```
{
  "alg": "HS256"
}
```

###### Body

```
{
  "sub": "Joe"
}
```

###### Token

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UifQ.1KP0SsvENi7Uz1oQc07aXTL7kpQG5jBNIybqr60AlD4
```

### Secret Keys

```
SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); //or HS384 or HS512
```


----
### YouTube Link

> * [1. Token 소개](https://youtu.be/lnnCijQuTnQ)
> * [2. Token 구조 및 흐름](https://youtu.be/i5rp3mvFvLA)
> * [3. Token 생성 및 테스트](https://youtu.be/Nn9aXiyOnwk)
> * [4. Token 검증 및 테스트](https://youtu.be/W4xQVo82hhE)
> * [5. RSA 비대칭키 소개](https://youtu.be/fNWFtAFpiuI)
> * [6. RSA 적용하기](https://youtu.be/R29cAazcKqA)
> * [7. 코드 정리 및 마무리](https://youtu.be/wAcYFsyddrg)
