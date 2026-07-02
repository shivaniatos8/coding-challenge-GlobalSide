package com.globalside.codingchallenge.rbac;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class RbacChallengeApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    //Admin should be able to access all Api endpoints
    @Test
    void admin_shouldAccessAllEndpoints() throws Exception {

        mockMvc.perform(get("/products")
                .with(httpBasic("admin","admin123")))
                .andExpect(status().isOk());

        mockMvc.perform(post("/products")
                .with(httpBasic("admin","admin123"))
                .contentType("application/json")
                .content("{\"id\": 10,\"name\": \"Mobile\",\"description\":\"Testing.\",\"currency\":\"EUR\",\"category\":\"Testing\",\"brand\":\"NONAME\",\"color\":\"pink\",\"price\": 20.99}"))
                .andExpect(status().isOk());
    }
    //Admin should be able to delete  products
    @Test
    void admin_shouldAccessDeleteEndpoints() throws Exception {
        mockMvc.perform(delete("/products/2")
                        .with(httpBasic("admin","admin123")))
                        .andExpect(status().isOk());
    }
    //Admin Should be able to update product details
    @Test
    void admin_shouldAccessPutEndpoints() throws Exception {

        mockMvc.perform(put("/products/2")
                        .with(httpBasic("admin","admin123"))
                        .contentType("application/json")
                        .content("{\"id\": 1,\"name\": \"Test1\",\"description\":\"Testing.\",\"currency\":\"EUR\",\"category\":\"Testing\",\"brand\":\"Global\",\"color\":\"black\",\"price\": 20.99}"))
                .andExpect(status().isOk());
    }
   //User should access products -Readonly
    @Test
    void user_shouldAccessGetEndpoint() throws Exception {

        mockMvc.perform(get("/products")
                        .with(httpBasic("user", "user123")))
                        .andExpect(status().isOk());
    }
    //User should not be able to access put,delete and post endpoint
    @Test
    void user_shouldDeniedEndpoint() throws Exception {

        mockMvc.perform(post("/products")
                        .with(httpBasic("user", "user123"))
                .contentType("application/json")
                .content("{\"id\": 10,\"name\": \"Test1\",\"description\":\"Testing.\",\"currency\":\"EUR\",\"category\":\"Testing\",\"brand\":\"Global\",\"color\":\"black\",\"price\": 20.99}"))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/products/1")
                        .with(httpBasic("user", "user123"))
                        .contentType("application/json")
                        .content("{\"id\": 10,\"name\": \"Test1\",\"description\":\"Testing.\",\"currency\":\"EUR\",\"category\":\"Testing\",\"brand\":\"Global\",\"color\":\"black\",\"price\": 20.99}"))
                        .andExpect(status().isForbidden());

        mockMvc.perform(delete("/products/10")
                            .with(httpBasic("user","user123")))
                            .andExpect(status().isForbidden());
    }
    //Unauthorised Access
    @Test
    void unauthorisedAccess_shouldBeDenied() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isUnauthorized());

    }
    //Wrong Credentials
    @Test
    void wrongCredentials_shouldBeDenied() throws Exception {
        mockMvc.perform(get("/products")
                .with(httpBasic("Shivani","user12345")))
                .andExpect(status().isUnauthorized());
    }

    }
