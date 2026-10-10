package com.faisal.backend.dto;

import com.faisal.backend.model.UserProperty;

public record ResponseProperty(String propertyName,
                               double propertyValue,
                               int room,
                               int bathroom,
                               double squareFeet,
                               String description,
                               String generatedUUID,
                               String concurrentStamp
) {}
