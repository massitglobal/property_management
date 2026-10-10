package com.faisal.backend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GeneralRestTemplate <T>{


    private String responseCode;
    private String responseMessage;

    //using raw type to return to front end
    private T data;
}
