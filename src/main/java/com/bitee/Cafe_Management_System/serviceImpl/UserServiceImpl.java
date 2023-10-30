package com.bitee.Cafe_Management_System.serviceImpl;

import com.bitee.Cafe_Management_System.constants.CafeConstants;
import com.bitee.Cafe_Management_System.dao.UserDao;
import com.bitee.Cafe_Management_System.model.User;
import com.bitee.Cafe_Management_System.service.UserService;
import com.bitee.Cafe_Management_System.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup", requestMap);
try{
        if (validateSignUpMap(requestMap)) {
            User user = userDao.findByEmailId(requestMap.get("email"));
            if (Objects.isNull(user)) {
                userDao.save(getUserFromMap(requestMap));
                return CafeUtils.getResponseEntity("Successfully Registered", HttpStatus.CREATED);
            } else return CafeUtils.getResponseEntity("Email Already exists", HttpStatus.BAD_REQUEST);
        } else return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
    }
catch (Exception e) {
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,
            HttpStatus.INTERNAL_SERVER_ERROR);
}
    }
    private boolean validateSignUpMap(Map<String,String> requestMap){
       return requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password");
    }

    private User getUserFromMap(Map<String,String> requestMap){
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }
}
