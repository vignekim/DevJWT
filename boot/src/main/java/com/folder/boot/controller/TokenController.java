package com.folder.boot.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

  private Map<String, Object> resultMap;

  @GetMapping("/authorize")
  public Map<String, Object> authorize(@RequestParam("key") String key) {
    resultMap = new HashMap<>();
    resultMap.put("key", key);
    resultMap.put("token", "bearer 1A2b3C4d");
    resultMap.put("state", true);
    return resultMap;
  }

  @PostMapping("/verification")
  public Map<String, Object> verification(@RequestBody Map<String, Object> paramMap) {
    resultMap = new HashMap<>();
    Map<String, String> headerMap = new HashMap<>();
    Map<String, String> payloadMap = new HashMap<>();
    resultMap = paramMap;
    headerMap.put("algorithm", "HS256");
    headerMap.put("type", "JWT");

    payloadMap.put("name", "폴더");

    resultMap.put("state", true);
    resultMap.put("header", headerMap);
    resultMap.put("payload", payloadMap);
    return resultMap;
  }

}
