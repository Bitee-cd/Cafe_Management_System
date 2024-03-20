package com.bitee.Cafe_Management_System.Otp;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpDao extends JpaRepository<Otp,Integer> {

    Otp findByUserEmail(String email);
    Otp findByOtp(String token);
}
