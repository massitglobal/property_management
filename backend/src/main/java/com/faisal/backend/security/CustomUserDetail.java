package com.faisal.backend.security;

import com.faisal.backend.model.UserInformation;
import com.faisal.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetail implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetail(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserInformation u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        //setting from db to internal Spring User and pass back the info to authentication
        return User.withUsername(u.getUsername())
                .password(u.getPasswordHash())
                .disabled(!u.getEnabled())
                .roles(u.getRoles().toArray(String[]::new))
                .build();
    }
}
