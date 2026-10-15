package com.faisal.backend.service;

import com.faisal.backend.model.UserInformation;
import com.faisal.backend.repository.UserRepository;
import com.faisal.backend.util.CustomRandomGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService{


    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserInformation save(UserInformation userInformation) {

        String guid = CustomRandomGenerator.generateUUID();
        userInformation.setGeneratedUUID(guid);

        userInformation.setStatus('A');
        userInformation.setCreatedDate(LocalDateTime.now());
        userInformation.setCreatedBy(guid);

        return userRepository.save(userInformation);
    }

    @Override
    public UserInformation update(UserInformation userInformation) {

        //old information
        UserInformation old =  userRepository.findByGeneratedUUID(userInformation.getGeneratedUUID());

        //copy
        BeanUtils.copyProperties(userInformation,old, "setCreatedBy", "setCreatedDate","setModifiedBy","setModifiedDate");

        return userRepository.save(old);
    }

    @Override
    public UserInformation getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public UserInformation getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Override
    public UserInformation getUserByUUID(String uuid) {
        return userRepository.findByGeneratedUUID(uuid);
    }
}
