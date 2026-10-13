package com.faisal.backend.dto;

public record ResponseProperty(String propertyName,
                               double propertyValue,
                               int room,
                               int bathroom,
                               double squareFeet,
                               String description,
                               String generatedUUID,
                               String concurrentStamp
) {}
