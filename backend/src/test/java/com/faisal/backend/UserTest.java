package com.faisal.backend;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.faisal.backend.model.UserInformation;
import com.faisal.backend.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional // rolls back seeded users after each test
public class UserTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    UserRepository repo;
    @Autowired
    PasswordEncoder encoder;


    @BeforeEach
    void seed() {
        save("admin", "admin-pass", "ADMIN", "USER");
        save("user", "user-pass", "USER");
    }

    private void save(String username, String rawPassword, String... roles) {
        UserInformation u = new UserInformation();
        u.setUsername(username);
        u.setPasswordHash(encoder.encode(rawPassword));
        u.setEnabled(true);
        u.getRoles().addAll(java.util.Set.of(roles));
        repo.save(u);
    }

    private String login(String username, String password) throws Exception {
        MvcResult r = mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andReturn();
        return com.jayway.jsonpath.JsonPath.read(r.getResponse().getContentAsString(), "$.data.accessToken");
    }

    @Test
    void loginWithValidCredentialsReturnsTokens() throws Exception {
        login("user", "user-pass");
    }

    @Test
    void loginWithWrongPasswordIsRejected() throws Exception {
        mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpointNeedsToken() throws Exception {
        mvc.perform(get("/api/ping")).andExpect(status().isUnauthorized());
    }

    @Test
    void userRoleCannotAccessAdminEndpoint() throws Exception {
        String token = login("user", "user-pass");
        mvc.perform(get("/admin/ping").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminRoleCanAccessAdminEndpoint() throws Exception {
        String token = login("admin", "admin-pass");
        mvc.perform(get("/admin/ping").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }



}
