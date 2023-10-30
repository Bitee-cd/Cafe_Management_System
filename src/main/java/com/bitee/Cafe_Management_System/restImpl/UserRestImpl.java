package com.bitee.Cafe_Management_System.restImpl;

import com.bitee.Cafe_Management_System.constants.CafeConstants;
import com.bitee.Cafe_Management_System.rest.UserRest;
import com.bitee.Cafe_Management_System.service.UserService;
import com.bitee.Cafe_Management_System.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserRestImpl implements UserRest {

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
}
