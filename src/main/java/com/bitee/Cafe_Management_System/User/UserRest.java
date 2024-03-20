package com.bitee.Cafe_Management_System.User;

import com.bitee.Cafe_Management_System.User.UserWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "api/v1/auth")
public interface UserRest {
    @PostMapping(path ="/signup")
    ResponseEntity<String> signup(@RequestBody(required = true) Map<String,String> requestMap);

    @PostMapping(path="/login")
   ResponseEntity<String> login(@RequestBody(required = true) Map<String,String> requestMap);

    @GetMapping(path="/get")
    ResponseEntity<List<UserWrapper>> getAllUsers();

    @PostMapping(path="/update")
     ResponseEntity<String> update(@RequestBody(required = true) Map<String,String> requestMap);

    @GetMapping(path="/check-token")
   ResponseEntity<String> checkToken();

    @PostMapping(path="/change-password")
     ResponseEntity<String> changePassword(@RequestBody Map<String,String> requestMap);

    @PostMapping(path="/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String,String>requestMap);

    @PostMapping(path="/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String,String> requestMap);
}
