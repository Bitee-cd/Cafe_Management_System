package com.bitee.Cafe_Management_System.Otp;

import com.bitee.Cafe_Management_System.User.User;
import com.bitee.Cafe_Management_System.User.UserDao;
import com.bitee.Cafe_Management_System.constants.CafeConstants;
import com.bitee.Cafe_Management_System.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Slf4j
@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    OtpDao otpDao;
    @Autowired
    UserDao userDao;
    private static final Integer OTP_LENGTH = 6;
    private static final Integer EXPIRY_TIME_IN_MINUTES = 1000 * 60 * 15;

    @Override
    public String generateUserOtp(User user) {
        String random0tp = generateRandomOtp();
        Otp otp = new Otp();
        otp.setToken(random0tp);
        otp.setUser(user);
        otp.setExpiresAt(new Date(System.currentTimeMillis() + EXPIRY_TIME_IN_MINUTES));
        otpDao.save(otp);
        return random0tp;
    }

    @Override
    public ResponseEntity<String> verifySignUpOtp(Map<String, String> requestMap) {
        try {
            String email = requestMap.get("email");
            String otp = requestMap.get("otp");
            User user = userDao.findByEmail(email);
            Otp supposedOtp = otpDao.findByOtp(otp);

            if (user != null && supposedOtp != null && supposedOtp.getUser().equals(user)) {
                if (user.getStatus().equals("false")) {
                    if (supposedOtp.getExpiresAt().after(new Date())) {

                        user.setStatus("true");
                        userDao.save(user);
                        return CafeUtils.getResponseEntity("User successfully activated", HttpStatus.OK);
                    } else return CafeUtils.getResponseEntity("Token is expired", HttpStatus.BAD_REQUEST);
                } else return CafeUtils.getResponseEntity("User already activated", HttpStatus.BAD_REQUEST);
            } else return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String generateRandomOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    private Date calculateExpiryTime(Integer expiry_minutes) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryTime = now.plusMinutes(expiry_minutes);
        return Date.from(expiryTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
