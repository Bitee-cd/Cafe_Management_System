package com.bitee.Cafe_Management_System.Otp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("api/v1/otp")
public interface OtpRest {

    @PostMapping("/verify")
    ResponseEntity<String> verifyOtp(Map<String, String> requestMap);
}
