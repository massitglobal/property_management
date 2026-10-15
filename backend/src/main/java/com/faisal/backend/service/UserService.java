package com.faisal.backend.service;

import com.faisal.backend.model.UserInformation;

public interface UserService {

    //CRUDS USER
    public UserInformation save(UserInformation userInformation);
    public UserInformation update(UserInformation userInformation);


    public UserInformation getUserByUsername(String username);
    public UserInformation getUserByEmail(String email);
    public UserInformation getUserByUUID(String uuid);


}
