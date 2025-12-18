package com.ensa.SprintFlow.builder;

import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ErrorResponseBuilder {
  private String error = "";
  private String message = "";
  private HttpStatus status = HttpStatus.OK;
  private ArrayList<String> details = new ArrayList<>();

  public ErrorResponseBuilder error(String error) {
    this.error = error;
    return this;
  }

  public ErrorResponseBuilder message(String message) {
    this.message = message;
    return this;
  }

  public ErrorResponseBuilder status(HttpStatus status) {
    this.status = status;
    return this;
  }

  public ErrorResponseBuilder details(ArrayList<String> details) {
    this.details.addAll(details);
    return this;
  }

  public ErrorResponseBuilder detail(String detail) {
    this.details.add(detail);
    return this;
  }

  public ResponseEntity<?> build() {
    HashMap<String, Object> body = new HashMap<>();
    body.put("error", this.error);
    body.put("message", this.message);
    body.put("status", this.status.value());
    body.put("details", this.details);

    return ResponseEntity.status(this.status).body(body);
  }
}
