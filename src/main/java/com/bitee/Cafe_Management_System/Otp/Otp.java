package com.bitee.Cafe_Management_System.Otp;

import com.bitee.Cafe_Management_System.User.User;
import jakarta.persistence.*;
import jdk.jfr.Name;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@NamedQuery(name = "Otp.findByUserEmail", query="SELECT o FROM Otp o WHERE o.user.email = :email")
@NamedQuery(name="Otp.findByOtp",query="SELECT o FROM Otp o WHERE o.token = :token")

@Entity
@Data
@Table(name="otp")
@DynamicInsert
@DynamicUpdate
public class Otp {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer Id;

    @Column(name="token")
    private String token;

    @CreationTimestamp
    @Column(name="created_at")
    private Date createdAt;

    @Column(name="expires_at")
    private Date expiresAt;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    private User user;
}
