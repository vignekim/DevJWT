package com.folder.boot.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.folder.boot.security.TokenGenerator;

@RestController
public class TokenController {

  @Autowired private TokenGenerator tokenGenerator;

  @GetMapping("/authorize")
  public Map<String, Object> authorize(@RequestParam("name") String name) {
    return tokenGenerator.setJwtToken(name);
  }

  @PostMapping("/verification")
  public Map<String, Object> verification(@RequestBody Map<String, Object> paramMap) {
    return tokenGenerator.getJwtInfo(paramMap);
  }

}
