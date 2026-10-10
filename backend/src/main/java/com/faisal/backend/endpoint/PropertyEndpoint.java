package com.faisal.backend.endpoint;


import com.faisal.backend.dto.GeneralRestTemplate;
import com.faisal.backend.dto.ResponseProperty;
import com.faisal.backend.model.UserProperty;
import com.faisal.backend.repository.PropertyRepository;
import com.faisal.backend.service.PropertyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/property")
@Slf4j
public class PropertyEndpoint {

    private final PropertyService propertyService;

    @Autowired
    public PropertyEndpoint(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping("/")
    public ResponseEntity<?> saveProperty(@RequestBody UserProperty property) {

        GeneralRestTemplate<ResponseProperty> restTemplate = new GeneralRestTemplate<>();

        try {
            log.info("saving property : {}",property.getGeneratedUUID());
            property = propertyService.save(property);
            log.info("saving successfully : {}",property.getGeneratedUUID());
        } catch (DataIntegrityViolationException e) {
            //todo, need to change this to reflect properly in log
            log.info("error saving property {} : {}",property.getGeneratedUUID(), e.getMessage());
            restTemplate.setResponseCode(String.valueOf(HttpStatus.CONFLICT));
            restTemplate.setResponseMessage("cannot save property : " + property.getGeneratedUUID());

            return ResponseEntity.status(HttpStatus.CONFLICT).body(restTemplate);
        }

        //tuck in property into record after saving
        ResponseProperty responseProperty = new ResponseProperty(property.getPropertyName(), property.getPropertyValue(),
                property.getRoom(), property.getBathroom(), property.getSquareFeet(),  property.getDescription(),
                property.getGeneratedUUID(), property.getConcurrentStamp());

        restTemplate.setData(responseProperty);
        restTemplate.setResponseCode(HttpStatus.CREATED.toString());
        restTemplate.setResponseMessage("save property successfully");
        return ResponseEntity.ok().body(restTemplate);

    }

    public ResponseEntity<?> getAllProperty(@RequestBody UserProperty property) {

        //feed in dummy to see the list properly return



        return ResponseEntity.ok().body(property);
    }

}
