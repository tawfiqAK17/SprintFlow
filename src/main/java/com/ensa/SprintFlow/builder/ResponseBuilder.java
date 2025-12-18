package com.ensa.SprintFlow.builder;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseBuilder {

  private Map<String, Object> body = new HashMap<>();
  private HttpStatus status;

  public ResponseBuilder property(String key, Object value) {
    body.put(key, value);
    return this;
  }

  public ResponseBuilder status(HttpStatus status) {
    this.status = status;
    return this;
  }

  public ResponseEntity<?> build() {
    return ResponseEntity.status(status).body(body);
  }
}
