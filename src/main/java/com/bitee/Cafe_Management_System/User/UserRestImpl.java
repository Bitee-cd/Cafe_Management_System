package com.bitee.Cafe_Management_System.User;

import com.bitee.Cafe_Management_System.Otp.OtpService;
import com.bitee.Cafe_Management_System.constants.CafeConstants;

import com.bitee.Cafe_Management_System.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserRestImpl implements UserRest {
    @Autowired
    OtpService otpService;
    @Autowired
    UserService userService;
    @Override
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
      try{
          return userService.signUp(requestMap);

      }catch(Exception e){
          e.printStackTrace();
      }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try{
            return userService.login(requestMap);
        }catch (Exception ex){
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        try{
            return userService.getAllUsers();
        }catch (Exception ex){
           ex.printStackTrace();
        }
        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
       try{
            return userService.update(requestMap);
       }catch (Exception e){
           e.printStackTrace();
       }
       return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        try{
            return userService.checkToken();
        }catch(Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try{
            return userService.changePassword(requestMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try{
            return userService.forgotPassword(requestMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> verifyOtp(Map<String, String> requestMap) {
        try{
            return otpService.verifySignUpOtp(requestMap);
        }catch(Exception e ){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> activate(Map<String, String> requestMap) {
        try{ return userService.activateUser(requestMap);}
        catch(Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
