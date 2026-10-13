package com.faisal.backend.service;

import com.faisal.backend.model.UserProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;


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

    //testing to be use as filter
    public List<UserProperty> getAllProperty();
    public List<UserProperty> getAllProperty(String state, String district, Pageable pageable);

    public  List<UserProperty> getAllProperty(Pageable pageable);

    public List<UserProperty> getAllProperty(Pageable pageable, Long userId, int page, int size);
    public List<UserProperty> getAllProperty(Predicate<UserProperty> predicate);

}
