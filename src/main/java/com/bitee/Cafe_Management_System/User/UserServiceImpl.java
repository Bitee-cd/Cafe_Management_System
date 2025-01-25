package com.bitee.Cafe_Management_System.User;

import com.bitee.Cafe_Management_System.JWT.CustomUsersDetailsService;
import com.bitee.Cafe_Management_System.JWT.JWTFilter;
import com.bitee.Cafe_Management_System.JWT.JwtUtil;
import com.bitee.Cafe_Management_System.Otp.Otp;
import com.bitee.Cafe_Management_System.Otp.OtpService;
import com.bitee.Cafe_Management_System.constants.CafeConstants;
import com.bitee.Cafe_Management_System.utils.CafeUtils;
import com.bitee.Cafe_Management_System.utils.EmailUtils;
import com.bitee.Cafe_Management_System.User.UserWrapper;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JWTFilter jwtFilter;
    @Autowired
    CustomUsersDetailsService customUsersDetailsService;

    @Autowired
    OtpService otpService;

    @Autowired
    EmailUtils emailUtils;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userDao.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    user = getUserFromMap(requestMap); // Create user entity
                    userDao.save(user);
                    //generate userOtp and send an email
                    String userOtp = otpService.generateUserOtp(user);
                    log.info("Generated otp");
                    emailUtils.sendOtpMail(user.getEmail(), "Cafe Management:Verify your Account", userOtp);
                    log.info("Sent user otp from userservice");
                    return CafeUtils.getResponseEntity("Successfully Registered", HttpStatus.CREATED);
                } else return CafeUtils.getResponseEntity("Email Already exists", HttpStatus.BAD_REQUEST);
            } else return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("inside login {}", requestMap);
        log.info(requestMap.get("email"));

        try {
            if (validateLoginMap(requestMap)) {
                Authentication auth = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
                );

                if (auth.isAuthenticated()) {
                    if (customUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                        log.info("gooten here");
                        String token = jwtUtil.generateToken(
                                customUsersDetailsService.getUserDetail().getEmail(),
                                customUsersDetailsService.getUserDetail().getRole()
                        );


                        return new ResponseEntity<String>("{\"token\":\"" + token + "\"}", HttpStatus.OK);
                    } else {
                        return CafeUtils.getResponseEntity("Wait for admin Approval", HttpStatus.BAD_REQUEST);
                    }
                }
            } else return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        try {
            if (jwtFilter.isAdmin()) {
                return new ResponseEntity<>(userDao.getAllUser(), HttpStatus.OK);
            } else return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<User> maybeUser = userDao.findById(Integer.parseInt(requestMap.get("id")));
                if (!maybeUser.isEmpty()) {
                    userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"), maybeUser.get().getEmail(), userDao.getAllAdmin());
                    return CafeUtils.getResponseEntity("User status updated successfully", HttpStatus.OK);
                } else return CafeUtils.getResponseEntity("User not found", HttpStatus.NOT_FOUND);
            } else return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        try {
            return CafeUtils.getResponseEntity("OK", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User user = userDao.findByEmail(jwtFilter.getCurrentUser());
            if (!user.equals(null)) {
                if (user.getPassword().equals(requestMap.get("oldPassword"))) {
                    user.setPassword(requestMap.get("newPassword"));
                    userDao.save(user);
                    return CafeUtils.getResponseEntity("password updated Successfully", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Incorrect old Password", HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userDao.findByEmail(requestMap.get("email"));
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())) {
                emailUtils.forgetMail(user.getEmail(), "Credentials by Cafe Management", user.getPassword());
            }
            return CafeUtils.getResponseEntity("Check for mail for credentials", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> verifyOtp(Map<String, String> requestMap) {
        try {
            //TODO implement this
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> activateUser(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<User> maybeUser = userDao.findById(Integer.parseInt(requestMap.get("id")));
                if (!maybeUser.isEmpty()) {
                } else return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.NOT_FOUND);

            } else return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {

        allAdmin.remove(jwtFilter.getCurrentUser());
        if (status != null && status.equalsIgnoreCase("true")) {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account approved", "User: " + user + "\n is approved " +
                    "by \n ADMIN:" + jwtFilter.getCurrentUser(), allAdmin);
        } else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account disabled", "User: " + user + "\n is disabled " +
                    "by \n ADMIN:" + jwtFilter.getCurrentUser(), allAdmin);
        }
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        return requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password");
    }

    private boolean validateLoginMap(Map<String, String> requestMap) {
        return requestMap.containsKey("email") && requestMap.containsKey("password");
    }

    private User getUserFromMap(Map<String, String> requestMap) {
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