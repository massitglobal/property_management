package com.faisal.backend.endpoint;


import com.faisal.backend.dto.GeneralRestTemplate;
import com.faisal.backend.dto.LoginRequest;
import com.faisal.backend.model.UserRoles;
import com.faisal.backend.security.JwtService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/auth")
public class UserAuthentication {

    private final AuthenticationManager authManager;
    private final JwtService jwt;

    @Autowired
    UserAuthentication(AuthenticationManager authManager, JwtService jwt) {
        this.authManager = authManager;
        this.jwt = jwt;
    }

    @PostMapping("/login")
    public ResponseEntity<?> getUserAuthentication(@RequestBody LoginRequest loginRequest){

        //authenticate username and password from db
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        //get all roles binding to the user
        List<String> roles = auth.getAuthorities().stream()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .toList();

        GeneralRestTemplate<LoginRequest>  loginResponse = new GeneralRestTemplate<>();

        loginRequest.setAccessToken(jwt.createAccessToken(loginRequest.getUsername(), roles));
        loginRequest.setRefreshToken(jwt.createRefreshToken(loginRequest.getUsername()));
        loginResponse.setData(loginRequest);

        return ResponseEntity.ok(loginResponse);
    }


}
