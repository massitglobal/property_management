package com.faisal.backend.endpoint;


import com.faisal.backend.dto.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserAuthentication {

    @PostMapping("/login")
    public ResponseEntity<?> getUserAuthentication(@RequestBody LoginRequest loginRequest){

        return ResponseEntity.ok(loginRequest);
    }


}
