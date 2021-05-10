package com.cct.onlineteaching.config.security;

import com.cct.onlineteaching.dao.UserMapper;
import com.cct.onlineteaching.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userMapper.findByAccount(s);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with account %s.", s));
        } else {
            return CustomUserFactory.create(user);
        }
    }
}
