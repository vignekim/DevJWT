package com.folder.boot.security;

import java.security.Key;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.folder.boot.dto.ClaimsDTO;
import com.folder.boot.dto.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoder;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenGenerator {

  @Value(value = "${access.auth.header}")
  private String AUTH_HEADER;

  @Value(value = "${access.auth.type}")
  private String AUTH_TYPE;

  @Value(value = "${access.auth.jwt}")
  private String jwtSecretKey;

  @Autowired ObjectMapper objectMapper;

  private String tokenType = "JWT";
  private String algorithm = "HS256";
  private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  public Map<String, Object> setJwtToken(String name) {
    Map<String, Object> resultMap = new HashMap<>();

    String base64 = Base64.getEncoder().encodeToString(name.getBytes());
    Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64));

    JwtBuilder builder = Jwts.builder()
      .setIssuer("DevJWT")
      .setSubject("1")
      .setAudience(name)
      .setExpiration(Calendar.getInstance().getTime())
      .setIssuedAt(Calendar.getInstance().getTime())
      .signWith(key, signatureAlgorithm);

    String token = builder.compact();
    resultMap.put("token", AUTH_TYPE.concat(" ").concat(token));
//  resultMap.put("token", AUTH_TYPE.concat(" ").concat(name));
    resultMap.put("state", true);
    return resultMap;
  }

  public Map<String, Object> getJwtInfo(Map<String, Object> paramMap) {
    Map<String, Object> resultMap = new HashMap<>();
    Map<String, String> headerMap = new HashMap<>();

    Iterator<String> keys = paramMap.keySet().iterator();
    while (keys.hasNext()) {
      String key = keys.next();
      log.info("{} : {}", key, paramMap.get(key));
    }

    headerMap.put("type", tokenType);
    headerMap.put("algorithm", algorithm);

    resultMap.put("state", true);
    resultMap.put("header", headerMap);
    resultMap.put("payload", User.builder().no(1).name("폴더").build());

    return resultMap;
  }

  public boolean isValidToken(String token) {
    try {
      Claims claims = getClaimsFormToken(getTokenFromHeader(token));
      log.info("============================================");
      log.info("|expireTime\t: {}|", claims.getExpiration());
      log.info("|realTime\t: {}|", Calendar.getInstance().getTime());
      log.info("============================================");
      return true;
    } catch (ExpiredJwtException exception) {
      log.info("==============");
      log.error("Token Expired");
    } catch (JwtException exception) {
      log.info("==============");
      log.error("Token Tampered");
    } catch (NullPointerException exception) {
      log.info("==============");
      log.error("Token is null");
    }
    log.info("==============");
    return false;
  }

  private String setContent(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return "";
  }

  private User getContent(String content) {
    try {
      return objectMapper.readValue(content, new TypeReference<User>() {});
    } catch(JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  private Date createExpiredDate() {
    return null;
  }

  private Map<String, Object> createHeader() {
    return null;
  }

  private Map<String, Object> createClaims(User user) {
    return null;
  }

  private Key createSignature(SignatureAlgorithm signatureAlgorithm) {
    return null;
  }

  private String getTokenFromHeader(String header) {
    return header.split(" ")[1];
  }

  private Claims getClaimsFormToken(String token) {
    return null;
  }

  private User getUserFromToken(String token) {
    return null;
  }

  private ClaimsDTO parseTokenToUserInfo(String token) {
    return null;
  }

}
