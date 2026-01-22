package com.ensa.SprintFlow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SprintFlowApplication {

  public static void main(String[] args) {
    SpringApplication.run(SprintFlowApplication.class, args);
  }
}
