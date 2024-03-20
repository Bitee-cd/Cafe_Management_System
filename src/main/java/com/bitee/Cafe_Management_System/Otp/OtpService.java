package com.bitee.Cafe_Management_System.Otp;

import com.bitee.Cafe_Management_System.User.User;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface OtpService {
   String generateUserOtp(User user);

   ResponseEntity<String> verifySignUpOtp(Map<String,String> requestMap);
}
