package com.faisal.backend.service;

import com.faisal.backend.model.UserProperty;
import org.springframework.stereotype.Service;

import java.util.List;


public interface PropertyService {

    //Property CRUD operation
    public UserProperty save(UserProperty userProperty);

    public UserProperty update(UserProperty userProperty);

    public UserProperty delete(UserProperty userProperty);

    //get property information
    public UserProperty getPropertyById(Long id);
    public List<UserProperty> getPropertyByUserId(Long userId);
    public List<UserProperty> getPropertyByStatus(String status);
    public List<UserProperty> getPropertyByStatus(String status, Long userId);
    public UserProperty getUserPropertyByGeneratedUUID(UserProperty userProperty);
}
