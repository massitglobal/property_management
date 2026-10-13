package com.faisal.backend.endpoint;


import com.faisal.backend.dto.GeneralRestTemplate;
import com.faisal.backend.dto.PropertySearch;
import com.faisal.backend.dto.ResponseProperty;
import com.faisal.backend.model.UserProperty;
import com.faisal.backend.repository.PropertyRepository;
import com.faisal.backend.service.PropertyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

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

    /*
    use this method to get listing after authenticated
    roles : admin - sees all listing
    user - sees own listing.
    cons: different from public listing , shud be public listing comes from the same method
     */
    @PostMapping("/list")
    public ResponseEntity<?> getAllProperty(@RequestBody PropertySearch property,
                                            @PageableDefault(size = 20) Pageable pageable) {

        GeneralRestTemplate<ResponseProperty> restTemplate = new GeneralRestTemplate<>();

        //testing to get all listing first
        List<UserProperty> list = propertyService.getAllProperty(property.getState(), property.getDistrict(), pageable);

        return ResponseEntity.ok().body(list);
    }

}
