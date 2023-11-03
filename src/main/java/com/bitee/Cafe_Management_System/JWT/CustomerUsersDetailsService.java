package com.bitee.Cafe_Management_System.JWT;

import com.bitee.Cafe_Management_System.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class CustomerUsersDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    private com.bitee.Cafe_Management_System.model.User  userDetail;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Inside loadByUsername {}",email);
       userDetail = userDao.findByEmailId(email);
       if(!Objects.isNull(userDetail)){
           return new User(userDetail.getEmail(),userDetail.getPassword(),new ArrayList<>());
       }else{
           throw new UsernameNotFoundException("user not found");
       }
    }

    public com.bitee.Cafe_Management_System.model.User getUserDetail(){
        return userDetail;
    }
}
