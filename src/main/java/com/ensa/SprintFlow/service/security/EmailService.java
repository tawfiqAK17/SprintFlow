package com.ensa.SprintFlow.service.security;

import com.ensa.SprintFlow.exception.generalException.EmailConditionsException;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.model.security.VerificationCode;
import com.ensa.SprintFlow.repository.security.VerificationCodeRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  private VerificationCodeRepository verificationCodeRepository;
  private JavaMailSender javaMailSender;

  EmailService(
      VerificationCodeRepository verificationCodeRepository, JavaMailSender javaMailSender) {
    this.verificationCodeRepository = verificationCodeRepository;
    this.javaMailSender = javaMailSender;
  }

  public void validateConditions(String email) throws EmailConditionsException {
    if (!email.contains("@")) {
      throw new EmailConditionsException();
    }
  }

  public void sendVerificationEmail(User user) {
    // generate the verification code
    String verificationCode = generateVerificationCode();
    // create the message
    SimpleMailMessage message = generateMessage(user.getEmail(), verificationCode);
    // send the message
    javaMailSender.send(message);
    // saving the verification code in the db with the user id
    VerificationCode verificationCodeEntity = new VerificationCode();
    verificationCodeEntity.setUser(user);
    verificationCodeEntity.setCode(verificationCode);
    // the code expired after 10 minutes
    verificationCodeEntity.setExpirationDate(LocalDateTime.now().plusMinutes(10));
    System.out.println(user.getId() + "+++++++++++++++++++++++");
    verificationCodeRepository.save(verificationCodeEntity);
  }

  private SimpleMailMessage generateMessage(String destinationEmail, String verificationCode) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(destinationEmail);
    message.setSubject("Verification code");
    message.setText("your verification code is: " + verificationCode);
    return message;
  }

  private String generateVerificationCode() {
    Random random = new Random();
    Integer code = 100000 + random.nextInt(900000);
    return code.toString();
  }

  public static ArrayList<String> getConditions() {
    ArrayList<String> details = new ArrayList<>();
    details.add("the email should contain '@'");
    return details;
  }
}
