package com.faisal.backend.service;

import com.faisal.backend.model.UserProperty;
import com.faisal.backend.repository.PropertyRepository;
import com.faisal.backend.util.CustomRandomGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {


    private final PropertyRepository propertyRepository;

    @Autowired
    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }


    @Override
    public UserProperty save(UserProperty userProperty) {

        if(userProperty.getGeneratedUUID() == null || userProperty.getGeneratedUUID().isEmpty()){
            //create new post

            //set the generated uuid of the object ;
            userProperty.setGeneratedUUID(CustomRandomGenerator.generateUUID());
            userProperty.setConcurrentStamp(CustomRandomGenerator.generateUUID());

            userProperty.setStatus('A');
            userProperty.setCreatedDate(LocalDateTime.now());
            return propertyRepository.save(userProperty);
        }


        return update(userProperty);
    }

    @Override
    public UserProperty update(UserProperty userProperty) {

        //check the property concurrent stamp , if different - reject
        if (validateConcurrentStamp(userProperty)) {
            throw new DataIntegrityViolationException("concurrent stamp not match for " + userProperty.getGeneratedUUID());
        }

        //get old data
        UserProperty old = getUserPropertyByGeneratedUUID(userProperty);

        if (old == null) {
            throw new DataIntegrityViolationException("property not found for " + userProperty.getGeneratedUUID());
        }

        //copy the properties
        BeanUtils.copyProperties(userProperty,old,
                //list of fields need to be ignored
                "id", "generatedUUID", "createdDate", "createdBy", "concurrentStamp"
                );

        //put in audit trail
        old.setModifiedDate(LocalDateTime.now());
        //TODO - put in modified by using jwt token

        return this.propertyRepository.save(old);
    }

    @Override
    public UserProperty delete(UserProperty userProperty) {
        return null;
    }

    @Override
    public UserProperty getPropertyById(Long id) {
        return null;
    }

    @Override
    public List<UserProperty> getPropertyByUserId(Long userId) {
        return List.of();
    }

    @Override
    public List<UserProperty> getPropertyByStatus(String status) {
        return List.of();
    }

    @Override
    public List<UserProperty> getPropertyByStatus(String status, Long userId) {
        return List.of();
    }

    @Override
    public UserProperty getUserPropertyByGeneratedUUID(UserProperty userProperty) {
        return  propertyRepository.findByGeneratedUUID(userProperty.getGeneratedUUID());
    }

    private boolean validateConcurrentStamp(UserProperty userProperty) {
        return !getUserPropertyByGeneratedUUID(
                userProperty)
                .getConcurrentStamp()
                .equals(userProperty.getConcurrentStamp());
    }
}
