package com.ybe.ybe_toyproject3.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ybe.ybe_toyproject3.domain.user.dto.request.LoginRequest;
import com.ybe.ybe_toyproject3.domain.user.dto.request.SignUpRequest;
import com.ybe.ybe_toyproject3.domain.user.dto.response.LoginResponse;
import com.ybe.ybe_toyproject3.domain.user.dto.response.SignUpResponse;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.domain.user.repository.UserRepository;
import com.ybe.ybe_toyproject3.global.common.Authority;
import com.ybe.ybe_toyproject3.global.error.response.ErrorResponse;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.ybe.ybe_toyproject3.global.common.Authority.ROLE_USER;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
class AuthControllerIntegrateTest {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    static final String AUTH_URL = "/auth";

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .name("userForTest")
                .email("userForTest@email.com")
                .authority(ROLE_USER)
                .password(passwordEncoder.encode("12341234"))
                .build();
        userRepository.save(user);

    }

    @AfterEach
    void tearDown() {
        userRepository.deleteByEmail("userForTest@email.com");
    }

    @Test
    @DisplayName("회원가입_성공_테스트")
    public void signUpSuccess() throws Exception{
        // given
        SignUpRequest signUpRequest = new SignUpRequest("tempuser", "tempuser@email.com", "12341234");


        // when
        ResultActions resultActions = mvc.perform(post(AUTH_URL + "/signup")
                        .content(objectMapper.writeValueAsBytes(signUpRequest))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    SignUpResponse signUpResponse = objectMapper.readValue(contentAsString, SignUpResponse.class);
                    assert signUpResponse != null;
                    assert signUpResponse.getId() != null;
                    assert signUpResponse.getEmail().equals("tempuser@email.com");
                });

    }

    @Test
    @DisplayName("회원가입_실패_테스트_중복된_이메일")
    public void signUpFail() throws Exception{
        // given
        SignUpRequest signUpRequest = new SignUpRequest("tempuser", "userForTest@email.com", "12341234");


        // when
        ResultActions resultActions = mvc.perform(post(AUTH_URL + "/signup")
                        .content(objectMapper.writeValueAsBytes(signUpRequest))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isConflict())
                .andDo(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    ErrorResponse errorResponse = objectMapper.readValue(contentAsString, ErrorResponse.class);
                    assert errorResponse != null;
                    assert errorResponse.getErrorCode() == 409;
                });

    }

    @Test
    @DisplayName("로그인_성공_테스트")
    public void loginSuccess() throws Exception{
        // given
        LoginRequest loginRequest = new LoginRequest("userForTest@email.com", "12341234");


        // when
        ResultActions resultActions = mvc.perform(post(AUTH_URL + "/login")
                        .content(objectMapper.writeValueAsBytes(loginRequest))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(result -> {
                    String contentString = result.getResponse().getContentAsString();
                    LoginResponse loginResponse = objectMapper.readValue(contentString, LoginResponse.class);
                    assert loginResponse != null;
                    assert loginResponse.getId() != null;
                    assert loginResponse.getEmail().equals("userForTest@email.com");
                })
                .andDo(result -> {
                    boolean authorization = result.getResponse().containsHeader("Authorization");
                    assert authorization;
                });
    }

    @Test
    @DisplayName("로그인_실패_테스트")
    public void loginFail() throws Exception{
        // given
        LoginRequest loginRequest = new LoginRequest("userForTest1@email.com", "12341234");


        // when
        ResultActions resultActions = mvc.perform(post(AUTH_URL + "/login")
                        .content(objectMapper.writeValueAsBytes(loginRequest))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isConflict())
                .andDo(result -> {
                    String contentString = result.getResponse().getContentAsString();
                    ErrorResponse errorResponse = objectMapper.readValue(contentString, ErrorResponse.class);
                    assert errorResponse != null;
                    assert errorResponse.getErrorCode() == 409;
                });
    }

}