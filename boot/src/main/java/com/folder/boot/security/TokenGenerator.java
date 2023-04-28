package com.folder.boot.security;

import java.security.Key;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.folder.boot.dto.HeaderDTO;
import com.folder.boot.dto.ResultDTO;
import com.folder.boot.dto.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
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
  @Autowired KeyUtils keyUtils;

  private String tokenType = "JWT";
  private String algorithm = "HS256";
  private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  public Map<String, Object> setJwtToken(String name) {
    Map<String, Object> resultMap = new HashMap<>();

    User user = User.builder().no(1).name(name).build();

    JwtBuilder builder = Jwts.builder()
      .setHeader(createHeader())
      .setClaims(createClaims(user))
      .signWith(createSignature(), signatureAlgorithm);

    String token = builder.compact();
    resultMap.put("token", AUTH_TYPE.concat(" ").concat(token));
    resultMap.put("state", true);
    return resultMap;
  }

  public Map<String, Object> getJwtInfo(HttpServletRequest request) {
    Map<String, Object> resultMap = new HashMap<>();

    if(isValidToken(request)) {
      String token = getTokenFromHeader(request);

      resultMap.put("state", true);
      resultMap.put("header", getHeaderFromToken(token));
      resultMap.put("payload", getUserFromToken(token));
    } else {
      resultMap.put("state", false);
    }

    return resultMap;
  }

  public boolean isValidToken(HttpServletRequest request) {
    try {
      Claims claims = getClaimsFormToken(getTokenFromHeader(request));
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
    Calendar date = Calendar.getInstance();
    date.add(Calendar.MINUTE, 3);
    return date.getTime();
  }

  private Map<String, Object> createHeader() {
    Map<String, Object> header = new HashMap<String, Object>();
    header.put("typ", tokenType);
    header.put("alg", algorithm);
    return header;
  }

  private Claims createClaims(User user) {
    return Jwts.claims()
      .setIssuer("DevJWT")
      .setSubject("User")
      .setAudience(keyUtils.encodeContent(setContent(user)))
      .setExpiration(createExpiredDate())
      .setIssuedAt(Calendar.getInstance().getTime());
  }

  private Key createSignature() {
    byte[] jwtKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecretKey);
    return new SecretKeySpec(jwtKeySecretBytes, signatureAlgorithm.getJcaName());
  }

  private String getTokenFromHeader(HttpServletRequest request) {
    String header = request.getHeader(AUTH_HEADER);
    log.info("header : {}", header);
    String[] strArray = header.split(" ");
    if(AUTH_TYPE.equals(strArray[0])) {
      String token = strArray[1];
      log.info("token : {}", token);
      return token;
    }
    return null;
  }

  private HeaderDTO getHeaderFromToken(String token) {
    JwsHeader<?> jwsHeader = Jwts.parserBuilder()
      .setSigningKey(createSignature()).build()
      .parseClaimsJws(token).getHeader();
    return HeaderDTO.builder()
      .type(jwsHeader.getType())
      .algorithm(jwsHeader.getAlgorithm())
      .build();
  }

  private Claims getClaimsFormToken(String token) {
    return Jwts.parserBuilder()
    .setSigningKey(createSignature()).build()
    .parseClaimsJws(token).getBody();
  }

  private User getUserFromToken(String token) {
    Claims claims = getClaimsFormToken(token);
    return getContent(keyUtils.decodeContent(claims.getAudience()));
  }

}
