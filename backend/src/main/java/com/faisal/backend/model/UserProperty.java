package com.faisal.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_property")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String propertyName;
    private double propertyValue;
    private int room;
    private int bathroom;
    private double squareFeet ;

    private String description;

    private String remarks ;

    private Character status;

    private String state;

    public String district;

    //prevent hacking by auto increment
    private String generatedUUID;

    //prevent concurrent update if implement message queue technique;
    private String concurrentStamp;

    //audit trail
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String createdBy;
    private String modifiedBy;
}
