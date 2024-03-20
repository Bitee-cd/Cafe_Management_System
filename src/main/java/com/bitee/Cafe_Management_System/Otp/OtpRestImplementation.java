package com.bitee.Cafe_Management_System.Otp;

import com.bitee.Cafe_Management_System.User.UserDao;
import com.bitee.Cafe_Management_System.constants.CafeConstants;
import com.bitee.Cafe_Management_System.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class OtpRestImplementation implements  OtpRest{

    @Autowired
    OtpService otpService;

    @Override
    public ResponseEntity<String> verifyOtp(Map<String, String> requestMap) {
        try{
            return otpService.verifySignUpOtp(requestMap);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
